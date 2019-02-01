package com.xmonit.solar.arduino.dao;


import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.ArduinoSerialBus;
import com.xmonit.solar.arduino.SerialCmd;

public abstract class Dao extends SerialCmd {


    public Dao(ArduinoSerialBus sb) {
        super(sb);
    }

    public abstract <DataT> DataT list() throws ArduinoException;
    public abstract <DataT> DataT get(int id) throws ArduinoException;
    public abstract <DataT> DataT findByTitleLike(String wildcardFilter) throws ArduinoException;
    public abstract <DataT> DataT findByTitleNotLike(String wildcardFilter) throws ArduinoException;

}
