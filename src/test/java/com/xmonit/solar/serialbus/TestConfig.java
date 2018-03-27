package com.xmonit.solar.serialbus;

public class TestConfig implements SerialBusConfig {

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
}