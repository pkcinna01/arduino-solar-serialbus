package com.xmonit.solar.arduino.dao;

import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.dao.sensor.CurrentSensorDao;
import com.xmonit.solar.arduino.data.sensor.CurrentSensor;
import org.junit.Before;

import static junit.framework.TestCase.assertEquals;

public class CurrentSensorDaoTest extends DaoTestBase<CurrentSensorDao, CurrentSensor> {

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        dao = new CurrentSensorDao(serialBus);
    }

    @org.junit.Test
    public void doListCurrentSensorsTest() throws ArduinoException {
        CurrentSensor[] sensors = dao.listCurrentSensors();
        for (CurrentSensor s : sensors) {
            System.out.println(s);
        }
    }

    int sensorId = 13;

    @org.junit.Test
    public void getChannelTest() throws ArduinoException {
        CurrentSensor.Channel channel = dao.channel(sensorId).get();
        System.out.println("channel: " + channel.name());
        CurrentSensor s = dao.get(sensorId);
        System.out.println(s.toString() );
        assertEquals(s.channel,channel);
    }

    @org.junit.Test
    public void getGainTest() throws ArduinoException {
        CurrentSensor.Gain gain = dao.gain(sensorId).get();
        System.out.println("gain: " + gain.name());
        CurrentSensor s = dao.get(sensorId);
        System.out.println(s.toString() );
        assertEquals(s.gain,gain);
    }

   /*
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
*/

}
