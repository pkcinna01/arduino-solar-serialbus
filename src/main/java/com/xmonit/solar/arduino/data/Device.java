package com.xmonit.solar.arduino.data;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class Device implements Serializable {

    public List<Fan> fans = new ArrayList();
    public String name;
    public List<TempSensor> tempSensors = new ArrayList();

    public void copy(Device src) throws Exception {

        if (src == null) {
            invalidate();
        } else {
            name = src.name;

            if ( tempSensors.size() != src.tempSensors.size() ) {
                throw new Exception("src and dest have different tempSensors list sizes");
            }
            for (int i = 0; i < tempSensors.size(); i++) {
                tempSensors.get(i).copy(src.tempSensors.get(i));
            }

            if ( fans.size() != src.fans.size() ) {
                throw new Exception("src and dest have different fans list sizes");
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
