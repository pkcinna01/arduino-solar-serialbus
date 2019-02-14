package com.xmonit.solar.arduino.serial;

import com.xmonit.solar.arduino.ArduinoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import purejavacomm.CommPortIdentifier;
import purejavacomm.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;

public class PJCArduinoSerialPort extends ArduinoSerialPort {

    private static final Logger logger = LoggerFactory.getLogger(PJCArduinoSerialPort.class);

    public static void createPorts(String strTtyRegEx, List<ArduinoSerialPort> ports) {
        Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            CommPortIdentifier id = (CommPortIdentifier) portList.nextElement();
            if (id.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (id.getName().matches(strTtyRegEx)) {
                    ports.add( new PJCArduinoSerialPort(id));
                }
            }
        }
    }
    protected boolean isOpen = false;
    protected purejavacomm.SerialPort pjcSerialPort;

    protected CommPortIdentifier portIdentifier;

    public PJCArduinoSerialPort(CommPortIdentifier portIdentifier) {
        this.portIdentifier = portIdentifier;
    }

    @Override
    public void close() {
        try {
            pjcSerialPort.close();
        } finally {
            isOpen = false;
        }
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return pjcSerialPort.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return pjcSerialPort.getOutputStream();
    }

    @Override
    public String getPortName() {
        return portIdentifier == null ? null : portIdentifier.getName();
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public synchronized void open() throws ArduinoException {

        try {
            pjcSerialPort = (SerialPort) portIdentifier.open("java-arduino", 2000);
            pjcSerialPort.setSerialPortParams(baudRate,dataBits,stopBits,parity);
            pjcSerialPort.notifyOnDataAvailable(true);
            pjcSerialPort.notifyOnOutputEmpty(true);
            isOpen = true;
            //Thread.sleep(200);
        } catch ( Exception ex ) {
            throw new ArduinoException("Failed opening comm port for " + getPortName(), ex);
        }

    }

}