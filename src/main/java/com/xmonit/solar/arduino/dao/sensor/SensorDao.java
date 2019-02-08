package com.xmonit.solar.arduino.dao.sensor;

import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.ArduinoSerialBus;
import com.xmonit.solar.arduino.dao.Dao;
import com.xmonit.solar.arduino.data.sensor.Sensor;
import org.apache.commons.lang3.StringUtils;

public class SensorDao extends Dao {

    public class PinAccessor extends SensorFieldAccessor<Integer> {
        public PinAccessor(int id) {
            super(id, "sensorPin", Integer.class);
        }
    }

    public class SampleCntAccessor extends SensorFieldAccessor<Integer> {
        public SampleCntAccessor(int id) {
            super(id, "sampleCnt", Integer.class);
        }
    }

    public class SampleIntervalMsAccessor extends SensorFieldAccessor<Integer> {
        public SampleIntervalMsAccessor(int id) {
            super(id, "sampleIntervalMs", Integer.class);
        }
    }

    public class SensorFieldAccessor<ResultT> extends ObjectFieldAccessor<ResultT> {
        public SensorFieldAccessor(int id, String fieldName, Class<ResultT> c) {
            super(id, ObjectType.SENSOR, fieldName, c);
        }
    }

    public SensorDao(ArduinoSerialBus serialBus) {
        super(serialBus);
    }

    @Override
    public Sensor[] findByTitleLike(String wildcardFilter, boolean bVerbose) throws ArduinoException {
        return doCommand("include,sensors," + wildcardFilter, "sensors", Sensor[].class, bVerbose);
    }

    @Override
    public Sensor[] findByTitleNotLike(String wildcardFilter, boolean bVerbose) throws ArduinoException {
        return doCommand("exclude,sensors," + wildcardFilter, "sensors", Sensor[].class, bVerbose);
    }

    @Override
    public Sensor[] findWhereIdIn(int[] ids, boolean bVerbose) throws ArduinoException {
        return doCommand("get,sensor," + StringUtils.join(ids, ','), "sensor", Sensor[].class, bVerbose);
    }

    @Override
    public Sensor get(int id, boolean bVerbose) throws ArduinoException {
        return doCommand("get,sensor," + id, "sensor", Sensor[].class, bVerbose)[0];
    }

    @Override
    public Sensor[] list(boolean bVerbose) throws ArduinoException {
        return doCommand("get,sensors", "sensors", Sensor[].class, bVerbose);
    }

    public SampleCntAccessor sampleCnt(int id) {
        return new SampleCntAccessor(id);
    }

    public SampleIntervalMsAccessor sampleIntervalMs(int id) {
        return new SampleIntervalMsAccessor(id);
    }

    public PinAccessor sensorPin(int id) {
        return new PinAccessor(id);
    }
}