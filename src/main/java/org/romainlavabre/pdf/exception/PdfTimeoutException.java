package org.romainlavabre.pdf.exception;

public class PdfTimeoutException extends PdfGenerationException {
    public PdfTimeoutException( String message ) {
        super( message );
    }
}
