package com.xmonit.solar.arduino.dao;


import com.fasterxml.jackson.databind.JsonNode;
import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.SerialCmd;
import com.xmonit.solar.arduino.json.ResponseExtractor;
import com.xmonit.solar.arduino.serial.ArduinoSerialBus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public abstract class Dao extends SerialCmd {


    public static class FieldMetaData {
        public boolean bReadOnly;
        public String name;
        public String type;
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

    public List<FieldMetaData> getFields() {
        List<FieldMetaData> fields = new LinkedList<>();
        for (Method method : getClass().getDeclaredMethods()) {
            Class rtnClass = method.getReturnType();
            if ( FieldAccessor.class.isAssignableFrom(rtnClass) ) {
                FieldMetaData meta = new FieldMetaData();
                meta.name = method.getName();
                meta.bReadOnly = Dao.ReadOnly.class.isAssignableFrom(rtnClass);
                try {
                    FieldAccessor fieldAccessor = null;
                    switch ( method.getParameterCount()) {
                        case 0:
                            fieldAccessor = (FieldAccessor) method.invoke(this);
                            break;
                        case 1:
                            fieldAccessor = (FieldAccessor) method.invoke(this, -1);
                            break;
                    }
                    if ( fieldAccessor != null ) {
                        meta.type = fieldAccessor.resultClass.getSimpleName();
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                fields.add(meta);
            }
        }
        return fields;
    }
}
