package com.xmonit.solar.serialbus.data;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;


public class ArduinoResponseTest {

    public static String strJsonErrorResponse = "{\n" +
            "      \"respCode\": -22,\n" +
            "      \"respMsg\": \"Some error occured\"\n" +
            "    }\n";
    public static String strJsonResponse = "{\n" +
            "      \"fanMode\": 2, \"fanModeText\": \"AUTO\",\n" +
            "      \"powerMeters\": [\n" +
            "        { \"name\": \"Main 12V Battery Bank\", \"watts\": 100," +
            "          \"current\": { \"amps\": 1.0, \"ratedAmps\": 200, \"ratedMilliVolts\": 75 },\n" +
            "          \"voltage\": { \"volts\": 12.7, \"analogPin\": 3, " +
            "          \"assignedVcc\": 4.88, \"assignedR1\": 1010000, \"assignedR2\": 105000 }\n}" +
            "      ],\n" +
            "      \"devices\": [\n" +
            "      { \"name\": \"Bench\",\n" +
            "        \"tempSensors\": [\n" +
            "          { \"name\": \"DHT\", \"temp\": 61.16, \"humidity\": 90.80, \"heatIndex\": 61.24 }\n" +
            "        ],\n" +
            "        \"fans\": [\n" +
            "          { \"name\": \"Exhaust\", \"relayPin\": 3, \"onTemp\": 90.00, \"offTemp\": 85.00, \"relayValue\": 0, \"on\": false }\n" +
            "        ]\n" +
            "      },\n" +
            "      { \"name\": \"Controllers\",\n" +
            "        \"tempSensors\": [\n" +
            "          { \"name\": \"Thermistor1\", \"temp\": 62.74 },\n" +
            "          { \"name\": \"Thermistor2\", \"temp\": 63.49 }\n" +
            "        ],\n" +
            "        \"fans\": [\n" +
            "          { \"name\": \"Oscillating\", \"relayPin\": 5, \"onTemp\": 115.00, \"offTemp\": 95.00, \"relayValue\": 0, \"on\": false }\n" +
            "        ]\n" +
            "      }],\n" +
            "      \"respCode\": 0,\n" +
            "      \"respMsg\": \"OK\"\n" +
            "    }\n";

    public static ObjectMapper mapper = new ObjectMapper();

    public static ArduinoGetResponse buildExpectedResp() {

        ArduinoGetResponse expectedGetResp = new ArduinoGetResponse();

        expectedGetResp.fanMode = 2;
        expectedGetResp.fanModeText = "AUTO";
        PowerMeter power = new PowerMeter();
        power.name = "Main 12V Battery Bank";
        power.watts = 100.0;
        expectedGetResp.powerMeters.add(power);
        Shunt shunt = new Shunt();
        shunt.amps = 1.0;
        shunt.ratedAmps = 200.0;
        shunt.ratedMilliVolts = 75.0;
        power.current = shunt;
        Voltmeter voltage = new Voltmeter();
        voltage.volts = 12.7;
        voltage.analogPin = 3;
        voltage.assignedVcc = 4.88;
        voltage.assignedR1 = 1010000.0;
        voltage.assignedR2 = 105000.0;
        power.voltage = voltage;

        Device device = new Device();
        device.name = "Bench";
        TempSensor tempSensor = new TempSensor();
        tempSensor.name = "DHT";
        tempSensor.temp = 61.16;
        tempSensor.humidity = 90.80;
        tempSensor.heatIndex = 61.24;
        device.tempSensors.add(tempSensor);
        Fan fan = new Fan();
        fan.name = "Exhaust";
        fan.relayPin = 3;
        fan.onTemp = 90.0;
        fan.offTemp = 85.0;
        fan.relayValue = 0;
        fan.on = false;
        device.fans.add(fan);
        expectedGetResp.devices.add(device);

        device = new Device();
        device.name = "Controllers";
        tempSensor = new TempSensor();
        tempSensor.name = "Thermistor1";
        tempSensor.temp = 62.74;
        device.tempSensors.add(tempSensor);
        tempSensor = new TempSensor();
        tempSensor.name = "Thermistor2";
        tempSensor.temp = 63.49;
        device.tempSensors.add(tempSensor);
        fan = new Fan();
        fan.name = "Oscillating";
        fan.relayPin = 5;
        fan.onTemp = 115.0;
        fan.offTemp = 95.0;
        fan.relayValue = 0;
        fan.on = false;
        device.fans.add(fan);
        expectedGetResp.devices.add(device);
        expectedGetResp.respCode = 0;
        expectedGetResp.respMsg = "OK";

        return expectedGetResp;
    }


    @Test
    public void jsonObjectMappingTest() throws Exception {


        ArduinoGetResponse getResp = mapper.readValue(strJsonResponse, ArduinoGetResponse.class);

        ArduinoGetResponse expectedGetResp = buildExpectedResp();
        assertEquals(expectedGetResp.devices.get(0).fans.get(0), getResp.devices.get(0).fans.get(0));
        assertEquals(expectedGetResp.devices.get(0).tempSensors, getResp.devices.get(0).tempSensors);
        assertEquals(expectedGetResp, getResp);

    }

    @Test
    public void deepCopyTest() throws Exception {
        ArduinoGetResponse srcObj = buildExpectedResp();
        ArduinoGetResponse destObj = srcObj.deepCopy();
        assertNotSame(srcObj,destObj);
        assertEquals(srcObj,destObj);
        assertNotSame(srcObj.devices,destObj.devices);
        assertEquals(srcObj.devices,destObj.devices);
        destObj.devices.get(1).fans.get(0).offTemp = 10.0;
        assertNotEquals(srcObj,destObj);

    }

    @Test
    public void validMirrorCopyTest() throws Exception {
        ArduinoGetResponse srcObj = mapper.readValue(strJsonResponse,ArduinoGetResponse.class);
        ArduinoGetResponse destObj = srcObj.deepCopy();
        assertNotSame(srcObj,destObj);
        assertEquals(srcObj,destObj);
        destObj.fanMode = 99;
        destObj.devices.get(1).tempSensors.get(0).temp = 999.0;
        assertNotEquals(srcObj,destObj);
        destObj.copy(srcObj);
        assertEquals(srcObj,destObj);
    }

    @Test
    public void invalidMirrorCopyTest() throws Exception {
        ArduinoGetResponse srcObj = mapper.readValue(strJsonResponse,ArduinoGetResponse.class);
        ArduinoGetResponse destObj = srcObj.deepCopy();
        destObj.fanMode = 99;
        destObj.devices.remove(1);
        assertNotEquals(srcObj,destObj);
        try {
            destObj.copy(srcObj);
            fail("Should not be able to copy objects with different size device arrays");
        } catch (Exception ex) {
            assertNotEquals(srcObj,destObj);
        }

        try {
            destObj = srcObj.deepCopy();
            destObj.powerMeters.remove(0);
            destObj.copy(srcObj);
            fail("Should not be able to copy objects with different size power meter arrays");
        } catch (Exception ex) {
            assertNotEquals(srcObj,destObj);
        }

        try {
            destObj = srcObj.deepCopy();
            destObj.devices.get(1).tempSensors.remove(0);
            srcObj.copy(destObj);
            fail("Should not be able to copy objects with different size temp sensor arrays");
        } catch (Exception ex) {
            assertNotEquals(srcObj,destObj);
        }
    }
}
