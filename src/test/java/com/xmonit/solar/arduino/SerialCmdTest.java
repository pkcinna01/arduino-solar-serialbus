package com.xmonit.solar.arduino;

import com.xmonit.solar.arduino.data.Eeprom;
import com.xmonit.solar.arduino.data.Environment;
import com.xmonit.solar.arduino.data.device.Device;
import com.xmonit.solar.arduino.data.sensor.Sensor;
import com.xmonit.solar.arduino.json.ResponseExtractor;
import org.junit.Test;

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
    public void doCommand() {
    }

    @Test
    public void doCommand1() {
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
}