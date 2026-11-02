# HTML to PDF with Chrome headless

```java
PdfConfigurer.init()
        .setPdfTmpDirectory( "/tmp/pdf" )   // required
        .setChromeBinary( "google-chrome" ) // default
        .setTimeout( Duration.ofSeconds( 60 ) ) // default
        .setMaxConcurrent( 2 )              // default, per JVM
        .build();

byte[] pdf = pdfBuilder.build( html );
```

## Guarantees

- **No zombie, no leftover**: Chrome is started without a shell, in its own working directory (HTML, PDF and
  profile). Beyond the timeout, Chrome and all its descendants are killed (`PdfTimeoutException`); at the end of
  every generation, any process whose command line names the working directory (the crash handler detaches from
  Chrome) is killed with its descendants, and the directory is removed. Run the container with an init process
  (`init: true`) so the killed processes are reaped.
- **At most `maxConcurrent` generations at once**: the others wait for a free slot.
- **No network, no JavaScript by default**: every request goes to a dead proxy and no host name resolves, and a
  Content-Security-Policy placed first in the head refuses scripts and every resource but inline styles and
  `data:` images and fonts. A markup injected in the HTML can neither run nor reach an internal service. Turn them
  on with `setAllowNetwork( true )` / `setAllowJavascript( true )` only for HTML free of any user-provided value.
- **Errors are thrown** (`PdfGenerationException`), never swallowed.

Chrome's own switch to disable scripts (`--blink-settings=scriptEnabled=false`) is not used: it also stops
headless printing, which then exits 0 without a PDF.
