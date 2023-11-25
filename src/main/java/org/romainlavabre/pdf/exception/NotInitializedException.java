package org.romainlavabre.pdf.exception;

public class NotInitializedException extends RuntimeException {
    public NotInitializedException() {
        super( "PdfBuilder not initialized, use PdfConfigurer for fix it" );
    }
}
