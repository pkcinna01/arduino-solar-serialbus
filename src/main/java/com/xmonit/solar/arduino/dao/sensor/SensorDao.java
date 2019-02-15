package com.xmonit.solar.arduino.dao.sensor;

import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.dao.DomainDao;
import com.xmonit.solar.arduino.dao.annotation.ArduinoDao;
import com.xmonit.solar.arduino.dao.annotation.IntegerAccessor;
import com.xmonit.solar.arduino.data.sensor.Sensor;
import com.xmonit.solar.arduino.serial.ArduinoSerialBus;
import org.apache.commons.lang3.StringUtils;

@ArduinoDao
public class SensorDao extends DomainDao {

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

    @IntegerAccessor
    public SampleCntAccessor sampleCnt(int id) {
        return new SampleCntAccessor(id);
    }

    @IntegerAccessor(validationRegEx = "[+-][0-9]+")
    public SampleIntervalMsAccessor sampleIntervalMs(int id) {
        return new SampleIntervalMsAccessor(id);
    }

    @IntegerAccessor
    public PinAccessor sensorPin(int id) {
        return new PinAccessor(id);
    }
}