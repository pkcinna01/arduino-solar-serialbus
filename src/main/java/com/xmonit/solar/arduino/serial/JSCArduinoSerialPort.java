package com.xmonit.solar.arduino.serial;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.xmonit.solar.arduino.ArduinoException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import static com.fazecast.jSerialComm.SerialPort.*;

@Slf4j
public class JSCArduinoSerialPort extends ArduinoSerialPort implements SerialPortDataListener {

    static long lastOpenTimeMs = 0;

    public static void createPorts(String strTtyRegEx, List<ArduinoSerialPort> ports) {
        for(SerialPort sp: SerialPort.getCommPorts()) {
            if (sp.getSystemPortName().matches(strTtyRegEx)) {
                ports.add( new JSCArduinoSerialPort(sp));
            }
        }
    }

    protected SerialPort jscSerialPort;

    public JSCArduinoSerialPort(SerialPort sp) {
        this.jscSerialPort = sp;
    }


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
    public int getListeningEvents() {
        return LISTENING_EVENT_DATA_AVAILABLE | LISTENING_EVENT_DATA_RECEIVED | LISTENING_EVENT_DATA_WRITTEN;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {

        return jscSerialPort.getOutputStream();
    }

    @Override
    public String getPortName() {
        return jscSerialPort == null ? null : jscSerialPort.getSystemPortName();
    }

    @Override
    public boolean isOpen() {
        return jscSerialPort != null && jscSerialPort.isOpen();
    }


    @Override
    public synchronized void open() throws ArduinoException {
        try {
            jscSerialPort.setComPortParameters(baudRate,dataBits,stopBits,parity);
            //jscSerialPort.addDataListener(this); // only use in async mode or some data will go to listener
            //jscSerialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING,3000,0);
            jscSerialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING,500,0);
            boolean opened = jscSerialPort.openPort();
            if ( !opened ) {
                throw new Exception("Failed opening Arduino port for " + jscSerialPort.getSystemPortName() );
            }
            long elapsedTimeSinceLastOpenMs = System.currentTimeMillis() - lastOpenTimeMs;
            lastOpenTimeMs = System.currentTimeMillis();
            long delayMs = elapsedTimeSinceLastOpenMs > 5*60*1000 ? 1750 : 215;
            log.debug("delay after open() = " + delayMs + "ms.");
            Thread.sleep( delayMs );
        } catch ( Exception ex ) {
            jscSerialPort = null;
            throw new ArduinoException("Failed opening comm port for " + getPortName(), ex);
        }

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