package org.romainlavabre.pdf;

import com.hubspot.chrome.devtools.client.ChromeDevToolsClient;
import com.hubspot.chrome.devtools.client.ChromeDevToolsSession;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class PdfBuilderImpl implements PdfBuilder {

    @Override
    public File build( String html ) {

        final String tmpFile  = PdfConfigurer.get().getPdfTmpDirectory() + "/" + UUID.randomUUID() + ".html";

        try {
            Files.writeString( Path.of( tmpFile ), html );
        } catch ( final IOException e ) {
            e.printStackTrace();
            return null;
        }

        final String filename = PdfConfigurer.get().getPdfTmpDirectory() + "/" + UUID.randomUUID() + ".pdf";

        ChromeDevToolsClient client = ChromeDevToolsClient.defaultClient();

        try ( ChromeDevToolsSession session = client.connect("127.0.0.1", 9292)) {
            // Control Chrome remotely
            session.navigate( tmpFile );
            byte[] pdfContent = session.printToPDF();
            Files.write( Path.of( filename ), pdfContent);
        } catch ( URISyntaxException e ) {
            throw new RuntimeException( e );
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }

        client.close();
        new File( tmpFile ).delete();
        return new File( filename );
    }


}
