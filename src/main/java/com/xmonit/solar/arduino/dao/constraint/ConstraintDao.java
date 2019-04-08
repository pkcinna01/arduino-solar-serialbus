package com.xmonit.solar.arduino.dao.constraint;

import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.dao.DomainDao;
import com.xmonit.solar.arduino.dao.annotation.ArduinoDao;
import com.xmonit.solar.arduino.dao.annotation.BooleanAccessor;
import com.xmonit.solar.arduino.dao.annotation.ChoiceAccessor;
import com.xmonit.solar.arduino.data.constraint.Constraint;
import com.xmonit.solar.arduino.serial.ArduinoSerialBus;
import org.apache.commons.lang3.StringUtils;

@ArduinoDao
public class ConstraintDao extends DomainDao {

    public class ConstraintFieldAccessor<ResultT> extends ObjectFieldAccessor<ResultT> {
        public ConstraintFieldAccessor(int id, String fieldName, Class<ResultT> c) {
            super(id, ObjectType.CONSTRAINT, fieldName, c);
        }
    }

    public class ModeAccessor extends ConstraintFieldAccessor<Constraint.Mode> {
        public ModeAccessor(int id) {
            super(id, "mode", Constraint.Mode.class);
        }
    }

    public class PassedAccessor extends ConstraintFieldAccessor<Boolean> {
        public PassedAccessor(int id) {
            super(id, "passed", Boolean.class);
        }
    }

    public ConstraintDao(ArduinoSerialBus sb) {
        super(sb);
    }

    @Override
    public Constraint[] findByTitleLike(String wildcardFilter, boolean bVerbose) throws ArduinoException {
        return doCommand("include,constraints," + stringArg(wildcardFilter), "constraints", Constraint[].class, bVerbose);
    }

    @Override
    public Constraint[] findByTitleNotLike(String wildcardFilter, boolean bVerbose) throws ArduinoException {
        return doCommand("exclude,constraints," + stringArg(wildcardFilter), "constraints", Constraint[].class, bVerbose);
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

    @ChoiceAccessor
    public ModeAccessor mode(int id) {
        return new ModeAccessor(id);
    }

    @BooleanAccessor
    public PassedAccessor passed(int id) {
        return new PassedAccessor(id);
    }

}