package com.xmonit.solar.arduino.dao;

import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.SerialCmdTest;
import com.xmonit.solar.arduino.data.Eeprom;
import com.xmonit.solar.arduino.data.JsonFormat;
import org.junit.After;
import org.junit.Before;

import static junit.framework.TestCase.assertEquals;

public class EepromDaoTest extends SerialCmdTest {

    EepromDao dao;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        dao = new EepromDao(serialBus);
    }

    @After
    @Override
    public void tearDown() throws Exception {
        serialBus.close();
    }

    @org.junit.Test
    public void jsonFormatAccessorTest() throws ArduinoException {
        Eeprom orig = dao.get();
        System.out.println("Original json format: " + orig.jsonFormat.name());
        EepromDao.JsonFormatAccessor jsonFormat = dao.jsonFormat();
        assertEquals(orig.jsonFormat,jsonFormat.get());
        JsonFormat newFormat = orig.jsonFormat == JsonFormat.PRETTY ? JsonFormat.COMPACT : JsonFormat.PRETTY;
        jsonFormat.set(newFormat);
        Eeprom eeprom = dao.get();
        System.out.println(eeprom.toString() );
        assertEquals(newFormat,eeprom.jsonFormat);
        jsonFormat.set(orig.jsonFormat);
        System.out.println("Final json format: " + jsonFormat.get());
    }

}
