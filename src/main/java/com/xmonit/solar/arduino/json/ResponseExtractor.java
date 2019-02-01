package com.xmonit.solar.arduino.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.xmonit.solar.arduino.ArduinoException;
import org.apache.commons.lang3.StringUtils;

public class ResponseExtractor {
    int arrIndex = 0;
    JsonNode arrNode;

    public ResponseExtractor(JsonNode respNode) {
        this.arrNode = respNode;
        this.arrIndex = 0;
    }

    public <DataT> DataT[] extract( Class<DataT[]> respClass) throws ArduinoException {
        return extract(respClass,null);
    }

    public <DataT> DataT extract( Class<DataT> respClass, String elementName ) throws ArduinoException {
        try {
            JsonNode dataNode = arrNode.get(arrIndex);
            int respCode = dataNode.get("respCode").asInt();
            if ( respCode != 0 ) {
                throw new ArduinoException(dataNode.get("respMsg").asText(), respCode);
            }
            JsonNode node = StringUtils.isEmpty(elementName) ? dataNode.get(0) : dataNode.get(elementName);
            DataT obj = ArduinoMapper.instance.treeToValue(node, respClass);
            return obj;
        } catch ( Exception ex ) {
            throw new ArduinoException("Failed mapping JSON response to " + respClass.getSimpleName(), ex);
        }
    }

    public ResponseExtractor nextResponse() {
        arrIndex++;
        return this;
    }
}
