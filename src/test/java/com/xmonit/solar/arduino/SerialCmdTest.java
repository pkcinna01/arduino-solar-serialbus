package com.xmonit.solar.arduino;

import com.xmonit.solar.arduino.data.Eeprom;
import com.xmonit.solar.arduino.data.Environment;
import com.xmonit.solar.arduino.data.Time;
import com.xmonit.solar.arduino.data.device.Device;
import com.xmonit.solar.arduino.data.sensor.Sensor;
import com.xmonit.solar.arduino.json.ResponseExtractor;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class SerialCmdTest extends SerialBusTestSetup {

    @Test
    public void doCommands() throws ArduinoException {
        SerialCmd cmd = new SerialCmd(serialBus);
        String[] cmds = { "get,sensors", "get,devices" };

        ResponseExtractor respExtractor = cmd.doCommands(cmds);

        Sensor[] sensors = respExtractor.extract(Sensor[].class,"sensors");
        for( Sensor s : sensors ) {
            System.out.println(s);
        }
        Device[] devices = respExtractor.nextResponse().extract(Device[].class,"devices");
        for( Device d : devices) {
            System.out.println(d);
        }
    }


    @Test
    public void doGetEnvironment() throws ArduinoException {
        Environment env = new SerialCmd(serialBus).doGetEnvironment();
        System.out.println(env);
    }

    @Test
    public void doGetEeprom() throws ArduinoException {
        Eeprom eeprom = new SerialCmd(serialBus).doGetEeprom();
        System.out.println(eeprom);
    }

    @Test
    public void doGetTime() throws ArduinoException {
        Time time = new SerialCmd(serialBus).time().get();
        System.out.println(time);
    }

    @Test
    public void doSetTime() throws ArduinoException {
        LocalDateTime when = LocalDateTime.now();
        SerialCmd.TimeAccessor time = new SerialCmd(serialBus).time();
        time.set(when);
        assertTrue(Duration.between(when,time.get().toLocalDateTime()).toMinutes() < 1 );
    }

    @Test
    public void doGetJsonFormat() throws ArduinoException {
        String strFmt = new SerialCmd(serialBus).jsonFormat().get();
        System.out.println(strFmt);
    }

    @Test
    public void doSetJsonFormat() throws ArduinoException {
        SerialCmd.FieldAccessor<String> jsonFmt = new SerialCmd(serialBus).jsonFormat();
        String oldFormat = jsonFmt.get();
        String newFormat = "pretty".equalsIgnoreCase(oldFormat) ? "compact" : "pretty";
        jsonFmt.set(newFormat);
        assertEquals(newFormat,jsonFmt.get().toLowerCase());
        jsonFmt.set(oldFormat);
    }
}