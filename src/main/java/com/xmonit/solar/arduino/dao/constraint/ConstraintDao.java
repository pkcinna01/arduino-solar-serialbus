package com.xmonit.solar.arduino.dao.constraint;

import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.ArduinoSerialBus;
import com.xmonit.solar.arduino.dao.Dao;
import com.xmonit.solar.arduino.data.constraint.Constraint;
import org.apache.commons.lang3.StringUtils;

public class ConstraintDao extends Dao {

    public ConstraintDao(ArduinoSerialBus sb) {
        super(sb);
    }

    @Override
    public Constraint[] findByTitleLike(String wildcardFilter, boolean bVerbose) throws ArduinoException {
        return doCommand("include,constraints," + wildcardFilter, "constraints", Constraint[].class, bVerbose);
    }

    @Override
    public Constraint[] findByTitleNotLike(String wildcardFilter, boolean bVerbose) throws ArduinoException {
        return doCommand("exclude,constraints," + wildcardFilter, "constraints", Constraint[].class, bVerbose);
    }

    @Override
    public Constraint[] findWhereIdIn(int[] ids, boolean bVerbose) throws ArduinoException {
        return doCommand("get,constraint," + StringUtils.join(ids, ','), "constraint", Constraint[].class, bVerbose);
    }

    @Override
    public Constraint get(int id, boolean bVerbose) throws ArduinoException {
        return doCommand("get,constraint," + id, "constraint", Constraint[].class, bVerbose)[0];
    }

    @Override
    public Constraint[] list(boolean bVerbose) throws ArduinoException {
        return doCommand("get,constraints", "constraints", Constraint[].class, bVerbose);
    }

    public class ConstraintFieldAccessor<ResultT> extends ObjectFieldAccessor<ResultT> {
        public ConstraintFieldAccessor(int id, String fieldName, Class<ResultT> c) {
            super(id, ObjectType.CONSTRAINT, fieldName, c);
        }
    }

    public class PassedAccessor extends ConstraintFieldAccessor<Boolean> {
        public PassedAccessor(int id) {
            super(id, "passed", Boolean.class);
        }
    }
    public PassedAccessor passed(int id) {
        return new PassedAccessor(id);
    }

    public class ModeAccessor extends ConstraintFieldAccessor<Constraint.Mode> {
        public ModeAccessor(int id) {
            super(id, "mode", Constraint.Mode.class);
        }
    }
    public ModeAccessor mode(int id) {
        return new ModeAccessor(id);
    }

}