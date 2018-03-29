package com.xmonit.solar.serialbus.data;

import lombok.Data;

import java.io.Serializable;

@Data
public class ArduinoResponse implements Serializable {

    public Integer respCode;
    public String respMsg;

    public double getRespCodeAsDouble() {
        return respCode == null ? Double.NaN : respCode.doubleValue();
    }

}
