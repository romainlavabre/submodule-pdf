package org.romainlavabre.pdf;

import java.io.IOException;

public class BackgroundPdfBuildingTool {

    protected static Process process;

    public static void start(int port) {
        String command = "google-chrome --headless --no-sandbox --no-pdf-header-footer --disable-gpu --remote-debugging-port="
                + port;
        final String[] cmdline = { "sh", "-c", command };
        final Runtime runtime = Runtime.getRuntime();

        try {
            process = runtime.exec( cmdline );
            process.waitFor();
        } catch ( final IOException | InterruptedException e ) {
            e.printStackTrace();
        }

    }

    public static void stop()  {
        if (process != null) {
            process.destroy();
        }
    }

}
