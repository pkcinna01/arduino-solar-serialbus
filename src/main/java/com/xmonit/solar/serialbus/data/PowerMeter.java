package com.xmonit.solar.serialbus.data;

import lombok.Data;

import java.io.Serializable;


@Data
public class PowerMeter implements Serializable {

    public String name;
    public Voltmeter voltage;
    public Shunt current;
    public Double watts;


    public void copy(PowerMeter src) {
        if (src == null) {
            invalidate();
        } else {
            name = src.name;
            voltage.copy(src.voltage);
            current.copy(src.current);
        }
    }


    public double getWattsAsDouble() {
        return watts == null ? Double.NaN : watts.doubleValue();
    }


    public void invalidate() {
        name = null;
        voltage.invalidate();
        current.invalidate();
    }
}
