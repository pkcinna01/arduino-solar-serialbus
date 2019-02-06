package com.xmonit.solar.arduino.dao;

import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.dao.sensor.SensorDao;
import com.xmonit.solar.arduino.data.sensor.Sensor;
import org.junit.Before;

import static junit.framework.TestCase.assertEquals;

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

    @org.junit.Test
    public void doFindWhereIdInTest() throws ArduinoException {
        super.doFindWhereIdInTest();
    }

    @org.junit.Test
    public void getSensorPinTest() throws ArduinoException {
        int sensorId = 2;
        int sensorPin = dao.sensorPin(sensorId).get();
        System.out.println("sensorPin: " + sensorPin);
        System.out.println( dao.get(sensorId).toString() );
    }

    @org.junit.Test
    public void setSensorPinTest() throws ArduinoException {
        int sensorId = 2;
        SensorDao.PinAccessor sensorPin = dao.sensorPin(sensorId);
        int oldValue = sensorPin.get();
        int newValue = 21;
        sensorPin.set(newValue);
        int remoteValue = sensorPin.get();
        System.out.println("sensorPin: " + remoteValue);
        System.out.println( dao.get(sensorId).toString() );
        assertEquals(newValue,remoteValue);
        sensorPin.set(oldValue);
    }

}
