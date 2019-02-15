package com.xmonit.solar.arduino.serial;

import com.xmonit.solar.arduino.ArduinoException;
import lombok.extern.slf4j.Slf4j;
import purejavacomm.CommPortIdentifier;
import purejavacomm.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;

@Slf4j
public class PJCArduinoSerialPort extends ArduinoSerialPort {

    static long lastOpenTimeMs = 0;

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
            long elapsedTimeSinceLastOpenMs = System.currentTimeMillis() - lastOpenTimeMs;
            lastOpenTimeMs = System.currentTimeMillis();
            long delayMs = elapsedTimeSinceLastOpenMs > 5*60*1000 ? 1750 : 215;
            log.debug("delay after open() = " + delayMs + "ms.");
            Thread.sleep( delayMs );
        } catch ( Exception ex ) {
            throw new ArduinoException("Failed opening comm port for " + getPortName(), ex);
        }

    }

}