package com.xmonit.solar.serialbus.data;

import lombok.Data;

@Data
public class ArduinoResponse {

    public Integer respCode;
    public String respMsg;

    public double getRespCodeAsDouble() {
        return respCode == null ? Double.NaN : respCode.doubleValue();
    }

}
