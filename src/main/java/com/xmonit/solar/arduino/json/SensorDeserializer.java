package com.xmonit.solar.arduino.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.xmonit.solar.arduino.data.Status;
import com.xmonit.solar.arduino.data.sensor.*;

import java.io.IOException;


public class SensorDeserializer extends StdDeserializer<Sensor> {

    public SensorDeserializer() {
        this(null);
    }

    public SensorDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Sensor deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        JsonNode typeNode = node.get("type");
        String type = typeNode != null ? typeNode.asText() : "Sensor";
        JsonNode statusNode = node.get("status");
        Status status = null;
        if ( statusNode != null ) {
            status = new Status(statusNode.get("code").asInt(), statusNode.get("msg").asText());
        }

        Sensor rtnSensor = null;

        switch (type) {
            case "ThermistorSensor":
                rtnSensor = ArduinoMapper.instance.treeToValue(node, ThermistorSensor.class);
                break;
            case "PowerSensor":
                rtnSensor = ArduinoMapper.instance.treeToValue(node, PowerSensor.class);
                break;
            case "CompositeSensor":
                rtnSensor = ArduinoMapper.instance.treeToValue(node, CompositeSensor.class);
                break;
            case "VoltageSensor":
                rtnSensor = ArduinoMapper.instance.treeToValue(node, VoltageSensor.class);
                break;
            case "CurrentSensor":
                rtnSensor = ArduinoMapper.instance.treeToValue(node, CurrentSensor.class);
                break;
            case "ToggleSensor":
                rtnSensor = ArduinoMapper.instance.treeToValue(node, ToggleSensor.class);
                break;
            case "LightSensor":
            case "RelaySensor":
            case "DhtTempSensor":
            case "DhtHumiditySensor":
                rtnSensor = ArduinoMapper.instance.treeToValue(node, ArduinoSensor.class);
                break;
            default:
                rtnSensor = new Sensor(
                        node.get("id").asInt(),
                        type,
                        node.get("name").asText(),
                        node.get("value").asDouble(),
                        status
                );
        }

        rtnSensor.receivedTimeMs = System.currentTimeMillis();

        return rtnSensor;
    }
}
