package com.xmonit.solar.arduino;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import purejavacomm.CommPortIdentifier;
import purejavacomm.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

public class PureJavaCommSerialPortImpl extends ArduinoSerialPort {

    private static final Logger logger = LoggerFactory.getLogger(PureJavaCommSerialPortImpl.class);

    protected purejavacomm.SerialPort pjcSerialPort;
    protected CommPortIdentifier portIdentifier;
    protected boolean isOpen = false;

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
    public synchronized void open(String portNamePattern) throws ArduinoException {

        try {
            portIdentifier = findPortId(portNamePattern);
            pjcSerialPort = (SerialPort) portIdentifier.open("java-arduino", 2000);
            pjcSerialPort.setSerialPortParams(baudRate,dataBits,stopBits,parity);
            pjcSerialPort.notifyOnDataAvailable(true);
            pjcSerialPort.notifyOnOutputEmpty(true);
            isOpen = true;
            Thread.sleep(2000);
        } catch ( Exception ex ) {
            throw new ArduinoException("Failed opening comm port for " + portNamePattern, ex);
        }

    }

    @Override
    public String getPortName() {
        return portIdentifier == null ? null : portIdentifier.getName();
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    private CommPortIdentifier findPortId(String commPortName) throws Exception {

        Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            CommPortIdentifier id = (CommPortIdentifier) portList.nextElement();
            if (id.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (id.getName().matches(commPortName)) {
                    logger.info("Found serial port matching '" + commPortName + "': '" + id.getName() + "'");
                    return id;
                }
            }
        }
        throw new ArduinoException("No serial port matched regex: '" + commPortName + "'", -1);
    }

}