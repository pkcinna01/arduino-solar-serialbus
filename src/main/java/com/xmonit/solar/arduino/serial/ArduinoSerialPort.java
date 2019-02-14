package com.xmonit.solar.arduino.serial;

import com.xmonit.solar.arduino.ArduinoConfig;
import com.xmonit.solar.arduino.ArduinoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class ArduinoSerialPort {

	public static final int DATABITS_7 = 7;

	public static final int DATABITS_8 = 8;
	private static final Logger logger = LoggerFactory.getLogger(ArduinoSerialPort.class);

	public static final int PARITY_EVEN = 2;
	public static final int PARITY_NONE = 0;

	public static final int PARITY_ODD = 1;
	public static final int STOPBITS_1 = 1;
	public static final int STOPBITS_2 = 2;

	// public int parity = PARITY_ODD;
	public int baudRate = 38400;
	public int dataBits = DATABITS_8;
	public int parity = PARITY_NONE;
	public int stopBits = STOPBITS_1;

	public abstract void close();
	public abstract InputStream getInputStream() throws IOException;
	public abstract OutputStream getOutputStream() throws IOException;
	public abstract String getPortName();

	public void init(ArduinoConfig.PortConfig conf) {
		setBaudRate( conf.baudRate ).setDataBits( conf.dataBits ).setParity( conf.parity ).setStopBits( conf.stopBits );
	}

	public abstract boolean isOpen();

	public abstract void open() throws ArduinoException;

	public ArduinoSerialPort setBaudRate(int br) { baudRate = br; return this; }

	public ArduinoSerialPort setDataBits(int db) { dataBits = db; return this; }

	public ArduinoSerialPort setParity(int p) { parity = p; return this; }

	public ArduinoSerialPort setStopBits(int sb) { stopBits = sb; return this; }


}
