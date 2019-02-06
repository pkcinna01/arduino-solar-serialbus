package com.xmonit.solar.arduino.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.xmonit.solar.arduino.ArduinoException;

public class ResponseExtractor {

    public static void validateReturnCode(JsonNode node) throws ArduinoException {
        validateReturnCode(node,0);
    }

    public static void validateReturnCode(JsonNode node, int index) throws ArduinoException {
        node = node.get(index);
        int respCode = node.get("respCode").asInt();
        if ( respCode != 0 ) {
            throw new ArduinoException(node.get("respMsg").asText(), respCode);
        }
    }

    int arrIndex = 0;

    JsonNode arrNode;

    public ResponseExtractor(JsonNode respNode) {
        this.arrNode = respNode;
        this.arrIndex = 0;
    }

    public <DataT> DataT[] extract( Class<DataT[]> respClass) throws ArduinoException {
        String nullStr = null;
        return extract(respClass,nullStr);
    }

    public <DataT> DataT extract(Class<DataT> respClass, Object[] elementNames) throws ArduinoException {
        try {
            JsonNode dataNode = arrNode.get(arrIndex);
            int respCode = dataNode.get("respCode").asInt();
            if ( respCode != 0 ) {
                throw new ArduinoException(dataNode.get("respMsg").asText(), respCode);
            }
            JsonNode node = dataNode;
            for (int i = 0; i < elementNames.length; i++) {
                Object pathElement = elementNames[i];
                if ( pathElement instanceof Integer ) {
                    node = node.get((Integer)pathElement);
                } else {
                    node = node.get(pathElement.toString());
                }
            }
            DataT obj = ArduinoMapper.instance.treeToValue(node, respClass);
            return obj;
        } catch ( JsonProcessingException ex ) {
            throw new ArduinoException("Failed mapping JSON response to " + respClass.getSimpleName(), ex);
        }
    }

    public <DataT> DataT extract( Class<DataT> respClass, String elementName ) throws ArduinoException {
        return extract(respClass, new String[]{elementName});
    }

    public ResponseExtractor nextResponse() {
        arrIndex++;
        return this;
    }
}
