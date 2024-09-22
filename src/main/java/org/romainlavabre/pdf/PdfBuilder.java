package org.romainlavabre.pdf;

import java.io.File;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface PdfBuilder {
    /**
     * @param html Pdf body in html
     * @return Pdf
     */
    File build( String html );


    /**
     * @param html   Pdf body in html
     * @param footer Pdf footer in html
     * @return Pdf
     */
    File build( String html, String footer );


    /**
     * @param html   Pdf body in html
     * @param footer Pdf footer in html
     * @param header Pdf header in html
     * @return Pdf
     */
    File build( String html, String footer, String header );
}
