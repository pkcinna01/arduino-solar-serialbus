package com.xmonit.solar.arduino.dao.sensor;

import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.dao.annotation.ArduinoDao;
import com.xmonit.solar.arduino.dao.annotation.ChoiceAccessor;
import com.xmonit.solar.arduino.dao.annotation.DoubleAccessor;
import com.xmonit.solar.arduino.data.sensor.CurrentSensor;
import com.xmonit.solar.arduino.data.sensor.Sensor;
import com.xmonit.solar.arduino.serial.ArduinoSerialBus;

import java.util.Arrays;

@ArduinoDao
public class CurrentSensorDao extends SensorDao {

    public class ChannelAccessor extends SensorFieldAccessor<CurrentSensor.Channel> {
        public ChannelAccessor(int id) {
            super(id,"channel", CurrentSensor.Channel.class);
        }
    }

    public class GainAccessor extends SensorFieldAccessor<CurrentSensor.Gain> {
        public GainAccessor(int id) {
            super(id,"gain", CurrentSensor.Gain.class);
        }
    }

    public class RatedAmpsAccessor extends SensorFieldAccessor<Double> {
        public RatedAmpsAccessor(int id) {
            super(id,"ratedAmps", Double.class);
        }
    }

    public class RatedMillivoltsAccessor extends SensorFieldAccessor<Double> {
        public RatedMillivoltsAccessor(int id) {
            super(id,"ratedMillivolts", Double.class);
        }
    }

    public class RatedOhmsAccessor extends SensorFieldAccessor<Double> {
        public RatedOhmsAccessor(int id) {
            super(id,"ratedOhms", Double.class);
        }
    }

    public CurrentSensorDao(ArduinoSerialBus serialBus) {
        super(serialBus);
    }

    @ChoiceAccessor
    public ChannelAccessor channel(int id) {
        return new ChannelAccessor(id);
    }

    @ChoiceAccessor
    public GainAccessor gain(int id) {
        return new GainAccessor(id);
    }

    protected boolean isCurrentSensor(Sensor s) { return "CurrentSensor".equalsIgnoreCase(s.type); }

    public CurrentSensor[] listCurrentSensors() throws ArduinoException {
        return Arrays.stream(list(true)).filter( s -> isCurrentSensor(s) ).toArray(CurrentSensor[]::new);
    }

    @DoubleAccessor
    public RatedAmpsAccessor ratedAmps(int id) {
        return new RatedAmpsAccessor(id);
    }

    @DoubleAccessor
    public RatedMillivoltsAccessor ratedMillivolts(int id) {
        return new RatedMillivoltsAccessor(id);
    }

    @DoubleAccessor
    public RatedOhmsAccessor ratedOhms(int id) {
        return new RatedOhmsAccessor(id);
    }

    /*
    w.printlnStringObj(F("channel"), strChannel,",");
    w.printlnNumberObj(F("shuntADC"), shuntADC, ",");
    w.printlnStringObj(F("gain"), strGain, ",");
    w.printlnNumberObj(F("millivoltIncrement"),buffer,",");
    w.printlnNumberObj(F("ratedAmps"), ratedAmps, ",");
    w.printlnNumberObj(F("ratedMillivolts"), ratedMillivolts,",");
    w.printlnNumberObj(F("ratedOhms"), buffer,",");
    */

}