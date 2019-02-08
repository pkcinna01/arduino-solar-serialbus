package com.xmonit.solar.arduino.data.constraint;

import com.xmonit.solar.arduino.data.DomainObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Constraint extends DomainObject {

    public enum Mode { FAIL, PASS, TEST, REMOTE, REMOTE_OR_FAIL, REMOTE_OR_PASS, REMOTE_OR_TEST };

    public String title;
    public Boolean passed;
    public Integer passDelayMs;
    public Integer failDelayMs;
    public Double passMargin;
    public Double failMargin;
    public Boolean isDeferred;
    public Mode mode;


    public List<Constraint> children = new ArrayList<>();

    public Constraint() {}

    public Constraint(Integer id, String type, String title, Boolean bPassed) {
        super(id,type);
        this.title = title;
        this.passed = bPassed;
    }

    @Override
    public String toString() {
    	return super.toString();
    }
}

