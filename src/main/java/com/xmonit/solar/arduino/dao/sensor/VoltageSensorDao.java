package com.xmonit.solar.arduino.dao.sensor;

import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.dao.annotation.ArduinoDao;
import com.xmonit.solar.arduino.dao.annotation.DoubleAccessor;
import com.xmonit.solar.arduino.dao.annotation.IntegerAccessor;
import com.xmonit.solar.arduino.data.sensor.Sensor;
import com.xmonit.solar.arduino.data.sensor.VoltageSensor;
import com.xmonit.solar.arduino.serial.ArduinoSerialBus;

import java.util.Arrays;

@ArduinoDao
public class VoltageSensorDao extends SensorDao {

    public class MaxVccAgeMsAccessor extends SensorFieldAccessor<Integer> {
        public MaxVccAgeMsAccessor(int id) {
            super(id,"maxVccAgeMs", Integer.class);
        }
    }

    public class R1Accessor extends SensorFieldAccessor<Double> {
        public R1Accessor(int id) {
            super(id,"r1", Double.class);
        }
    }

    public class R2Accessor extends SensorFieldAccessor<Double> {
        public R2Accessor(int id) {
            super(id,"r2", Double.class);
        }
    }

    public class VccAccessor extends SensorFieldAccessor<Double> {
        public VccAccessor(int id) {
            super(id,"vcc", Double.class);
        }
    }

    public VoltageSensorDao(ArduinoSerialBus serialBus) {
        super(serialBus);
    }

    protected boolean isVoltageSensor(Sensor s) { return "VoltageSensor".equalsIgnoreCase(s.type); }

    public VoltageSensor[] listVoltageSensors() throws ArduinoException {
        return Arrays.stream(list(true)).filter( s -> isVoltageSensor(s) ).toArray(VoltageSensor[]::new);
    }

    @IntegerAccessor
    public MaxVccAgeMsAccessor maxVccAgeMs(int id) {
        return new MaxVccAgeMsAccessor(id);
    }

    @DoubleAccessor
    public R1Accessor r1(int id) {
        return new R1Accessor(id);
    }

    @DoubleAccessor
    public R2Accessor r2(int id) {
        return new R2Accessor(id);
    }

    @DoubleAccessor
    public VccAccessor vcc(int id) {
        return new VccAccessor(id);
    }
}