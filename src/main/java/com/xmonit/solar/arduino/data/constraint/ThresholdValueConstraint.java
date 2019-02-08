package com.xmonit.solar.arduino.data.constraint;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ThresholdValueConstraint extends ValueConstraint {

    public Double threshold;

    public ThresholdValueConstraint() {}

    public ThresholdValueConstraint(Integer id, String type, String title, Boolean bPassed) {
        super(id, type, title, bPassed);
    }
    
    @Override
    public String toString() {
    	return super.toString();
    }
}

