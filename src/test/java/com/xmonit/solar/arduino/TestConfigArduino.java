package com.xmonit.solar.arduino;

import com.xmonit.solar.arduino.serial.ArduinoSerialPort;

public class TestConfigArduino implements ArduinoConfig {

    String commPortRegEx = "ttyUSB0";

    @Override
    public ArduinoConfig.PortConfig getPortConfig(String devicedId) {

        ArduinoConfig.PortConfig config = new ArduinoConfig.PortConfig();
        //config.baudRate = 115200;
        config.baudRate = 38400;
        config.dataBits = ArduinoSerialPort.DATABITS_8;
        config.parity = ArduinoSerialPort.PARITY_NONE;
        config.stopBits = ArduinoSerialPort.STOPBITS_1;
        return config;
    }

}