package com.xmonit.solar.arduino;


import com.fasterxml.jackson.databind.JsonNode;
import com.xmonit.solar.arduino.data.Eeprom;
import com.xmonit.solar.arduino.data.Environment;
import com.xmonit.solar.arduino.data.Time;
import com.xmonit.solar.arduino.json.ArduinoMapper;
import com.xmonit.solar.arduino.json.ResponseExtractor;

import java.time.LocalDateTime;

public class SerialCmd {

    public class FieldAccessor<ResultT> {

        public String fieldName;
        public Class<ResultT> resultClass;
        public Object[] resultNameArr;

        public FieldAccessor(String fieldName, Class<ResultT> resultClass) {
            this(fieldName,new String[]{fieldName},resultClass);
        }

        public FieldAccessor(String fieldName, Object[] resultNameArr, Class<ResultT> resultClass) {
            this.fieldName = fieldName;
            this.resultNameArr = resultNameArr;
            this.resultClass = resultClass;
        }

        public ResultT get() throws ArduinoException {
            return get(resultClass,fieldName,resultNameArr);
        }

        public <ExplicitResultT> ExplicitResultT get(Class<ExplicitResultT> resultClass, String fieldName, Object[] resultNameArr) throws ArduinoException {
            JsonNode jsonNode = execute("get," + fieldName );
            return new ResponseExtractor(jsonNode).extract(resultClass, resultNameArr);
        }

        protected String getSetCmdBase() {
            return "set," + fieldName + ",";
        }

        public void save(ResultT value) throws ArduinoException {
            saveAsString(""+value);
        }

        public void saveAsString(String value) throws ArduinoException {
            String cmdBase = getSetCmdBase();
            JsonNode jsonNode = execute("eeprom," + cmdBase + value);
            ResponseExtractor.validateReturnCode(jsonNode);
        }

        public void set(ResultT value) throws ArduinoException {
            setAsString( "" + value);
        }

        public void setAsString(String value) throws ArduinoException {
            JsonNode jsonNode = execute(getSetCmdBase() + value);
            ResponseExtractor.validateReturnCode(jsonNode);
        }
    }

    public enum ObjectType {
        CAPABILITY, CONSTRAINT, DEVICE, SENSOR;
    }

    public class TimeAccessor extends FieldAccessor<Time> {

        public TimeAccessor() {
            super("time", Time.class);
        }

        public void set() throws ArduinoException {
            set( LocalDateTime.now() );
        }

        public void set(LocalDateTime dateTime) throws ArduinoException {
            setAsString(toString(dateTime));
        }

        protected String toString(LocalDateTime dateTime) {
            return new StringBuilder().append(dateTime.getYear()).append(',').append(dateTime.getMonthValue())
                    .append(',').append(dateTime.getDayOfMonth()).append(',').append(dateTime.getHour())
                    .append(',').append(dateTime.getMinute()).append(',').append(dateTime.getSecond()).toString();
        }
    }

    protected ArduinoSerialBus serialBus;

    public SerialCmd(ArduinoSerialBus sb) {
        this.serialBus = sb;
    }

    public <DataT> DataT doCommand(String cmd, String elementName, Class<DataT> resultClass, boolean bVerbose) throws ArduinoException {
        JsonNode jsonNode = execute( (bVerbose?"verbose,":"") + cmd);
        return new ResponseExtractor(jsonNode).extract(resultClass, elementName);
    }

    public ResponseExtractor doCommands(String[] cmds) throws ArduinoException {
        JsonNode jsonNode = execute(cmds);
        return new ResponseExtractor(jsonNode);
    }

    public Eeprom doGetEeprom() throws ArduinoException {
        JsonNode jsonNode = execute("get,eeprom");
        return new ResponseExtractor(jsonNode).extract(Eeprom.class, "eeprom");
    }

    public Environment doGetEnvironment() throws ArduinoException {
        JsonNode jsonNode = execute("get,env");
        return new ResponseExtractor(jsonNode).extract(Environment.class, "env");
    }

    public void doReset() throws ArduinoException {
        ResponseExtractor.validateReturnCode(execute("reset"));
    }

    public JsonNode execute(String strCmd) throws ArduinoException {

        String strResp = serialBus.execute(strCmd);
        try {
            return ArduinoMapper.instance.readTree(strResp);
        } catch (Exception ex) {
            throw new ArduinoException("Failed parsing JSON response from Arduino", ex);
        }
    }

    protected JsonNode execute(String[] commands) throws ArduinoException {
        StringBuilder sb = new StringBuilder();
        for (String cmd : commands) {
            if (sb.length() != 0) {
                sb.append(";");
            }
            sb.append(cmd);
        }
        return execute(sb.toString());
    }

    public FieldAccessor<String> jsonFormat() {
        return new FieldAccessor<String>("jsonFormat", String.class);
    }

    public TimeAccessor time() {
        return new TimeAccessor();
    }

}
