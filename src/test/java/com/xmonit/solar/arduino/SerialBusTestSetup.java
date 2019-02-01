package com.xmonit.solar.arduino;

import org.junit.After;
import org.junit.Before;

public class SerialBusTestSetup {

    public ArduinoSerialBus serialBus;

    @Before
    public void setUp() throws Exception {
        serialBus = new ArduinoSerialBus( new TestConfigArduino(), new PrintResponseHandler());
    }

    @After
    public void tearDown() throws Exception {
        serialBus.close();
    }
}
