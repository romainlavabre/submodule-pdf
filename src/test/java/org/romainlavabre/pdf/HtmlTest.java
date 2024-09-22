package org.romainlavabre.pdf;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public class HtmlTest {
    private static  boolean    isSetupDone = false;
    protected final PdfBuilder pdfBuilder  = new PdfBuilderImpl();


    @Before
    public void before() throws IOException {
        if ( isSetupDone ) {
            return;
        }

        isSetupDone = true;

        if ( Files.exists( Path.of( ".develop" ) ) ) {
            Files.list( Path.of( ".develop" ) ).forEachOrdered( new Consumer< Path >() {
                @Override
                public void accept( Path path ) {
                    try {
                        Files.delete( path );
                    } catch ( IOException e ) {
                        throw new RuntimeException( e );
                    }
                }
            } );
        }

        Files.deleteIfExists( Path.of( ".develop" ) );
        Files.createDirectory( Path.of( ".develop" ) );

        PdfConfigurer.init()
                .setPdfTmpDirectory( ".develop" );
    }


    @Test
    public void test_with_html_only() throws Exception {
        pdfBuilder.build( Files.readString( Path.of( "./src/test/resources/html/test1.html" ) ) );
    }


    @Test
    public void test_with_html_and_css() throws Exception {
        pdfBuilder.build( Files.readString( Path.of( "./src/test/resources/html/test2.html" ) ) );
    }


    @Test
    public void test_with_html_and_css_and_footer_and_header() throws Exception {
        pdfBuilder.build( Files.readString( Path.of( "./src/test/resources/html/test3.html" ) ) );
    }


    @Test
    public void test_with_html_and_css_and_footer_and_header_on_multiple_page() throws Exception {
        pdfBuilder.build( Files.readString( Path.of( "./src/test/resources/html/test4.html" ) ) );
    }


    @Test
    public void test_with_html_and_css_and_complex_footer_and_header() throws Exception {
        pdfBuilder.build( Files.readString( Path.of( "./src/test/resources/html/test5.html" ) ) );
    }


    @Test
    public void test_real_case_1() throws Exception {
        pdfBuilder.build( Files.readString( Path.of( "./src/test/resources/html/real_case_1.html" ) ) );
    }
}
