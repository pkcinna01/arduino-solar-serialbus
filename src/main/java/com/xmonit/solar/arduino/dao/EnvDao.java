package com.xmonit.solar.arduino.dao;


import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.data.Environment;
import com.xmonit.solar.arduino.serial.ArduinoSerialBus;

public class EnvDao extends SingletonDao<Environment> {


    public class EnvFieldAccessor<DataT> extends SingletonFieldAccessor<DataT> implements ReadOnly {

        public EnvFieldAccessor(String fieldName, Class<DataT> fieldClass) {
            super(ObjectType.ENV, fieldName, fieldClass);
        }

        @Override
        protected String getSetCmdBase() {
            return objectType.name() + "," + "set," + fieldName + ",";
        }
    }

    public EnvDao(ArduinoSerialBus serialBus) {
        super(serialBus);
    }

    @Override
    public Environment get() throws ArduinoException {
        return getEnvironment();
    }

}
