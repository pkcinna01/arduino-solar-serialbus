package com.xmonit.solar.arduino;

public class TestConfigArduino implements ArduinoConfig {

    String cmd = "GET";
    String commPortRegEx = "ttyACM.*";

    @Override
    public String getCmd() {
        return cmd;
    }

    @Override
    public String getCommPortRegEx() {
        return commPortRegEx;
    }

    public Integer getBaudRate() { return 57600; }
}