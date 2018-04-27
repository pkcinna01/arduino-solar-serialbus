package com.xmonit.solar.arduino;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.fazecast.jSerialComm.SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
import static com.fazecast.jSerialComm.SerialPort.LISTENING_EVENT_DATA_RECEIVED;
import static com.fazecast.jSerialComm.SerialPort.LISTENING_EVENT_DATA_WRITTEN;

public class JSerialCommSerialPortImpl extends ArduinoSerialPort implements SerialPortDataListener {

    private static final Logger logger = LoggerFactory.getLogger(JSerialCommSerialPortImpl.class);

    protected SerialPort jscSerialPort;

    @Override
    public void close() {
        if ( jscSerialPort != null ) {
            jscSerialPort.closePort();
        }
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return jscSerialPort.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return jscSerialPort.getOutputStream();
    }

    @Override
    public synchronized void open(String portNamePattern) throws ArduinoException {

        try {
            jscSerialPort = findPort(portNamePattern);
            jscSerialPort.setComPortParameters(baudRate,dataBits,stopBits,parity);
            jscSerialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING,4000,4000);
            jscSerialPort.openPort();
            //jscSerialPort.addDataListener(this);
            //Thread.sleep(3000);
        } catch ( Exception ex ) {
            jscSerialPort = null;
            throw new ArduinoException("Failed opening comm port for " + portNamePattern, ex);
        }

    }

    @Override
    public String getPortName() {
        return jscSerialPort == null ? null : jscSerialPort.getSystemPortName();
    }

    @Override
    public boolean isOpen() {
        return jscSerialPort != null && jscSerialPort.isOpen();
    }

    private SerialPort findPort(String commPortName) throws ArduinoException {

        for(SerialPort sp: com.fazecast.jSerialComm.SerialPort.getCommPorts()) {
            if (sp.getSystemPortName().matches(commPortName)) {
                return sp;
            }
        }
        throw new ArduinoException("No serial port matched regex: '" + commPortName + "'", -1);
    }

    @Override
    public int getListeningEvents() {
        return LISTENING_EVENT_DATA_AVAILABLE | LISTENING_EVENT_DATA_RECEIVED | LISTENING_EVENT_DATA_WRITTEN;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        logger.info("Received jSerialComm port event: " + event.toString());
//            //jscSerialPort.notifyOnDataAvailable(true);
//            //jscSerialPort.notifyOnOutputEmpty(true);
    }
}