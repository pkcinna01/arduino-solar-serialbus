package com.xmonit.solar.arduino.data.constraint;

import com.xmonit.solar.arduino.data.ValueSource;
import lombok.Data;
import lombok.EqualsAndHashCode;

//TODO - rename in arduino native code...  maybe NumericConstraint???

@Data
@EqualsAndHashCode(callSuper = true)
public class ValueConstraint extends Constraint {

    public ValueSource valueSource;

    public ValueConstraint() {}

    public ValueConstraint(Integer id, String type, String title, Boolean bPassed) {
        super(id, type, title, bPassed);
    }
    
    @Override
    public String toString() {
    	return super.toString();
    }
}

