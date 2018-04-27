package com.xmonit.solar.arduino;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class ArduinoSerialPort {

    public static final int DATABITS_7 = 7;
    public static final int DATABITS_8 = 8;

    public static final int STOPBITS_1 = 1;
    public static final int STOPBITS_2 = 2;

    public static final int PARITY_NONE = 0;
    public static final int PARITY_ODD = 1;
    public static final int PARITY_EVEN = 2;

    public int dataBits = DATABITS_8;
    public int stopBits = STOPBITS_1;
    public int parity = PARITY_ODD;
    public int baudRate = 38400;

    public abstract void close();

    public abstract InputStream getInputStream() throws IOException;

    public abstract OutputStream getOutputStream() throws IOException;

    public abstract void open(String portNamePattern) throws ArduinoException;

    public abstract String getPortName();

    public abstract boolean isOpen();

    public boolean portNameMatches(String portNamePattern) {
        String portName = getPortName();
        return portName != null && !portName.matches(portNamePattern);
    }
}


