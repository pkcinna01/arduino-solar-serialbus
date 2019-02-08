package com.xmonit.solar.arduino.dao;

import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.dao.constraint.ConstraintDao;
import com.xmonit.solar.arduino.dao.device.DeviceDao;
import com.xmonit.solar.arduino.data.constraint.Constraint;
import com.xmonit.solar.arduino.data.device.Device;

import org.junit.Before;

import static junit.framework.TestCase.assertEquals;

public class DeviceDaoTest extends DaoTestBase<DeviceDao, Device> {

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        dao = new DeviceDao(serialBus);
    }
    
    @org.junit.Test
    public void doListTest() throws ArduinoException {
        super.doListTest();
    }

    @org.junit.Test
    public void doVerboseListTest() throws ArduinoException {
        super.doVerboseListTest();
    }

    @org.junit.Test
    public void doFilterTest() throws ArduinoException {
        super.doFilterTest();
    }


    @org.junit.Test
    public void doGetTest() throws ArduinoException {
        super.doGetTest();
    }

    @org.junit.Test
    public void doFindWhereIdInTest() throws ArduinoException {
        super.doFindWhereIdInTest();
    }

    public int deviceId = 1;

    @org.junit.Test
    public void setModeTest() throws ArduinoException {
        Device orig = dao.get(deviceId);
        Constraint.Mode newMode = orig.constraint.mode != Constraint.Mode.TEST ? Constraint.Mode.TEST : Constraint.Mode.REMOTE_OR_TEST;
        ConstraintDao.ModeAccessor mode = dao.mode(deviceId);
        mode.set(newMode);
        Constraint c = dao.constraint(deviceId).get();
        System.out.println(c.toString() );
        assertEquals(c.mode,newMode);
        mode.set(orig.constraint.mode);
    }

}
