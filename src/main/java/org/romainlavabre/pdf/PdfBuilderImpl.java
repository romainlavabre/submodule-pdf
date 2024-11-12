package org.romainlavabre.pdf;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public abstract class PdfBuilderImpl implements PdfBuilder {

    protected static String command = "";

    @Override
    public File build( final String html ) {
        return build( html, null, null );
    }


    @Override
    public File build( String html, String footer ) {
        return build( html, footer, null );
    }



    // @todo check if command isBlank, log error
    @Override
    public File build( String html, String footer, String header ) {
        final String tmpFile  = PdfConfigurer.get().getPdfTmpDirectory() + "/" + UUID.randomUUID() + ".html";
        final String filename = PdfConfigurer.get().getPdfTmpDirectory() + "/" + UUID.randomUUID() + ".pdf";

        try {
            Files.writeString( Path.of( tmpFile ), html );
        } catch ( final IOException e ) {
            e.printStackTrace();
            return null;
        }

        final String[] cmdline = {
                "sh",
                "-c",
                command
        };

        final Runtime runtime = Runtime.getRuntime();

        try {
            final Process process = runtime.exec( cmdline );
            process.waitFor();
        } catch ( final IOException | InterruptedException e ) {
            e.printStackTrace();
            return null;
        }

        final File tmp = new File( tmpFile );
        tmp.delete();

        return new File( filename );
    }

    protected void setCommand( String html, String footer, String header ) {
        final String tmpFile  = PdfConfigurer.get().getPdfTmpDirectory() + "/" + UUID.randomUUID() + ".html";
        final String filename = PdfConfigurer.get().getPdfTmpDirectory() + "/" + UUID.randomUUID() + ".pdf";

        this.command = "google-chrome --headless --no-margins --disable-gpu --print-to-pdf=" + filename + " " + tmpFile;
    }

}
