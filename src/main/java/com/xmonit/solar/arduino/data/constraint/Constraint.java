package com.xmonit.solar.arduino.data.constraint;

import com.xmonit.solar.arduino.data.DomainObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Constraint extends DomainObject {

    public String title;
    public String state;
    public Integer passDelayMs;
    public Integer failDelayMs;
    public Double passMargin;
    public Double failMargin;
    public Boolean isDeferred;
    public String mode;


    public List<Constraint> children = new ArrayList<>();

    public Constraint() {}

    public Constraint(Integer id, String type, String title, String state) {
        super(id,type);
        this.title = title;
        this.state = state;
    }

    public boolean isPassed() {
        return "passed".equalsIgnoreCase(state);
    }

    @Override
    public String toString() {
    	return super.toString();
    }
}

