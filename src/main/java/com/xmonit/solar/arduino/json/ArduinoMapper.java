package com.xmonit.solar.arduino.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.xmonit.solar.arduino.data.capability.Capability;
import com.xmonit.solar.arduino.data.constraint.Constraint;
import com.xmonit.solar.arduino.data.device.Device;
import com.xmonit.solar.arduino.data.sensor.Sensor;

public class ArduinoMapper extends ObjectMapper {

    public static ArduinoMapper instance = new ArduinoMapper();

    protected ArduinoMapper() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Sensor.class, new SensorDeserializer(null));
        module.addDeserializer(Device.class, new DeviceDeserializer(null));
        module.addDeserializer(Capability.class, new CapabilityDeserializer(null));
        module.addDeserializer(Constraint.class, new ConstraintDeserializer(null));
        registerModule(module);
    }
    
    public String asString(Object obj) throws JsonProcessingException {
    	return writeValueAsString(obj);
    }
}