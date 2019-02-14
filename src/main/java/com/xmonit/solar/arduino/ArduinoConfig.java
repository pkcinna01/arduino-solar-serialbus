package com.xmonit.solar.arduino;


public interface ArduinoConfig {

    public class PortConfig {
        public int baudRate, dataBits, stopBits, parity;
    }

    public PortConfig getPortConfig(String deviceId);


}
