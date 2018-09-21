package com.xmonit.solar.arduino.data;

import java.io.Serializable;


public class Sensor implements Serializable {
    public String name;
    public Double value;
    public Status status = new Status();

    public void copy(Sensor src) throws Exception {

        if (src == null) {
            invalidate();
        } else {
            name = src.name;
            value = src.value;
            status.copy(src.status);
        }
    }

    public void invalidate() {
        name = null;
        value = Double.NaN;
        status.invalidate();
    }

    public boolean isStatusOk() {
        return status != null && status.code != null && status.code == 0;
    }

    public double getValue() {
        return (value == null || !isStatusOk() ) ? Double.NaN : value.doubleValue();
    }
}
