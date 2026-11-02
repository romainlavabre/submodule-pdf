package org.romainlavabre.pdf;

import org.romainlavabre.pdf.exception.PdfGenerationException;
import org.romainlavabre.pdf.exception.PdfTimeoutException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Chrome is started without a shell, so it is the process we hold and can kill; and in its own working
 * directory (HTML, PDF, profile), which every process it spawns names on its command line: whatever detached
 * from it is found and killed at the end.
 *
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class PdfBuilderImpl implements PdfBuilder {

    protected static final Duration KILL_WAIT = Duration.ofSeconds( 10 );
    protected static final Pattern  HEAD      = Pattern.compile( "<head(\\s[^>]*)?>", Pattern.CASE_INSENSITIVE );
    protected static final Pattern  DOCTYPE   = Pattern.compile( "^\\s*<!doctype[^>]*>", Pattern.CASE_INSENSITIVE );

    private Semaphore slots;


    @Override
    public byte[] build( String html ) {
        PdfConfigurer configurer = PdfConfigurer.get();
        Semaphore     slots      = slots( configurer );

        try {
            slots.acquire();
        } catch ( InterruptedException e ) {
            Thread.currentThread().interrupt();
            throw new PdfGenerationException( "Interrupted while waiting for a free generation slot", e );
        }

        Path workDirectory = null;

        try {
            // Absolute: it is the marker searched on the command lines, whatever Chrome makes of a relative path
            workDirectory = Files.createTempDirectory( Path.of( configurer.getPdfTmpDirectory() ).toAbsolutePath(), "pdf-" );

            return render( configurer, workDirectory, html );
        } catch ( IOException e ) {
            throw new PdfGenerationException( "The PDF could not be generated", e );
        } finally {
            if ( workDirectory != null ) {
                killLeftovers( workDirectory );
                delete( workDirectory );
            }

            slots.release();
        }
    }


    protected byte[] render( PdfConfigurer configurer, Path workDirectory, String html ) throws IOException {
        Path input  = workDirectory.resolve( "document.html" );
        Path output = workDirectory.resolve( "document.pdf" );
        Path log    = workDirectory.resolve( "chrome.log" );

        Files.writeString( input, withPolicy( configurer, html ), StandardCharsets.UTF_8 );

        Process process = new ProcessBuilder( command( configurer, workDirectory, input, output ) )
                .redirectErrorStream( true )
                .redirectOutput( log.toFile() )
                .start();

        Duration timeout = configurer.getTimeout();

        try {
            if ( !process.waitFor( timeout.toMillis(), TimeUnit.MILLISECONDS ) ) {
                kill( process );
                throw new PdfTimeoutException( "Chrome did not produce the PDF within " + timeout.toSeconds() + " s: killed" );
            }
        } catch ( InterruptedException e ) {
            kill( process );
            Thread.currentThread().interrupt();
            throw new PdfGenerationException( "Interrupted during the generation: Chrome killed", e );
        }

        if ( process.exitValue() != 0 || !Files.isRegularFile( output ) || Files.size( output ) == 0 ) {
            throw new PdfGenerationException( "Chrome exited with code " + process.exitValue() + " without a PDF: " + tail( log ) );
        }

        return Files.readAllBytes( output );
    }


    protected List< String > command( PdfConfigurer configurer, Path workDirectory, Path input, Path output ) {
        List< String > command = new ArrayList<>();
        command.add( configurer.getChromeBinary() );
        command.add( "--headless" );
        command.add( "--no-sandbox" );
        command.add( "--disable-gpu" );
        command.add( "--no-pdf-header-footer" );
        command.add( "--no-first-run" );
        command.add( "--no-default-browser-check" );
        command.add( "--disable-extensions" );
        command.add( "--disable-background-networking" );
        command.add( "--disable-component-update" );
        command.add( "--disable-default-apps" );
        command.add( "--disable-sync" );
        command.add( "--disable-crash-reporter" );
        // One profile per generation: two Chrome at once never share a profile lock
        command.add( "--user-data-dir=" + workDirectory.resolve( "profile" ) );

        if ( !configurer.isAllowNetwork() ) {
            // Every request goes to a dead proxy, loopback included, and no host name resolves
            command.add( "--proxy-server=127.0.0.1:9" );
            command.add( "--proxy-bypass-list=<-loopback>" );
            command.add( "--host-resolver-rules=MAP * ~NOTFOUND" );
        }

        command.add( "--print-to-pdf=" + output );
        command.add( input.toUri().toString() );

        return command;
    }


    /**
     * JavaScript and the loading of resources are refused by a Content-Security-Policy placed first in the
     * head, before any markup of the document: Chrome's switch to disable scripts also stops headless
     * printing. Without network, only inline styles and data: images and fonts load: no URL, no local file.
     */
    protected String withPolicy( PdfConfigurer configurer, String html ) {
        List< String > directives = new ArrayList<>();

        if ( !configurer.isAllowNetwork() ) {
            directives.add( "default-src 'none'" );
            directives.add( "img-src data:" );
            directives.add( "font-src data:" );
            directives.add( "style-src 'unsafe-inline'" );
        }

        if ( !configurer.isAllowJavascript() ) {
            directives.add( "script-src 'none'" );
        } else if ( !configurer.isAllowNetwork() ) {
            directives.add( "script-src 'unsafe-inline'" );
        }

        if ( directives.isEmpty() ) {
            return html;
        }

        String  meta = "<meta http-equiv=\"Content-Security-Policy\" content=\"" + String.join( "; ", directives ) + "\">";
        Matcher head = HEAD.matcher( html );

        if ( head.find() ) {
            return html.substring( 0, head.end() ) + meta + html.substring( head.end() );
        }

        Matcher doctype = DOCTYPE.matcher( html );

        if ( doctype.find() ) {
            return html.substring( 0, doctype.end() ) + meta + html.substring( doctype.end() );
        }

        return meta + html;
    }


    protected void kill( Process process ) {
        killTree( List.of( process.toHandle() ) );
    }


    /**
     * The descendants are listed before the roots die: once a parent is gone, its children no longer descend
     * from it.
     */
    protected void killTree( List< ProcessHandle > roots ) {
        List< ProcessHandle > all = new ArrayList<>();

        for ( ProcessHandle root : roots ) {
            all.add( root );
            all.addAll( root.descendants().toList() );
        }

        for ( ProcessHandle process : all ) {
            process.destroyForcibly();
        }

        for ( ProcessHandle process : all ) {
            waitForExit( process );
        }
    }


    /**
     * A process left behind is one whose command line names the working directory: Chrome passes its profile
     * to every process it spawns, the crash handler included, which detaches from it.
     */
    protected void killLeftovers( Path workDirectory ) {
        String                    marker    = workDirectory.toString();
        long                      self      = ProcessHandle.current().pid();
        List< ProcessHandle >     leftovers = new ArrayList<>();
        Iterator< ProcessHandle > processes = ProcessHandle.allProcesses().iterator();

        while ( processes.hasNext() ) {
            ProcessHandle candidate = processes.next();

            if ( candidate.pid() != self && candidate.info().commandLine().orElse( "" ).contains( marker ) ) {
                leftovers.add( candidate );
            }
        }

        killTree( leftovers );
    }


    protected void waitForExit( ProcessHandle process ) {
        try {
            process.onExit().get( KILL_WAIT.toMillis(), TimeUnit.MILLISECONDS );
        } catch ( InterruptedException e ) {
            Thread.currentThread().interrupt();
        } catch ( Exception ignored ) {
            // Still alive after SIGKILL: nothing more can be done from here
        }
    }


    protected void delete( Path workDirectory ) {
        try {
            Files.walkFileTree( workDirectory, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile( Path file, BasicFileAttributes attributes ) throws IOException {
                    Files.deleteIfExists( file );

                    return FileVisitResult.CONTINUE;
                }


                @Override
                public FileVisitResult postVisitDirectory( Path directory, IOException exception ) throws IOException {
                    Files.deleteIfExists( directory );

                    return FileVisitResult.CONTINUE;
                }
            } );
        } catch ( IOException e ) {
            throw new PdfGenerationException( "The working directory " + workDirectory + " could not be removed", e );
        }
    }


    protected String tail( Path log ) {
        try {
            String content = Files.readString( log, StandardCharsets.UTF_8 );

            return content.length() > 2000 ? content.substring( content.length() - 2000 ) : content;
        } catch ( IOException e ) {
            return "(no Chrome output)";
        }
    }


    /**
     * Sized at the first generation, once the configuration is built.
     */
    protected synchronized Semaphore slots( PdfConfigurer configurer ) {
        if ( slots == null ) {
            slots = new Semaphore( configurer.getMaxConcurrent(), true );
        }

        return slots;
    }
}
