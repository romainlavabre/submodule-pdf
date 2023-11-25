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
        final String tmpFile  = PdfConfigurer.get().getPdfTmpDirectory() + "/" + UUID.randomUUID() + ".html";
        final String filename = PdfConfigurer.get().getPdfTmpDirectory() + "/" + UUID.randomUUID() + ".pdf";

        try {
            Files.writeString( Path.of( tmpFile ), html );
        } catch ( final IOException e ) {
            e.printStackTrace();
            return null;
        }

        final String[] cmdline = { "sh", "-c", "wkhtmltopdf " + tmpFile + " " + filename };

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
}
