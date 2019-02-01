package com.xmonit.solar.arduino.dao;

import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.ArduinoSerialBus;
import com.xmonit.solar.arduino.data.capability.Capability;

public class CapabilityDao extends Dao {

    public CapabilityDao(ArduinoSerialBus sb) {
        super(sb);
    }

    @Override
    public Capability[] list() throws ArduinoException {
        return doCommand("get,capabilities", "capabilities", Capability[].class);
    }

    @Override
    public Capability get(int id) throws ArduinoException {
        return doCommand("get,capability," + id, "capability", Capability.class);
    }

    @Override
    public Capability[] findByTitleLike(String wildcardFilter) throws ArduinoException {
        return doCommand("include,capabilities," + wildcardFilter, "capabilities", Capability[].class);
    }

    @Override
    public Capability[] findByTitleNotLike(String wildcardFilter) throws ArduinoException {
        return doCommand("exclude,capabilities," + wildcardFilter, "capabilities", Capability[].class);
    }
}