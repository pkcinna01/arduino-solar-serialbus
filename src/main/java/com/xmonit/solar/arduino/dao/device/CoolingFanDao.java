package com.xmonit.solar.arduino.dao.device;

import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.dao.annotation.DoubleAccessor;
import com.xmonit.solar.arduino.dao.annotation.IntegerAccessor;
import com.xmonit.solar.arduino.data.device.CoolingFan;
import com.xmonit.solar.arduino.data.device.Device;
import com.xmonit.solar.arduino.serial.ArduinoSerialBus;

import java.util.Arrays;

public class CoolingFanDao extends PowerSwitchDao {

    public class MinDurationMsAccessor extends DeviceFieldAccessor<Integer> {
        public MinDurationMsAccessor(int id) {
            super(id, "minDurationMs", Integer.class);
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

    public CoolingFanDao(ArduinoSerialBus serialBus) {
        super(serialBus);
    }

    public CoolingFan[] listCoolingFans() throws ArduinoException {
        return Arrays.stream(list(true)).filter(s -> isCoolingFan(s)).toArray(CoolingFan[]::new);
    }

    @IntegerAccessor
    public MinDurationMsAccessor minDurationMs(int deviceId) {
        return new MinDurationMsAccessor(deviceId);
    }

    @DoubleAccessor
    public OffTempAccessor offTemp(int deviceId) {
        return new OffTempAccessor(deviceId);
    }

    @DoubleAccessor
    public OnTempAccessor onTemp(int deviceId) {
        return new OnTempAccessor(deviceId);
    }


}