package org.romainlavabre.pdf;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class PdfBuilderImpl implements PdfBuilder {


    @Override
    public File build( final String html ) {
        final String filename = PdfConfigurer.get().getPdfTmpDirectory() + "/" + UUID.randomUUID() + ".pdf";
        File         dest     = new File( filename );

        try {
            ConverterProperties converterProperties = new ConverterProperties();
            HtmlConverter.convertToPdf(
                    html,
                    new FileOutputStream( dest ),
                    converterProperties
            );
        } catch ( IOException e ) {
            e.printStackTrace();
        }


        return dest;
    }
}
