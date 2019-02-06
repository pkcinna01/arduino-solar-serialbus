package com.xmonit.solar.arduino.dao;

import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.data.device.Device;

import org.junit.Before;

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

}
