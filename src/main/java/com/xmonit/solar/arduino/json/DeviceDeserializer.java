package com.xmonit.solar.arduino.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.xmonit.solar.arduino.data.device.CoolingFan;
import com.xmonit.solar.arduino.data.device.Device;
import com.xmonit.solar.arduino.data.device.PowerSwitch;

import java.io.IOException;


public class DeviceDeserializer extends StdDeserializer<Device> {

    public DeviceDeserializer() {
        this(null);
    }

    public DeviceDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Device deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        JsonNode typeNode = node.get("type");
        String type = typeNode != null ? typeNode.asText() : "Device";

        Device rtnDevice = null;

        switch (type) {
            case "InverterSwitch":
            case "BatteryBankSwitch":
            case "PowerSwitch":
                rtnDevice = ArduinoMapper.instance.treeToValue(node, PowerSwitch.class);
                break;
            case "CoolingFan":
                rtnDevice = ArduinoMapper.instance.treeToValue(node, CoolingFan.class);
                break;
            default:
                rtnDevice = new Device(node.get("id").asInt(), type, node.get("name").asText());
        }

        return rtnDevice;
    }
}
