package com.xmonit.solar.arduino.data.capability;

import com.xmonit.solar.arduino.data.DoubleObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Capability extends DoubleObject {

    public String title;
    public Integer deviceId;

    public Capability() {}

    public Capability(Integer id, String type, String title, Double value) {
        super(id,type,value);
        this.title = title;
    }
    
    @Override
    public String toString() {
    	return super.toString();
    }
}
