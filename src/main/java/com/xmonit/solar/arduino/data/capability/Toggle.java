package com.xmonit.solar.arduino.data.capability;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class Toggle extends Capability {

    public Toggle() {}

    public Toggle(Integer id, String type, String title, Double value) {
        super(id,type,title,value);
    }
    
    @Override
    public String toString() {
    	return super.toString();
    }
}
