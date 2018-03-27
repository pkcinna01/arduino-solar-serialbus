package com.xmonit.solar.serialbus.data;

import com.xmonit.solar.serialbus.SerialBusException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ArduinoGetResponse extends ArduinoResponse {

    public List<Device> devices = new ArrayList();
    public Integer fanMode;
    public String fanModeText;
    public List<Shunt> shunts = new ArrayList();

    public double getFanModeAsDouble() {
        return fanMode == null ? Double.NaN : fanMode.doubleValue();
    }

    public void invalidate(Exception ex) {

        fanMode = null;
        fanModeText = null;

        for (Shunt s : shunts) {
            s.invalidate();
        }
        for (Device d : devices) {
            d.invalidate();
        }
        if (ex instanceof SerialBusException) {
            SerialBusException sex = (SerialBusException) ex;
            respCode = sex.reasonCode.orElse(-1);
        } else {
            respCode = -1;
        }

        respMsg = ex == null ? "Invalidated" : ex.getMessage();

    }
}
