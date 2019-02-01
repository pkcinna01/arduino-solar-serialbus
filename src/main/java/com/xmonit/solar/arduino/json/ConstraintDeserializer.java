package com.xmonit.solar.arduino.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.xmonit.solar.arduino.data.constraint.*;

import java.io.IOException;


public class ConstraintDeserializer extends StdDeserializer<Constraint> {

    public ConstraintDeserializer() {
        this(null);
    }

    public ConstraintDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Constraint deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        JsonNode typeNode = node.get("type");
        String type = typeNode != null ? typeNode.asText() : "Constraint";
        
        Constraint rtnConstraint = null;

        switch (type) {
            case "Boolean":
                rtnConstraint = ArduinoMapper.instance.treeToValue(node, BooleanConstraint.class);
                break;
            case "And":
            case "Or":
            case "Composite":
                rtnConstraint = ArduinoMapper.instance.treeToValue(node, CompositeConstraint.class);
                break;
            case "Not":
            case "Nested":
                rtnConstraint = ArduinoMapper.instance.treeToValue(node, NestedConstraint.class);
                break;
            case "AtMost":
            case "AtLeast":
            case "ThresholdValue":
                rtnConstraint = ArduinoMapper.instance.treeToValue(node, ThresholdValueConstraint.class);
                break;
            case "TimeRange":
                rtnConstraint = ArduinoMapper.instance.treeToValue(node, TimeRangeConstraint.class);
                break;
            case "Range":
                rtnConstraint = ArduinoMapper.instance.treeToValue(node, RangeConstraint.class);
                break;
            case "TransitionDuration":
                rtnConstraint = ArduinoMapper.instance.treeToValue(node, TransitionDurationConstraint.class);
                break;
            default:
                rtnConstraint = new Constraint(
                        node.get("id").asInt(),
                        type,
                        node.get("title").asText(),
                        node.get("state").asText()
                );
        }

        return rtnConstraint;
    }
}
