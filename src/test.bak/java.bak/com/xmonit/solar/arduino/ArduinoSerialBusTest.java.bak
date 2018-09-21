package com.xmonit.solar.arduino;

import com.xmonit.solar.arduino.data.ArduinoResponseTest;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;

public class ArduinoSerialBusTest {

    static ArduinoSerialBus serialBus;

    @org.junit.Test
    public void parseAndPrintArduinoResponse() throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        JsonNode respNode = mapper.readTree(ArduinoResponseTest.strJsonResponse);

        serialBus.processResponse(respNode);

    }

    @Before
    public void initAll() {

        serialBus = new ArduinoSerialBus( new TestConfigArduino(), new PrintResponseHandler());
        //serialBus.commPortName = "ttyACM999";
    }

}