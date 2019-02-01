package com.xmonit.solar.arduino;


import com.fasterxml.jackson.databind.JsonNode;
import com.xmonit.solar.arduino.data.Eeprom;
import com.xmonit.solar.arduino.data.Environment;
import com.xmonit.solar.arduino.json.ArduinoMapper;
import com.xmonit.solar.arduino.json.ResponseExtractor;

public class SerialCmd {

    protected boolean bVerbose;

    protected ArduinoSerialBus serialBus;

    public SerialCmd(ArduinoSerialBus sb) {
        this.serialBus = sb;
    }

    protected String addPrefix(String cmd) {
        if (bVerbose) {
            if (!cmd.toLowerCase().startsWith("verbose,")) {
                cmd = "verbose," + cmd;
            }
        }
        return cmd;
    }
    
    public <DataT> DataT doCommand(String cmd, Class<DataT> resultClass) throws ArduinoException {
        JsonNode jsonNode = execute(cmd);
        return new ResponseExtractor(jsonNode).extract(resultClass, null);
    }

    public <DataT> DataT doCommand(String cmd, String elementName, Class<DataT> resultClass) throws ArduinoException {
        JsonNode jsonNode = execute(cmd);
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
        rawExecute("reset");
    }

    public JsonNode execute(String strCmd) throws ArduinoException {

        String strResp = rawExecute(strCmd);
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
            sb.append(addPrefix(cmd));
        }
        return execute(sb.toString());
    }

    public boolean getVerbose() {
        return bVerbose;
    }

    public String rawExecute(String strCmd) throws ArduinoException {
    	return serialBus.execute(addPrefix(strCmd));
    }

    public void setVerbose(boolean bVerbose) {
        this.bVerbose = bVerbose;
    }
}
