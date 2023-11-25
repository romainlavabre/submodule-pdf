package org.romainlavabre.pdf;

import org.romainlavabre.pdf.exception.NotInitializedException;

public class PdfConfigurer {
    private static PdfConfigurer INSTANCE;
    private        String        pdfTmpDirectory;


    public PdfConfigurer() {
        INSTANCE = this;
    }


    protected static PdfConfigurer get() {
        if ( INSTANCE == null ) {
            throw new NotInitializedException();
        }

        return INSTANCE;
    }


    public static PdfConfigurer init() {
        return new PdfConfigurer();
    }


    public PdfConfigurer setPdfTmpDirectory( String path ) {
        pdfTmpDirectory = path;

        return this;
    }


    public void build() {
    }


    protected String getPdfTmpDirectory() {
        return pdfTmpDirectory;
    }
}
