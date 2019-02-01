package com.xmonit.solar.arduino.dao;

import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.ArduinoSerialBus;
import com.xmonit.solar.arduino.data.device.Device;

public class DeviceDao extends Dao {

    public DeviceDao(ArduinoSerialBus serialBus){
        super(serialBus);
    }

    @Override
    public Device[] list() throws ArduinoException {
        return doCommand("get,devices", "devices", Device[].class);
    }

    @Override
    public Device get(int id) throws ArduinoException {
        return doCommand("get,device," + id, "device", Device.class);
    }

    @Override
    public Device[] findByTitleLike(String wildcardFilter) throws ArduinoException {
        return doCommand("include,devices," + wildcardFilter, "devices", Device[].class);
    }

    @Override
    public Device[] findByTitleNotLike(String wildcardFilter) throws ArduinoException {
        return doCommand("exclude,devices," + wildcardFilter, "devices", Device[].class);
    }
}