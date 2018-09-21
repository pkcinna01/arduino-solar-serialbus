package com.xmonit.solar.arduino.data;

import com.xmonit.solar.arduino.ArduinoException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ArduinoGetResponse extends ArduinoResponse implements Serializable {

    public List<Sensor> sensors = new ArrayList();
    //public List<Device> devices = new ArrayList();
    //public Integer fanMode;
    //public String fanModeText;
    //public List<PowerMeter> powerMeters = new ArrayList();


    //public double getFanModeAsDouble() {
    //    return fanMode == null ? Double.NaN : fanMode.doubleValue();
    //}


    public void invalidate(Exception ex) {

        //fanMode = null;
        //fanModeText = null;

        //for (PowerMeter m : powerMeters) {
        //    m.invalidate();
        //}
        //for (Device d : devices) {
        //    d.invalidate();
        //}
        for (Sensor s : sensors) {
            s.invalidate();
        }
        if (ex instanceof ArduinoException) {
            ArduinoException sex = (ArduinoException) ex;
            respCode = sex.reasonCode.orElse(-1);
        } else {
            respCode = -1;
        }

        respMsg = ex == null ? "Invalidated" : ex.getMessage();

    }


    /**
     * Micrometer instrumentation does not allow unregistering meters that are no longer
     * valid or replaced.  This copy method recycles the data object so the registered metrics
     * still point to most recent valid values.  It assumes the length of arrays never change after
     * the first valid response from the arduino is processed.
     */
    public void copy(ArduinoGetResponse src) throws Exception {

        //setFanMode(src.getFanMode());
        //setFanModeText(src.getFanModeText());
        setRespCode(src.getRespCode());
        setRespMsg(src.getRespMsg());

        if (respCode == 0) {
            //if (src.powerMeters.size() != powerMeters.size()) {
            //    throw new ArduinoException("Arduino power meter count changed from "
            //            + src.powerMeters.size() + " to " + powerMeters.size() + " (unsupported). ", -1);
            //} else
            //if (src.devices.size() != devices.size()) {
            //    throw new ArduinoException("Arduino device count changed from "
            //            + src.devices.size() + " to " + devices.size() + " (unsupported). ", -1);
            //}
            if (src.sensors.size() != sensors.size()) {
                throw new ArduinoException("Arduino sensor count changed from "
                        + src.sensors.size() + " to " + sensors.size() + " (unsupported). ", -1);
            }
        }

        //for (int i = 0; i < powerMeters.size(); i++) {
        //    powerMeters.get(i).copy(src.powerMeters.get(i));
        //}

        //for (int i = 0; i < devices.size(); i++) {
        //    devices.get(i).copy(src.devices.get(i));
        //}
        for (int i = 0; i < sensors.size(); i++) {
            sensors.get(i).copy(src.sensors.get(i));
        }
    }



    public ArduinoGetResponse deepCopy() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(this);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (ArduinoGetResponse) ois.readObject();
    }


}
