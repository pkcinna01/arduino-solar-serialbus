package com.xmonit.solar.arduino.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xmonit.solar.arduino.json.ArduinoMapper;

import lombok.Data;


@Data
public class Eeprom {

   public String[] commands;   // List of commands run on startup
   public String jsonFormat;   // COMPACT or PRETTY
   public String serialConfig; // 8N1, 8E1, and 8O1 (only 8 bit/1 stop bit modes supported)
   public Integer serialSpeed;
   public String version;      // EEPROM version value should match env RAM value

   @Override
   public String toString() {
   	try {
       	return ArduinoMapper.instance.writeValueAsString(this);
       } catch ( JsonProcessingException ex ) {
       	return super.toString();
       }
   }
}

