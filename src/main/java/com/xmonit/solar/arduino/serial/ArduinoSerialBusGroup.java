package com.xmonit.solar.arduino.serial;


import com.xmonit.solar.arduino.ArduinoConfig;
import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.SerialCmd;
import com.xmonit.solar.arduino.data.Eeprom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ArduinoSerialBusGroup extends java.util.LinkedHashMap<String,ArduinoSerialBus> {

    private static final Logger logger = LoggerFactory.getLogger(ArduinoSerialBusGroup.class);

    private ArduinoConfig config;
    private Class<? extends ArduinoSerialPort> portClass;
    private String ttyRegEx;

    public void close() {
        for ( ArduinoSerialBus bus : this.values() ) {
            try {
                if ( bus.isOpen() ) {
                    bus.close();
                }
            } catch (Exception ex) {
                logger.error("Failed closing arduino serial bus: " + bus.getPortName(), ex);
            }
        }
    }

    public void init(Class<? extends ArduinoSerialPort> portClass, String ttyRegEx, ArduinoConfig config ) throws ArduinoException {

        this.portClass = portClass;
        this.ttyRegEx = ttyRegEx;
        this.config = config;

        populate();
    }

    private void populate() throws ArduinoException {

        List<ArduinoSerialPort> ports = ArduinoSerialPortFactory.createPorts(portClass, ttyRegEx, config);

        for ( ArduinoSerialPort sp : ports ) {
            ArduinoSerialBus serialBus = new ArduinoSerialBus(sp);
            // connect and get device name
            Eeprom eeprom = new SerialCmd(serialBus).getEeprom();
            String deviceName = eeprom.getDeviceName();
            Integer deviceId = eeprom.getDeviceId();
            serialBus.init(deviceName,deviceId);
            this.put(deviceName,serialBus);
        }
    }

    public void reload() throws ArduinoException {
        close();
        clear();
        populate();
    }
}
