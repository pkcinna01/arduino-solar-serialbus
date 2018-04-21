package com.xmonit.solar.arduino.data;

import lombok.Data;

import java.io.Serializable;


@Data
public class TempSensor implements Serializable {

    public Double heatIndex;
    public Double humidity;
    public String name;
    public Double temp;

    public void copy(TempSensor src) {
        if (src == null) {
            invalidate();
        } else {
            name = src.name;
            temp = src.temp;
            humidity = src.humidity;
            heatIndex = src.heatIndex;
        }
    }

    public double getHeatIndexAsDouble() {
        return heatIndex == null ? Double.NaN : heatIndex.doubleValue();
    }

    public double getHumidityAsDouble() {
        return humidity == null ? Double.NaN : humidity.doubleValue();
    }

    public double getTempAsDouble() {
        return temp == null ? Double.NaN : temp.doubleValue();
    }

    public void invalidate() {
        name = null;
        temp = null;
        humidity = null;
        heatIndex = null;
    }


}
