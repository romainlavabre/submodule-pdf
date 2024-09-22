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
}
