package org.romainlavabre.pdf;

import org.junit.Assert;

import java.io.File;

public class PdfBuilderTest {




    @org.junit.Test
    public void name() {
        PdfConfigurer.init().setPdfTmpDirectory( "/tmp" );
        PdfBuilder pdfBuilder = new PdfBuilderImpl();
        File pdfFile = pdfBuilder.build("<!DOCTYPE html>\n" +
                "<html>\n" +
                "\n" +
                "<head>\n" +
                "\n" +
                "    <style>\n" +
                "        .page-header, .page-header-space {\n" +
                "            height: 100px;\n" +
                "        }\n" +
                "\n" +
                "        .page-footer, .page-footer-space {\n" +
                "            height: 50px;\n" +
                "\n" +
                "        }\n" +
                "\n" +
                "        .page-footer {\n" +
                "            position: fixed;\n" +
                "            bottom: 0;\n" +
                "            width: 100%;\n" +
                "            border-top: 1px solid black; /* for demo */\n" +
                "            background: yellow; /* for demo */\n" +
                "        }\n" +
                "\n" +
                "        .page-header {\n" +
                "            position: fixed;\n" +
                "            top: 0mm;\n" +
                "            width: 100%;\n" +
                "            border-bottom: 1px solid black; /* for demo */\n" +
                "            background: yellow; /* for demo */\n" +
                "        }\n" +
                "\n" +
                "        .page {\n" +
                "            page-break-after: always;\n" +
                "        }\n" +
                "\n" +
                "        @page {\n" +
                "            margin: 20mm\n" +
                "        }\n" +
                "\n" +
                "        @media print {\n" +
                "            thead {display: table-header-group;}\n" +
                "            tfoot {display: table-footer-group;}\n" +
                "\n" +
                "            button {display: none;}\n" +
                "\n" +
                "            body {margin: 0;}\n" +
                "        }\n" +
                "\n" +
                "\n" +
                "    </style>\n" +
                "\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "\n" +
                "<div class=\"page-header\" style=\"text-align: center\">\n" +
                "    <img src=\"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ffairfair-assets.s3.eu-west-3.amazonaws.com%2FFFG-2.png&f=1&nofb=1&ipt=db40d25cc7eeeae05a114cb831a0c1b98b9f63f5782fcc9633e051d0a574b75b&ipo=images\" width=\"200\" height=\"64\" />\n" +
                "</div>\n" +
                "\n" +
                "<div class=\"page-footer\">\n" +
                "    I'm The Footer<br />\n" +
                "    I'm The Footer<br />\n" +
                "</div>\n" +
                "\n" +
                "<table>\n" +
                "\n" +
                "    <thead>\n" +
                "    <tr>\n" +
                "        <td>\n" +
                "            <!--place holder for the fixed-position header-->\n" +
                "            <div class=\"page-header-space\"></div>\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "    </thead>\n" +
                "\n" +
                "    <tbody>\n" +
                "    <tr>\n" +
                "        <td>\n" +
                "            <!--*** CONTENT GOES HERE ***-->\n" +
                "            <div class=\"page\">PAGE 1 mmmmm</div>\n" +
                "            <div class=\"page\">PAGE 2</div>\n" +
                "            <div class=\"page\" style=\"line-height: 3;\">\n" +
                "                PAGE 3 - Long Content\n" +
                "                <br/> Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc tincidunt metus eu consectetur rutrum. Praesent tempor facilisis dapibus. Aliquam cursus diam ac vehicula pulvinar. Integer lacinia non odio et condimentum. Aenean faucibus cursus\n" +
                "                mi, sed interdum turpis sagittis a. Quisque quis pellentesque mi. Ut erat eros, posuere sed scelerisque ut, pharetra vitae tellus. Suspendisse ligula sapien, laoreet ac hendrerit sit amet, viverra vel mi. Pellentesque faucibus nisl et dolor\n" +
                "                pharetra, vel mattis massa venenatis. Integer congue condimentum nisi, sed tincidunt velit tincidunt non. Nulla sagittis sed lorem pretium aliquam. Praesent consectetur volutpat nibh, quis pulvinar est volutpat id. Cras maximus odio posuere\n" +
                "                suscipit venenatis. Donec rhoncus scelerisque metus, in tempus erat rhoncus sed. Morbi massa sapien, porttitor id urna vel, volutpat blandit velit. Cras sit amet sem eros. Quisque commodo facilisis tristique. Proin pellentesque sodales rutrum.\n" +
                "                Vestibulum purus neque, congue vel dapibus in, venenatis ut felis. Donec et ligula enim. Sed sapien sapien, tincidunt vitae lectus quis, ultricies rhoncus mi. Nunc dapibus nulla tempus nunc interdum, sed facilisis ex pellentesque. Nunc vel\n" +
                "                lorem leo. Cras pharetra sodales metus. Cras lacus ex, consequat at consequat vel, laoreet ac dui. Curabitur aliquam, sapien quis congue feugiat, nisi nisl feugiat diam, sed vehicula velit nulla ac nisl. Aliquam quis nisi euismod massa blandit\n" +
                "                pharetra nec eget nunc. Etiam eros ante, auctor sit amet quam vel, fringilla faucibus leo. Morbi a pulvinar nulla. Praesent sed vulputate nisl. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Aenean commodo\n" +
                "                mollis iaculis. Maecenas consectetur enim vitae mollis venenatis. Ut scelerisque pretium orci id laoreet. In sit amet pharetra diam. Vestibulum in molestie lorem. Nunc gravida, eros non consequat fermentum, ex orci vestibulum orci, non accumsan\n" +
                "                sem velit ac lectus. Vivamus malesuada lacus nec velit dignissim, ac fermentum nulla pretium. Aenean mi nisi, convallis sed tempor in, porttitor eu libero. Praesent et molestie ante. Duis suscipit vitae purus sit amet aliquam. Vestibulum lectus\n" +
                "                justo, lobortis a purus a, dapibus efficitur metus. Suspendisse potenti. Duis dictum ex lorem. Suspendisse nec ligula consectetur magna hendrerit ullamcorper et eget mauris. Etiam vestibulum sodales diam, eget venenatis nunc luctus quis. Ut\n" +
                "                fermentum placerat neque nec elementum. Praesent orci erat, rhoncus vitae est eu, dictum molestie metus. Cras et fermentum elit. Aenean eget augue lacinia, varius ante in, ullamcorper dolor. Cras viverra purus non egestas consectetur. Nulla\n" +
                "                nec dolor ac lectus convallis aliquet sed a metus. Suspendisse eu imperdiet nunc, id pulvinar risus. Maecenas varius sagittis est, vel fermentum risus accumsan at. Vestibulum sollicitudin dui pharetra sapien volutpat, id convallis mi vestibulum.\n" +
                "                Phasellus commodo sit amet lorem quis imperdiet. Proin nec diam sed urna euismod ultricies at sed urna. Quisque ornare, nulla et vehicula ultrices, massa purus vehicula urna, ac sodales lacus leo vitae mi. Sed congue placerat justo at placerat.\n" +
                "                Aenean suscipit fringilla vehicula. Quisque iaculis orci vitae arcu commodo maximus. Maecenas nec nunc rutrum, cursus elit quis, porttitor sapien. Sed ac hendrerit ipsum, lacinia fringilla velit. Donec ultricies feugiat dictum.\n" +
                "            </div>\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "    </tbody>\n" +
                "\n" +
                "    <tfoot>\n" +
                "    <tr>\n" +
                "        <td>\n" +
                "            <!--place holder for the fixed-position footer-->\n" +
                "            <div class=\"page-footer-space\"></div>\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "    </tfoot>\n" +
                "\n" +
                "</table>\n" +
                "\n" +
                "</body>\n" +
                "\n" +
                "</html>");


    }
}
