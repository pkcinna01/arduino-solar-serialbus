package com.xmonit.solar.serialbus.data;

import lombok.Data;

@Data
public class Fan {

    public String name;
    public Double offTemp;
    public Boolean on;
    public Double onTemp;
    public Integer relayPin;
    public Integer relayValue;

    public void copy(Fan src) {
        if (src == null) {
            invalidate();
        } else {
            name = src.name;
            relayPin = src.relayPin;
            onTemp = src.onTemp;
            offTemp = src.offTemp;
            relayValue = src.relayValue;
            on = src.on;
        }
    }

    public double getOffTempAsDouble() {
        return offTemp == null ? Double.NaN : offTemp.doubleValue();
    }

    public double getOnTempAsDouble() {
        return onTemp == null ? Double.NaN : onTemp.doubleValue();
    }

    public double getRelayPinAsDouble() {
        return relayPin == null ? Double.NaN : relayPin.doubleValue();
    }

    public double getRelayValueAsDouble() {
        return relayValue == null ? Double.NaN : relayValue.doubleValue();
    }

    public void invalidate() {
        name = null;
        relayPin = null;
        onTemp = null;
        offTemp = null;
        relayValue = null;
        on = false;
    }

}
