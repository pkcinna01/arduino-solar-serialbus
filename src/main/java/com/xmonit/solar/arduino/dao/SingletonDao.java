package com.xmonit.solar.arduino.dao;


import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.serial.ArduinoSerialBus;

public abstract class SingletonDao<DataT> extends Dao {


    public SingletonDao(ArduinoSerialBus sb) {
        super(sb);
    }

    public abstract DataT get() throws ArduinoException;
}
