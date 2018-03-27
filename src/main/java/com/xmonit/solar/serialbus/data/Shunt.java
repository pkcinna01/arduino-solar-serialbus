package com.xmonit.solar.serialbus.data;

import lombok.Data;

@Data
public class Shunt {

    public String name;
    public Double shuntAmps;
    public Double shuntWatts;

    public void copy(Shunt src) {
        if (src == null) {
            invalidate();
        } else {
            name = src.name;
            shuntAmps = src.shuntAmps;
            shuntWatts = src.shuntWatts;
        }
    }

    public double getAmpsAsDouble() {
        return shuntAmps == null ? Double.NaN : shuntAmps.doubleValue();
    }

    public double getWattsAsDouble() {
        return shuntWatts == null ? Double.NaN : shuntWatts.doubleValue();
    }

    public void invalidate() {
        name = null;
        shuntAmps = null;
        shuntWatts = null;
    }
}
