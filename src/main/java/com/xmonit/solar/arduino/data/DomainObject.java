package com.xmonit.solar.arduino.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xmonit.solar.arduino.json.ArduinoMapper;

import lombok.Data;

@Data
public abstract class DomainObject {

    public Integer id;
    public String type;

    public DomainObject() {}

    public DomainObject(Integer id, String type) {
        this.id = id;
        this.type = type;
    }

    abstract public String getTitle(); // some arduino things have a title and some have a name so use this to normalize both
    
    @Override
    public String toString() {
    	try {
        	return ArduinoMapper.instance.writeValueAsString(this);
        } catch ( JsonProcessingException ex ) {
        	return super.toString();
        }
    }
}
