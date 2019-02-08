package com.xmonit.solar.arduino.dao;


import com.fasterxml.jackson.databind.JsonNode;
import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.ArduinoSerialBus;
import com.xmonit.solar.arduino.SerialCmd;
import com.xmonit.solar.arduino.json.ResponseExtractor;

public abstract class Dao extends SerialCmd {

    
    protected class ObjectFieldAccessor<FieldT> extends FieldAccessor<FieldT> {
        public int id;
        public ObjectType objectType;

        public ObjectFieldAccessor(int id, ObjectType type, String fieldName, Class<FieldT> fieldClass) {
            super(fieldName,fieldClass);
            this.id = id;
            this.objectType = type;
            this.resultNameArr = new Object[]{ type.name().toLowerCase(), 0, fieldName };
        }

        @Override
        public FieldT get() throws ArduinoException {
            JsonNode jsonNode = execute("verbose,get," + objectType.name() + "," + id);
            return new ResponseExtractor(jsonNode).extract(resultClass, resultNameArr);
        }

        @Override
        protected String getSetCmdBase() {
            return "set," + objectType.name() + "," + id + "," + fieldName + ",";
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
    
    public <DataT> DataT findByTitleLike(String wildcardFilter) throws ArduinoException { return findByTitleLike(wildcardFilter,true);}

    public abstract <DataT> DataT findByTitleLike(String wildcardFilter, boolean bVerbose) throws ArduinoException;

    public <DataT> DataT findByTitleNotLike(String wildcardFilter) throws ArduinoException { return findByTitleNotLike(wildcardFilter, true); }

    public abstract <DataT> DataT findByTitleNotLike(String wildcardFilter, boolean bVerbose) throws ArduinoException;

    public <DataT> DataT findWhereIdIn(int[] ids) throws ArduinoException { return findWhereIdIn(ids,true); }

    public abstract <DataT> DataT findWhereIdIn(int[] ids, boolean bVerbose ) throws ArduinoException;;

    public <DataT> DataT get(int id) throws ArduinoException { return get(id, true); }

    public abstract <DataT> DataT get(int id, boolean bVerbose) throws ArduinoException;

    public <DataT> DataT list() throws ArduinoException { return list(true); }

    public abstract <DataT> DataT list(boolean bVerbose) throws ArduinoException;

}
