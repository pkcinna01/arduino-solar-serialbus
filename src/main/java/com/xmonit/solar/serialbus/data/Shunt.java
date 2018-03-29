package com.xmonit.solar.serialbus.data;

import lombok.Data;

import java.io.Serializable;


@Data
public class Shunt implements Serializable {

    public Double amps;
    public Double ratedAmps;
    public Double ratedMilliVolts;

    public void copy(Shunt src) {
        if (src == null) {
            invalidate();
        } else {
            amps = src.amps;
            ratedAmps = src.ratedAmps;
            ratedMilliVolts = src.ratedMilliVolts;
        }
    }

    public double getAmpsAsDouble() {
        return amps == null ? Double.NaN : amps.doubleValue();
    }
    public double getRatedAmpsAsDouble() {
        return ratedAmps == null ? Double.NaN : ratedAmps.doubleValue();
    }
    public double getRatedMilliVoltsAsDouble() {
        return ratedMilliVolts == null ? Double.NaN : ratedMilliVolts.doubleValue();
    }


    public void invalidate() {
        amps = null;
        ratedAmps = null;
        ratedMilliVolts = null;
    }
}
