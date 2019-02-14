package com.xmonit.solar.arduino.dao.device;

import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.serial.ArduinoSerialBus;
import com.xmonit.solar.arduino.data.LogicLevel;
import com.xmonit.solar.arduino.data.device.Device;
import com.xmonit.solar.arduino.data.device.PowerSwitch;

import java.util.Arrays;

public class PowerSwitchDao extends DeviceDao {

    public class OnAccessor extends DeviceFieldAccessor<Boolean> {
        public OnAccessor(int id) {
            super(id, "on", Boolean.class);
        }
    }

    public class RelayOnSignalAccessor extends DeviceFieldAccessor<LogicLevel> {
        public RelayOnSignalAccessor(int id) {
            super(id, "relayOnSignal", LogicLevel.class);
        }
    }

    public class RelayPinAccessor extends DeviceFieldAccessor<Integer> {
        public RelayPinAccessor(int id) {
            super(id, "relayPin", Integer.class);
        }
    }

    static boolean isPowerSwitch(Device d) {
        return d.type != null && d.type.toLowerCase().endsWith("switch");
    }
    public PowerSwitchDao(ArduinoSerialBus serialBus){
        super(serialBus);
    }

    public PowerSwitch[] listPowerSwitches() throws ArduinoException {
        return Arrays.stream(list(true)).filter(s -> isPowerSwitch(s) ).toArray(PowerSwitch[]::new);
    }
    public OnAccessor on(int deviceId) {
        return new OnAccessor(deviceId);
    }

    public RelayOnSignalAccessor relayOnSignal(int deviceId) {
        return new RelayOnSignalAccessor(deviceId);
    }
    public RelayPinAccessor relayPin(int deviceId) {
        return new RelayPinAccessor(deviceId);
    }

}