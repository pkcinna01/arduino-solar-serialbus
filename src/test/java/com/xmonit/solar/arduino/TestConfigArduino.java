package com.xmonit.solar.arduino;

public class TestConfigArduino implements ArduinoConfig {

    String cmd = "GET,SENSORS";
    String commPortRegEx = "ttyUSB0";

    @Override
    public String getCmd() {
        return cmd;
    }

    @Override
    public String getCommPortRegEx() {
        return commPortRegEx;
    }

    public Integer getBaudRate() { return 115200; }
}