package com.xmonit.solar.arduino;

import org.junit.Before;

import java.io.PrintWriter;


public class ArduinoArduinoAboutTest {

    static ArduinoAbout about = new ArduinoAbout();

    @org.junit.Test
    public void printUsage() throws Exception {

        about.printHelp( new PrintWriter(System.out));

    }

    @Before
    public void initAll() {
    }

}