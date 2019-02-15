package com.xmonit.solar.arduino.dao;


import com.fasterxml.jackson.databind.JsonNode;
import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.SerialCmd;
import com.xmonit.solar.arduino.json.ResponseExtractor;
import com.xmonit.solar.arduino.serial.ArduinoSerialBus;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Dao extends SerialCmd {

    public static Map<String, List<FieldMetaData>> metaDataDictionary = new HashMap<>();

    @Data
    public static class FieldMetaData {
        public boolean readOnly;
        public String name;
        public String type;
        public String validationRegEx;
        public FieldMetaData(String name, String type, boolean readOnly, String validationRegEx) {
            this.name = name;
            this.type = type;
            this.readOnly = readOnly;
            this.validationRegEx = validationRegEx;
        }
    }
    
    protected class SingletonFieldAccessor<FieldT> extends FieldAccessor<FieldT> {
        public ObjectType objectType;

        public SingletonFieldAccessor(ObjectType type, String fieldName, Class<FieldT> fieldClass) {
            super(fieldName, fieldClass);
            this.objectType = type;
            this.resultNameArr = new Object[]{type.name().toLowerCase(), fieldName};
        }

        @Override
        public FieldT get() throws ArduinoException {
            JsonNode jsonNode = execute("verbose,get," + objectType.name());
            return new ResponseExtractor(jsonNode).extract(resultClass, resultNameArr);
        }

        @Override
        protected String getSetCmdBase() {
            return "set," + objectType.name() + "," + fieldName + ",";
        }

        @Override
        public void saveAsString(String value) throws ArduinoException {
            String cmdBase = getSetCmdBase();
            JsonNode jsonNode = execute("eeprom,replaceOrAdd,\\," + cmdBase + "*\\" + cmdBase + value);
        }
    }

    public Dao(ArduinoSerialBus sb) {
        super(sb);
    }

    public List<FieldMetaData> getAccessorDefinitions() {
        return metaDataDictionary.get(getClass().getSimpleName());
    }

}
