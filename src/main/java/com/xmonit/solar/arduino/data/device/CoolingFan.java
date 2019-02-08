package com.xmonit.solar.arduino.data.device;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CoolingFan extends PowerSwitch {

    public Double onTemp;
    public Double offTemp;
    public Integer minDurationMs;
    public Double currentTemp;

    public CoolingFan() {}

    public CoolingFan(Integer id, String type, String name) {
        super(id, type, name);
    }
    
    @Override
    public String toString() {
    	return super.toString();
    }
}
