package com.xmonit.solar.arduino;

import com.xmonit.solar.arduino.serial.ArduinoSerialBus;
import com.xmonit.solar.arduino.serial.ArduinoSerialBusGroup;
import com.xmonit.solar.arduino.serial.JSCArduinoSerialPort;
import org.junit.After;
import org.junit.Before;

public class SerialBusTestSetup {

    public ArduinoSerialBusGroup serialBuses;
    public ArduinoSerialBus serialBus;

    @Before
    public void setUp() throws Exception {

        serialBuses = new ArduinoSerialBusGroup();
        ArduinoConfig conf = new TestConfigArduino();
        serialBuses.init( JSCArduinoSerialPort.class, "ttyUSB[0-9]+", conf );
        serialBus = serialBuses.values().iterator().next();
    }

    @After
    public void tearDown() throws Exception {

        serialBuses.close();
    }
}
