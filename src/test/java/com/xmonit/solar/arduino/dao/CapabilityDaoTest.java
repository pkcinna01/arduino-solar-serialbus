package com.xmonit.solar.arduino.dao;

import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.dao.capability.CapabilityDao;
import com.xmonit.solar.arduino.data.constraint.Constraint;
import org.junit.Before;

public class CapabilityDaoTest extends DomainDaoTestBase<CapabilityDao, Constraint> {

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        dao = new CapabilityDao(serialBus);
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
