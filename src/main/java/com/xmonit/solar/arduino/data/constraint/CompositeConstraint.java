package com.xmonit.solar.arduino.data.constraint;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CompositeConstraint extends Constraint {

    public String joinName;
    public Boolean shortCircuit; // for logic evaluation (AND, OR, etc...)

    public CompositeConstraint(){
    }

    public CompositeConstraint(Integer id, String type, String title, String state) {
        super(id, type, title,state);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
