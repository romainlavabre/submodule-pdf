package org.romainlavabre.pdf;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service("WKHTMLPDFBuilderImpl")
public class WKHTMLPDFBuilderImpl extends AbstractCLIPDFBuilderImpl{

    @Override
    protected void setCommand( String html, String footer, String header ) {
        final String tmpFile  = PdfConfigurer.get().getPdfTmpDirectory() + "/" + UUID.randomUUID() + ".html";
        final String filename = PdfConfigurer.get().getPdfTmpDirectory() + "/" + UUID.randomUUID() + ".pdf";

        this.command = "wkhtmltopdf --enable-javascript " + getHeaderCommand( header ) + " "
                + getFooterCommand( footer ) + " " + tmpFile + " " + filename;
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
