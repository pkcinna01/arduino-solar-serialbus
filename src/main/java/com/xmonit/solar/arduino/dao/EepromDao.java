package com.xmonit.solar.arduino.dao;


import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.serial.ArduinoSerialBus;
import com.xmonit.solar.arduino.data.Eeprom;
import com.xmonit.solar.arduino.data.JsonFormat;

public class EepromDao extends SingletonDao<Eeprom> {


    public class DeviceNameAccessor extends EepromFieldAccessor<String> {

        public DeviceNameAccessor() {
            super( "deviceName", String.class);
        }
    }

    public class EepromFieldAccessor<DataT> extends SingletonFieldAccessor<DataT> {

        public EepromFieldAccessor(String fieldName, Class<DataT> fieldClass) {
            super(ObjectType.EEPROM, fieldName, fieldClass);
        }

        @Override
        protected String getSetCmdBase() {
            return objectType.name() + "," + "set," + fieldName + ",";
        }
    }

    public class JsonFormatAccessor extends EepromFieldAccessor<JsonFormat> {

        public JsonFormatAccessor() {
            super( "jsonFormat", JsonFormat.class);
        }
    }


    public EepromDao(ArduinoSerialBus serialBus) {
        super(serialBus);
    }

    @Override
    public Eeprom get() throws ArduinoException {
        return getEeprom();
    }

    public JsonFormatAccessor jsonFormat() {
        return new JsonFormatAccessor();
    }


}
