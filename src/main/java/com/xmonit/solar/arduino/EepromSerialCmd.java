package com.xmonit.solar.arduino;


import com.fasterxml.jackson.databind.JsonNode;
import com.xmonit.solar.arduino.data.Eeprom;
import com.xmonit.solar.arduino.json.ResponseExtractor;

public class EepromSerialCmd extends SerialCmd {


    public EepromSerialCmd(ArduinoSerialBus serialBus) {
        super(serialBus);
    }

    public void doSetJsonFormatCompact( boolean bCompact ) throws ArduinoException {
        rawExecute("eeprom,set,json_format," + (bCompact ? "COMPACT" : "PRETTY"));
    }

    public boolean doIsJsonFormatCompact() throws ArduinoException {
        JsonNode jsonNode = execute("eeprom,get,json_format");
        String jsonFormat = new ResponseExtractor(jsonNode).extract(Eeprom.class, "eeprom").jsonFormat;
        return "COMPACT".equalsIgnoreCase(jsonFormat);
    }
}
