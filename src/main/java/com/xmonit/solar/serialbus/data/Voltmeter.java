package com.xmonit.solar.serialbus.data;

import lombok.Data;

import java.io.Serializable;


@Data
public class Voltmeter implements Serializable {

    public Byte analogPin;
    public Double assignedR1;
    public Double assignedR2;
    public Double assignedVcc;
    public Double volts;


    public void copy(Voltmeter src) {
        if (src == null) {
            invalidate();
        } else {
            volts = src.volts;
            analogPin = src.analogPin;
            assignedVcc = src.assignedVcc;
            assignedR1 = src.assignedR1;
            assignedR2 = src.assignedR2;
        }
    }


    public double getAnalogPinAsDouble() {
        return analogPin == null ? Double.NaN : analogPin.doubleValue();
    }


    public double getAssignedR1AsDouble() {
        return assignedR1 == null ? Double.NaN : assignedR1.doubleValue();
    }


    public double getAssignedR2AsDouble() {
        return assignedR2 == null ? Double.NaN : assignedR2.doubleValue();
    }


    public double getAssignedVccAsDouble() {
        return assignedVcc == null ? Double.NaN : assignedVcc.doubleValue();
    }


    public double getVoltsAsDouble() {
        return volts == null ? Double.NaN : volts.doubleValue();
    }


    public void invalidate() {
        volts = null;
        analogPin = null;
        assignedVcc = null;
        assignedR1 = null;
        assignedR2 = null;
    }
}
