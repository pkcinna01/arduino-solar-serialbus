package com.xmonit.solar.arduino.serial;

import com.xmonit.solar.arduino.ArduinoConfig;
import com.xmonit.solar.arduino.ArduinoException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ArduinoSerialPortFactory {

	public static List<ArduinoSerialPort> createPorts(Class<? extends ArduinoSerialPort> portClass, String strTtyPattern, ArduinoConfig config ) throws ArduinoException {
		List<ArduinoSerialPort> ports = new ArrayList<>();
		try {
			Method m = portClass.getMethod("createPorts", String.class, List.class);
			m.invoke(ports, strTtyPattern, ports);

			for( ArduinoSerialPort port : ports ) {
				port.init(config.getPortConfig(port.getPortName()));
			}
		} catch (Exception ex) {
			throw new ArduinoException("Failed creating Serial Ports using '" + portClass.getSimpleName()
					+ "' as the implementation (ttyRegEx: '" + strTtyPattern + "'", ex);
		}
		return ports;
	}


	public static List<ArduinoSerialPort> createPorts(String strPortClass, String strTtyPattern, ArduinoConfig config ) throws ArduinoException {
		try {
			Class<? extends ArduinoSerialPort> portClass = Class.forName(strPortClass).asSubclass(ArduinoSerialPort.class);
			return createPorts(portClass,strTtyPattern,config);
		} catch (ClassNotFoundException ex) {
			throw new ArduinoException("Failed finding or createing ArduinoSerialPort  class '" + strPortClass + "'", ex);
		}
	}

}
