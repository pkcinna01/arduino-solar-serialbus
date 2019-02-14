package com.xmonit.solar.arduino.dao.device;

import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.serial.ArduinoSerialBus;
import com.xmonit.solar.arduino.data.device.CoolingFan;
import com.xmonit.solar.arduino.data.device.Device;

import java.util.Arrays;

public class CoolingFanDao extends PowerSwitchDao {

    public class MinDurationMsAccessor extends DeviceFieldAccessor<Double> {
        public MinDurationMsAccessor(int id) {
            super(id, "minDurationMs", Double.class);
        }
    }

    public class OffTempAccessor extends DeviceFieldAccessor<Double> {
        public OffTempAccessor(int id) {
            super(id, "offTemp", Double.class);
        }
    }

    public class OnTempAccessor extends DeviceFieldAccessor<Double> {
        public OnTempAccessor(int id) {
            super(id, "onTemp", Double.class);
        }
    }
    static boolean isCoolingFan(Device d) {
        return d.type != null && d.type.toLowerCase().endsWith("fan");
    }
    public CoolingFanDao(ArduinoSerialBus serialBus){
        super(serialBus);
    }

    public CoolingFan[] listCoolingFans() throws ArduinoException {
        return Arrays.stream(list(true)).filter(s -> isCoolingFan(s) ).toArray(CoolingFan[]::new);
    }
    public MinDurationMsAccessor minDurationMs(int deviceId) {
        return new MinDurationMsAccessor(deviceId);
    }

    public OffTempAccessor offTemp(int deviceId) {
        return new OffTempAccessor(deviceId);
    }
    public OnTempAccessor onTemp(int deviceId) {
        return new OnTempAccessor(deviceId);
    }



}