package com.xmonit.solar.serialbus.data;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ArduinoResponseTest {

    public static String strJsonErrorResponse = "{\n" +
            "      \"respCode\": -22,\n" +
            "      \"respMsg\": \"Some error occured\"\n" +
            "    }\n";
    public static String strJsonResponse = "{\n" +
            "      \"fanMode\": 2, \"fanModeText\": \"AUTO\",\n" +
            "      \"shunts\": [\n" +
            "        { \"name\": \"Main 12V Battery Bank\", \"shuntAmps\": -0.01, \"shuntWatts\": -0.00 }\n" +
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

    public static ArduinoGetResponse buildExpectedResp() {

        ArduinoGetResponse expectedGetResp = new ArduinoGetResponse();

        expectedGetResp.fanMode = 2;
        expectedGetResp.fanModeText = "AUTO";
        Shunt shunt = new Shunt();
        shunt.name = "Main 12V Battery Bank";
        shunt.shuntAmps = -0.01;
        shunt.shuntWatts = -0.00;
        expectedGetResp.shunts.add(shunt);

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

        ObjectMapper mapper = new ObjectMapper();
        ArduinoGetResponse getResp = mapper.readValue(strJsonResponse, ArduinoGetResponse.class);

        System.out.println(getResp);

        ArduinoGetResponse expectedGetResp = buildExpectedResp();
        assertEquals(expectedGetResp.devices.get(0).fans.get(0), getResp.devices.get(0).fans.get(0));
        assertEquals(expectedGetResp.devices.get(0).tempSensors, getResp.devices.get(0).tempSensors);
        assertEquals(expectedGetResp, getResp);

    }
}
