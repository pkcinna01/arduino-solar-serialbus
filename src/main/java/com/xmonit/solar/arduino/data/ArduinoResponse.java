package com.xmonit.solar.arduino.data;

import lombok.Data;

import java.io.Serializable;

@Data
public class ArduinoResponse implements Serializable {

    public Integer respCode;
    public String respMsg;

    //TODO - remove these hacks
    public String lastErrorMsg;
    public String lastInfoMsg;

    public double getRespCodeAsDouble() {
        return respCode == null ? Double.NaN : respCode.doubleValue();
    }

}
