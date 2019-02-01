package com.xmonit.solar.arduino.data.device;

import com.xmonit.solar.arduino.data.DomainObject;
import com.xmonit.solar.arduino.data.capability.Capability;
import com.xmonit.solar.arduino.data.constraint.Constraint;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Device extends DomainObject {

    public String name;
    public List<Capability> capabilities = new ArrayList<>();
    public Constraint constraint;

    public Device() {}

    public Device(Integer id, String type, String name) {
        super(id,type);
        this.name = name;
    }

    @Override
    public String getTitle() {
        return name;
    }
    
    @Override
    public String toString() {
    	return super.toString();
    }

}
