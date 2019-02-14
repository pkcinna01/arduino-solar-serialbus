package com.xmonit.solar.arduino.dao.device;

import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.serial.ArduinoSerialBus;
import com.xmonit.solar.arduino.dao.DomainDao;
import com.xmonit.solar.arduino.dao.constraint.ConstraintDao;
import com.xmonit.solar.arduino.data.constraint.Constraint;
import com.xmonit.solar.arduino.data.device.Device;
import org.apache.commons.lang3.StringUtils;

public class DeviceDao extends DomainDao {

    public class ConstraintAccessor extends DeviceFieldAccessor<Constraint> {
        public ConstraintAccessor(int deviceId) {
            super(deviceId, "constraint", Constraint.class);
        }
        @Override
        public void set(Constraint c) throws ArduinoException { throw new ArduinoException("Not supported", 500); }
    }

    public class DeviceFieldAccessor<ResultT> extends ObjectFieldAccessor<ResultT> {
        public DeviceFieldAccessor(int id, String fieldName, Class<ResultT> c) {
            super(id, ObjectType.DEVICE, fieldName, c);
        }
    }

    public DeviceDao(ArduinoSerialBus serialBus){
        super(serialBus);
    }

    public ConstraintAccessor constraint(int deviceId) {
        return new ConstraintAccessor(deviceId);
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

    public ConstraintDao.ModeAccessor mode(int deviceId) throws ArduinoException {
        int constraintId = -1;
        if ( deviceId >= 0) {
            Device d = get(deviceId, true);
            constraintId = d.constraint.id;
        }
        ConstraintDao constraintDao = new ConstraintDao(serialBus);
        return constraintDao.mode(constraintId);
    }


}