package com.xmonit.solar.arduino.dao;

import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.ArduinoSerialBus;
import com.xmonit.solar.arduino.data.capability.Capability;
import org.apache.commons.lang3.StringUtils;

public class CapabilityDao extends Dao {

    public CapabilityDao(ArduinoSerialBus sb) {
        super(sb);
    }

    @Override
    public Capability[] findByTitleLike(String wildcardFilter, boolean bVerbose) throws ArduinoException {
        return doCommand("include,capabilities," + wildcardFilter, "capabilities", Capability[].class, bVerbose);
    }

    @Override
    public Capability[] findByTitleNotLike(String wildcardFilter, boolean bVerbose) throws ArduinoException {
        return doCommand("exclude,capabilities," + wildcardFilter, "capabilities", Capability[].class, bVerbose);
    }

    @Override
    public Capability[] findWhereIdIn(int[] ids, boolean bVerbose) throws ArduinoException {
        return doCommand("get,capability," + StringUtils.join(ids, ','), "capability", Capability[].class, bVerbose);
    }

    @Override
    public Capability get(int id, boolean bVerbose) throws ArduinoException {
        return doCommand("get,capability," + id, "capability", Capability[].class, bVerbose)[0];
    }

    @Override
    public Capability[] list(boolean bVerbose) throws ArduinoException {
        return doCommand("get,capabilities", "capabilities", Capability[].class, bVerbose);
    }
}