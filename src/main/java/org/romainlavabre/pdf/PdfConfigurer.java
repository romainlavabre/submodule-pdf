package org.romainlavabre.pdf;

import org.romainlavabre.pdf.exception.NotInitializedException;

import java.time.Duration;

public class PdfConfigurer {
    private static PdfConfigurer INSTANCE;
    private        String        pdfTmpDirectory;
    private        String        chromeBinary    = "google-chrome";
    private        Duration      timeout         = Duration.ofSeconds( 60 );
    private        int           maxConcurrent   = 2;
    private        boolean       allowNetwork    = false;
    private        boolean       allowJavascript = false;


    public PdfConfigurer() {
        INSTANCE = this;
    }


    protected static PdfConfigurer get() {
        if ( INSTANCE == null ) {
            throw new NotInitializedException();
        }

        return INSTANCE;
    }


    public static PdfConfigurer init() {
        return new PdfConfigurer();
    }


    /**
     * Each generation works in its own sub-directory, removed at the end whatever the outcome.
     */
    public PdfConfigurer setPdfTmpDirectory( String path ) {
        pdfTmpDirectory = path;

        return this;
    }


    /**
     * The Chrome executable, google-chrome by default.
     */
    public PdfConfigurer setChromeBinary( String chromeBinary ) {
        this.chromeBinary = chromeBinary;

        return this;
    }


    /**
     * Beyond it, Chrome and all its child processes are killed. 60 seconds by default.
     */
    public PdfConfigurer setTimeout( Duration timeout ) {
        this.timeout = timeout;

        return this;
    }


    /**
     * Generations running at once in this JVM; the others wait for a free slot. 2 by default. Read once, at
     * the first generation.
     */
    public PdfConfigurer setMaxConcurrent( int maxConcurrent ) {
        this.maxConcurrent = maxConcurrent;

        return this;
    }


    /**
     * Off by default: Chrome loads nothing from the network, so a markup injected in the HTML cannot reach an
     * internal service. Turn it on only for HTML free of any user-provided value.
     */
    public PdfConfigurer setAllowNetwork( boolean allowNetwork ) {
        this.allowNetwork = allowNetwork;

        return this;
    }


    /**
     * Off by default, for the same reason as the network.
     */
    public PdfConfigurer setAllowJavascript( boolean allowJavascript ) {
        this.allowJavascript = allowJavascript;

        return this;
    }


    public void build() {
    }


    protected String getPdfTmpDirectory() {
        return pdfTmpDirectory;
    }


    protected String getChromeBinary() {
        return chromeBinary;
    }


    protected Duration getTimeout() {
        return timeout;
    }


    protected int getMaxConcurrent() {
        return maxConcurrent;
    }


    protected boolean isAllowNetwork() {
        return allowNetwork;
    }


    protected boolean isAllowJavascript() {
        return allowJavascript;
    }
}
