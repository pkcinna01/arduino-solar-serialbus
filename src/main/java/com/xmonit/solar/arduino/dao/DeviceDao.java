package com.xmonit.solar.arduino.dao;

import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.ArduinoSerialBus;
import com.xmonit.solar.arduino.data.device.Device;
import org.apache.commons.lang3.StringUtils;

public class DeviceDao extends Dao {

    public DeviceDao(ArduinoSerialBus serialBus){
        super(serialBus);
    }

    @Override
    public Device[] findByTitleLike(String wildcardFilter, boolean bVerbose) throws ArduinoException {
        return doCommand("include,devices," + wildcardFilter, "devices", Device[].class, bVerbose);
    }

    @Override
    public Device[] findByTitleNotLike(String wildcardFilter, boolean bVerbose) throws ArduinoException {
        return doCommand("exclude,devices," + wildcardFilter, "devices", Device[].class, bVerbose);
    }

    @Override
    public Device[] findWhereIdIn(int[] ids, boolean bVerbose) throws ArduinoException {
        return doCommand("get,device," + StringUtils.join(ids, ','), "device", Device[].class, bVerbose);
    }

    @Override
    public Device get(int id, boolean bVerbose) throws ArduinoException {
        return doCommand("get,device," + id, "device", Device[].class, bVerbose)[0];
    }

    @Override
    public Device[] list(boolean bVerbose) throws ArduinoException {
        return doCommand("get,devices", "devices", Device[].class, bVerbose);
    }
}