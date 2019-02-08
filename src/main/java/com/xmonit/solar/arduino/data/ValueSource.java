package com.xmonit.solar.arduino.data;

import lombok.Data;

// Used to lookup a sensor or other value source from a constraint.  Example, you have a max temp constraint and you
// want information about the sensor used to measure the temp.

@Data
public class ValueSource {

    public Integer id;
    public String type;
    public Double value;

    public ValueSource() {}

}
