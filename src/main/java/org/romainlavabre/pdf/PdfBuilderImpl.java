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
public class PdfBuilderImpl implements PdfBuilder {


    @Override
    public File build( final String html ) {
        return build( html, null, null );
    }


    @Override
    public File build( String html, String footer ) {
        return build( html, footer, null );
    }


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

        final String[] cmdline = { "sh", "-c", "wkhtmltopdf --enable-javascript " + getHeaderCommand( header ) + " " + getFooterCommand( footer ) + " " + tmpFile + " " + filename };

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


    protected String getHeaderCommand( String header ) {
        if ( header == null ) {
            return "";
        }

        final String tmpFile = PdfConfigurer.get().getPdfTmpDirectory() + "/" + UUID.randomUUID() + ".html";

        try {
            Files.writeString( Path.of( tmpFile ), "<!DOCTYPE HTML>" + header );
        } catch ( final IOException e ) {
            e.printStackTrace();
            return null;
        }

        return "--header-html " + tmpFile;
    }


    protected String getFooterCommand( String footer ) {
        if ( footer == null ) {
            return "";
        }

        final String tmpFile = PdfConfigurer.get().getPdfTmpDirectory() + "/" + UUID.randomUUID() + ".html";

        try {
            Files.writeString( Path.of( tmpFile ), "<!DOCTYPE html>" + footer );
        } catch ( final IOException e ) {
            e.printStackTrace();
            return null;
        }

        return "--footer-html " + tmpFile;
    }
}
