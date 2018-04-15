package com.xmonit.solar.serialbus;

import com.xmonit.solar.serialbus.data.ArduinoResponseTest;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;

import java.io.PrintWriter;


public class SerialBusAboutTest {

    static SerialBusAbout about = new SerialBusAbout();

    @org.junit.Test
    public void printUsage() throws Exception {

        about.printHelp( new PrintWriter(System.out));

    }

    @Before
    public void initAll() {
    }

}