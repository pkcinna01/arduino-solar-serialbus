package com.xmonit.solar.arduino.data.constraint;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class TransitionDurationConstraint extends Constraint {

    public Long minIntervalMs, elapsedMs;
    public Double originValue, destinationValue, lastValue;

    public TransitionDurationConstraint() {}

    public TransitionDurationConstraint(Integer id, String type, String title, Boolean bPassed) {
        super(id, type, title, bPassed);
    }
    
    @Override
    public String toString() {
    	return super.toString();
    }
}

