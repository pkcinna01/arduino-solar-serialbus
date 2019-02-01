package com.xmonit.solar.arduino.data.constraint;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class BooleanConstraint extends Constraint {

    public BooleanConstraint() {}

    public BooleanConstraint(Integer id, String type, String title, String state) {
        super(id, type, title,state);
    }
    
    @Override
    public String toString() {
    	return super.toString();
    }
}

