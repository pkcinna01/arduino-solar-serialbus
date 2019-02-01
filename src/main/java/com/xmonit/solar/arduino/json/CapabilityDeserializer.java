package com.xmonit.solar.arduino.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.xmonit.solar.arduino.data.capability.Capability;
import com.xmonit.solar.arduino.data.capability.Toggle;

import java.io.IOException;


public class CapabilityDeserializer extends StdDeserializer<Capability> {

    public CapabilityDeserializer() {
        this(null);
    }

    public CapabilityDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Capability deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        JsonNode typeNode = node.get("type");
        String type = typeNode != null ? typeNode.asText() : "Capability";

        Capability rtnCapability = null;

        switch (type) {
            case "Toggle":
                rtnCapability = ArduinoMapper.instance.treeToValue(node, Toggle.class);
                break;
            default:
                rtnCapability = new Capability(
                    node.get("id").asInt(),
                    type,
                    node.get("title").asText(),
                    node.get("value").asDouble()
                );
        }

        return rtnCapability;
    }
}
