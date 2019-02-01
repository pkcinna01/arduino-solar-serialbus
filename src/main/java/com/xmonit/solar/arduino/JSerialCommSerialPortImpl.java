package com.xmonit.solar.arduino;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.fazecast.jSerialComm.SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
import static com.fazecast.jSerialComm.SerialPort.LISTENING_EVENT_DATA_RECEIVED;
import static com.fazecast.jSerialComm.SerialPort.LISTENING_EVENT_DATA_WRITTEN;

public class JSerialCommSerialPortImpl extends ArduinoSerialPort implements SerialPortDataListener {

    protected SerialPort jscSerialPort;

    @Override
    public void close() {
        if ( jscSerialPort != null ) {
            jscSerialPort.closePort();
        }
        jscSerialPort = null;
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
            //jscSerialPort.addDataListener(this); // only use in async mode or some data will go to listener
            //jscSerialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING,3000,0);
            jscSerialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING,500,0);
            boolean opened = jscSerialPort.openPort();
            if ( !opened ) {
                throw new Exception("Failed opening Arduino port for " + jscSerialPort.getSystemPortName() );
            }
            Thread.sleep(500);
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
        switch(event.getEventType()){

            case LISTENING_EVENT_DATA_AVAILABLE:
                //logger.debug("LISTENING_EVENT_DATA_AVAILABLE");
                break;
            case LISTENING_EVENT_DATA_RECEIVED:
                //logger.debug("LISTENING_EVENT_DATA_RECEIVED");
                break;
            case LISTENING_EVENT_DATA_WRITTEN:
                //logger.debug("LISTENING_EVENT_DATA_WRITTEN");
                break;
        }
        //logger.info("Received: " + new String(event.getReceivedData()));
    }
}