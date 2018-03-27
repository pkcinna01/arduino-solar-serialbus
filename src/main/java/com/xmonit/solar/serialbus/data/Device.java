package com.xmonit.solar.serialbus.data;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Device {

    public List<Fan> fans = new ArrayList();
    public String name;
    public List<TempSensor> tempSensors = new ArrayList();

    public void copy(Device src) {
        if (src == null) {
            invalidate();
        } else {
            name = src.name;

            for (int i = 0; i < tempSensors.size(); i++) {
                tempSensors.get(i).copy(src.tempSensors.get(i));
            }

            for (int i = 0; i < fans.size(); i++) {
                fans.get(i).copy(src.fans.get(i));
            }
        }
    }

    public void invalidate() {
        name = null;
        for (TempSensor s : tempSensors) {
            s.invalidate();
        }
        for (Fan f : fans) {
            f.invalidate();
        }
    }


}
