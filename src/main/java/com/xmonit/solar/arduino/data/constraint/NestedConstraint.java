package com.xmonit.solar.arduino.data.constraint;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class NestedConstraint extends Constraint {

    public NestedConstraint(){
    }

    public NestedConstraint(Integer id, String type, String title, Boolean bPassed) {
        super(id, type, title, bPassed);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
