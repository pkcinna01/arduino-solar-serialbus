package com.xmonit.solar.arduino.data;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.xmonit.solar.arduino.json.ArduinoMapper;


@Data
public class Environment {

   public String buildDate;
   public String buildNumber;
   public String time;
   public Boolean timeSet; // was time explicitly set since last startup (yes/no)
   public Integer vcc; // arduino Vref reading for use in sensors
   public String version;

   @JsonIgnore
   public LocalDateTime getDateTime() {
      return LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-M-d H:m:s"));
   }
   
   @Override
   public String toString() {
   	try {
       	return ArduinoMapper.instance.writeValueAsString(this);
       } catch ( JsonProcessingException ex ) {
       	return super.toString();
       }
   }
}

