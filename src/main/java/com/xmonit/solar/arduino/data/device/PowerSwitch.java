package com.xmonit.solar.arduino.data.device;

import com.xmonit.solar.arduino.data.LogicLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PowerSwitch extends Device {

    public Integer relayPin;
    public Boolean on;
    public LogicLevel relayOnSignal;

    public PowerSwitch() {}

    public PowerSwitch(Integer id, String type, String name) {
        super(id, type, name); 
    }
    
    @Override
    public String toString() {
    	return super.toString();
    }
}
