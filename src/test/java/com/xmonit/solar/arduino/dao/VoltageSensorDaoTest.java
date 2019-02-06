package com.xmonit.solar.arduino.dao;

import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.dao.sensor.VoltageSensorDao;
import com.xmonit.solar.arduino.data.sensor.VoltageSensor;
import org.junit.Before;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class VoltageSensorDaoTest extends DaoTestBase<VoltageSensorDao, VoltageSensor> {

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        dao = new VoltageSensorDao(serialBus);
    }

    @org.junit.Test
    public void doListVoltageSensorsTest() throws ArduinoException {
        VoltageSensor[] sensors = dao.listVoltageSensors();
        for (VoltageSensor s : sensors) {
            System.out.println(s);
        }
    }

    int sensorId = 10;

    @org.junit.Test
    public void getVccTest() throws ArduinoException {
        double vcc = dao.vcc(sensorId).get();
        System.out.println("vcc: " + vcc);
        VoltageSensor s = dao.get(sensorId);
        System.out.println(s.toString() );
        assertEquals(s.vcc,vcc); //todo - this value is cached in arduino but could change
    }

    @org.junit.Test
    public void setVccTest() throws ArduinoException {
        VoltageSensorDao.VccAccessor vcc = dao.vcc(sensorId);
        //vcc is read only
        try {
            vcc.set(1d);
            fail("VCC is read only");
        } catch ( ArduinoException ex ) {
            System.out.println("VCC set was rejected (read only field). " + ex.getMessage());
        }
    }

    @org.junit.Test
    public void getR1Test() throws ArduinoException {
        double r1 = dao.r1(sensorId).get();
        System.out.println("r1: " + r1);
        VoltageSensor s = dao.get(sensorId);
        System.out.println(s.toString() );
        assertEquals(s.r1,r1);
    }

    @org.junit.Test
    public void setR1Test() throws ArduinoException {

        VoltageSensor s = dao.get(sensorId);
        double oldR1Val = s.r1;
        VoltageSensorDao.R1Accessor r1 = dao.r1(sensorId);
        assertEquals(s.r1,r1.get());
        double newR1Val = 21.21;
        r1.set(newR1Val);
        s = dao.get(sensorId);
        assertEquals(s.r1,newR1Val);
        r1.set(oldR1Val);
    }


}
