package com.xmonit.solar.arduino;


import com.fasterxml.jackson.databind.JsonNode;
import com.xmonit.solar.arduino.data.Eeprom;
import com.xmonit.solar.arduino.data.Environment;
import com.xmonit.solar.arduino.data.JsonFormat;
import com.xmonit.solar.arduino.data.Time;
import com.xmonit.solar.arduino.json.ArduinoMapper;
import com.xmonit.solar.arduino.json.ResponseExtractor;
import com.xmonit.solar.arduino.serial.ArduinoSerialBus;

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

        public FieldAccessor<ResultT> save(ResultT value) throws ArduinoException {
            if ( value instanceof String ) {
                saveAsString(stringArg((String)value));
            } else {
                saveAsString("" + value);
            }
            return this;
        }

        public FieldAccessor<ResultT> saveIf(boolean bDoSave, ResultT value) throws ArduinoException {
            if ( bDoSave ) {
                return save(value);
            }
            return this;
        }

        protected void saveAsString(String value) throws ArduinoException {
            String cmdBase = getSetCmdBase();
            JsonNode jsonNode = execute("eeprom," + cmdBase + value);
            ResponseExtractor.validateReturnCode(jsonNode);
        }

        public FieldAccessor<ResultT> set(ResultT value) throws ArduinoException {
            if ( value instanceof String ) {
                setAsString(stringArg((String)value));
            } else {
                setAsString("" + value);
            }
            return this;
        }

        protected void setAsString(String value) throws ArduinoException {
            JsonNode jsonNode = execute(getSetCmdBase() + value);
            ResponseExtractor.validateReturnCode(jsonNode);
        }
    }

    public enum ObjectType {
        CAPABILITY, CONSTRAINT, DEVICE, EEPROM, ENV, SENSOR;
    }

    public interface ReadOnly {

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
            sb.append(stringArg(cmd));
        }
        return execute(sb.toString());
    }

    public Eeprom getEeprom() throws ArduinoException {
        JsonNode jsonNode = execute("get,eeprom");
        return new ResponseExtractor(jsonNode).extract(Eeprom.class, "eeprom");
    }

    public Environment getEnvironment() throws ArduinoException {
        JsonNode jsonNode = execute("get,env");
        Environment env = new ResponseExtractor(jsonNode).extract(Environment.class, "env");
        env.setTty( serialBus.getPortName() );
        env.name = serialBus.name;
        env.id = serialBus.id;
        return env;
    }

    public Boolean isPaused() throws ArduinoException {
        JsonNode jsonNode = execute("get,isPaused");
        return new ResponseExtractor(jsonNode).extract(Boolean.class, "isPaused");
    }

    public FieldAccessor<JsonFormat> jsonFormat() {
        return new FieldAccessor<JsonFormat>("jsonFormat", JsonFormat.class);
    }

    // NOTE: Only use this if you really want to pause indefinitely (requires explicit "resume")
    public void pauseConstraintProcessing() throws ArduinoException {
        ResponseExtractor.validateReturnCode(execute("pause"));
    }

    public void pauseConstraintProcessing(int seconds) throws ArduinoException {
        ResponseExtractor.validateReturnCode(execute("pause," + seconds ));
    }

    public void resumeConstraintProcessing() throws ArduinoException {
        ResponseExtractor.validateReturnCode(execute("resume"));
    }

    public TimeAccessor time() {
        return new TimeAccessor();
    }

    protected String stringArg(String arg) throws ArduinoException {
        return stringArg(arg, ',', ';');
    }

    protected String stringArg(String arg, char ...restrictedChars) throws ArduinoException {
        if ( arg == null ) {
            throw new ArduinoException("String argument to Arduino serialbus command null", Error.NullArgument);
        }
        if ( arg.indexOf(';') >= 0 ) {
            throw new ArduinoException("String argument to Arduino serialbus command contains a reserved character: ;", Error.InvalidArgument);
        }
        for( char restrictedChar : restrictedChars ) {
            if ( arg.indexOf(restrictedChar) >= 0 ) {
                throw new ArduinoException("String argument to Arduino serialbus command contains a reserved character: " + restrictedChar, Error.InvalidArgument);
            }
        }
        return arg;
    }


}
