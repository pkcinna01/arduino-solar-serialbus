package com.xmonit.solar.arduino.dao;

import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.data.sensor.Sensor;
import org.junit.Before;

public class SensorDaoTest extends DaoTestBase<SensorDao, Sensor> {

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        dao = new SensorDao(serialBus);
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

}
