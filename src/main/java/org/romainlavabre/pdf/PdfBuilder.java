package org.romainlavabre.pdf;

import org.romainlavabre.pdf.exception.PdfGenerationException;
import org.romainlavabre.pdf.exception.PdfTimeoutException;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface PdfBuilder {
    /**
     * Nothing is left on disk nor running once it returns or throws.
     *
     * @param html Pdf body in html
     * @return the content of the PDF
     * @throws PdfTimeoutException    Chrome ran beyond the timeout and was killed
     * @throws PdfGenerationException Chrome could not produce the PDF
     */
    byte[] build( String html );
}
