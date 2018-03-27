package com.xmonit.solar.serialbus;

import com.xmonit.solar.serialbus.data.ArduinoResponseTest;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;

public class SerialBusTest {

    static SerialBus serialBus;

    @org.junit.Test
    public void parseAndPrintArduinoResponse() throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        JsonNode respNode = mapper.readTree(ArduinoResponseTest.strJsonResponse);

        serialBus.processResponse(respNode);

    }

    @Before
    public void initAll() {

        serialBus = new SerialBus( new TestConfig(), new PrintResponseHandler());
        serialBus.commPortName = "ttyACM999";
    }

}