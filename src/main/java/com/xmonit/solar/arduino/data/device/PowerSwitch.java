package com.xmonit.solar.arduino.data.device;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PowerSwitch extends Device {

    public Integer relayPin;

    public PowerSwitch() {}

    public PowerSwitch(Integer id, String type, String name) {
        super(id, type, name); 
    }
    
    @Override
    public String toString() {
    	return super.toString();
    }
}
