package org.romainlavabre.pdf;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("WKHTMLPDFBuilderImpl")
public class ChromePDFBuilderImpl extends AbstractCLIPDFBuilderImpl{

    @Override
    protected void setCommand( String html, String footer, String header ) {
        final String tmpFile  = PdfConfigurer.get().getPdfTmpDirectory() + "/" + UUID.randomUUID() + ".html";
        final String filename = PdfConfigurer.get().getPdfTmpDirectory() + "/" + UUID.randomUUID() + ".pdf";

        this.command = "google-chrome --headless --no-margins --disable-gpu --print-to-pdf=" + filename + " " + tmpFile;
    }

}
