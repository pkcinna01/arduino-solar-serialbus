package com.xmonit.solar.arduino.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.xmonit.solar.arduino.data.constraint.RemoteExpiredDelayOp;
import com.xmonit.solar.arduino.data.constraint.RemoteExpiredOp;

import java.io.IOException;

public class RemoteExpiredDeserializer extends StdDeserializer<RemoteExpiredOp> {

    public RemoteExpiredDeserializer() {
        this(null);
    }

    public RemoteExpiredDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public RemoteExpiredOp deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        JsonNode typeNode = node.get("type");
        String type = typeNode != null ? typeNode.asText() : "RemoteExpiredOp";

        RemoteExpiredOp rtnObj = null;

        switch (type) {
            case "delay":
                rtnObj = ArduinoMapper.instance.treeToValue(node, RemoteExpiredDelayOp.class);
                break;
            case "auto":
            default:
                rtnObj = new RemoteExpiredOp(type, node.get("expired").asBoolean());
        }

        return rtnObj;
    }
}
