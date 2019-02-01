package com.xmonit.solar.arduino.dao;

import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.ArduinoSerialBus;
import com.xmonit.solar.arduino.data.sensor.Sensor;

public class SensorDao extends Dao {

    public SensorDao(ArduinoSerialBus serialBus) {
        super(serialBus);
    }

    @Override
    public Sensor[] list() throws ArduinoException {
        return doCommand("get,sensors", "sensors", Sensor[].class);
    }

    @Override
    public Sensor get(int id) throws ArduinoException {
        return doCommand("get,sensor," + id, "sensor", Sensor.class);
    }

    @Override
    public Sensor[] findByTitleLike(String wildcardFilter) throws ArduinoException {
        return doCommand("include,sensors," + wildcardFilter, "sensors", Sensor[].class);
    }

    @Override
    public Sensor[] findByTitleNotLike(String wildcardFilter) throws ArduinoException {
        return doCommand("exclude,sensors," + wildcardFilter, "sensors", Sensor[].class);
    }

}