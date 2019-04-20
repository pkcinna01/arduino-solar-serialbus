package com.xmonit.solar.arduino.serial;


import com.xmonit.solar.arduino.ArduinoConfig;
import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.SerialCmd;
import com.xmonit.solar.arduino.data.Eeprom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

public class ArduinoSerialBusGroup {

    private LinkedHashMap<Integer,ArduinoSerialBus> mapImpl = new LinkedHashMap<>();  // hiding this because put/get are not type safe (take an Object)

    private static final Logger logger = LoggerFactory.getLogger(ArduinoSerialBusGroup.class);

    private ArduinoConfig config;
    private Class<? extends ArduinoSerialPort> portClass;
    private String ttyRegEx;

    public void close() {
        for ( ArduinoSerialBus bus : mapImpl.values() ) {
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
            for( int initAttempt = 1; initAttempt <= 3; initAttempt++) {
                try{
                    Eeprom eeprom = new SerialCmd(serialBus).getEeprom();
                    String deviceName = eeprom.getDeviceName();
                    Integer deviceId = eeprom.getDeviceId();
                    serialBus.init(deviceName,deviceId);
                    mapImpl.put(deviceId,serialBus);
                    break;
                } catch (Exception ex) {
                    logger.error("Failed initializing serialbus for " + sp.getPortName() + " (attempt=" + initAttempt + ")");
                }
            }
        }
    }

    public ArduinoSerialBus getByName(String arduinoName) {
        return mapImpl.values().stream().filter( arduino -> arduino.name == arduinoName ).findAny().orElse(null);
    }

    public ArduinoSerialBus getById(Integer id) {
        return mapImpl.get(id);
    }

    public Collection<ArduinoSerialBus> values() {
        return mapImpl.values();
    }

    public void reload() throws ArduinoException {
        close();
        mapImpl.clear();
        populate();
    }
}
