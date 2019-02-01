package com.xmonit.solar.arduino.dao;

import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.ArduinoSerialBus;
import com.xmonit.solar.arduino.data.constraint.Constraint;

public class ConstraintDao extends Dao {

    public ConstraintDao(ArduinoSerialBus sb) {
        super(sb);
    }

    @Override
    public Constraint[] list() throws ArduinoException {
        return doCommand("get,constraints", "constraints", Constraint[].class);
    }

    @Override
    public Constraint get(int id) throws ArduinoException {
        return doCommand("get,constraint," + id, "constraint", Constraint.class);
    }

    @Override
    public Constraint[] findByTitleLike(String wildcardFilter) throws ArduinoException {
        return doCommand("include,constraints," + wildcardFilter, "constraints", Constraint[].class);
    }

    @Override
    public Constraint[] findByTitleNotLike(String wildcardFilter) throws ArduinoException {
        return doCommand("exclude,constraints," + wildcardFilter, "constraints", Constraint[].class);
    }


}