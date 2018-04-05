package com.xmonit.solar.serialbus;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import purejavacomm.CommPortIdentifier;
import purejavacomm.SerialPort;

import java.io.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;


public class SerialBus {

    public static final Integer NO_VALIDATION_REQ_ID = -1;
    public static final Integer AUTO_GENERATE_REQ_ID = null;

    private static final Logger logger = LoggerFactory.getLogger(SerialBus.class);

    public String commPortName;

    protected SerialBusConfig config;
    protected CommPortIdentifier portId = null;
    protected LinkedList<SerialBusResponseProcessor> responseProcessors = new LinkedList();
    protected SerialPort serialPort = null;

    private boolean bPortOpen = false;
    private ObjectMapper mapper = new ObjectMapper();


    public SerialBus(SerialBusConfig config, List<SerialBusResponseProcessor> rpList) {
        this.config = config;
        responseProcessors.addAll(rpList);
    }


    public SerialBus(SerialBusConfig config, SerialBusResponseProcessor rp) {
        this(config, Collections.singletonList(rp));
    }


    public synchronized void close() {

        bPortOpen = false;
        if (serialPort != null) {
            try {
                serialPort.close();
                logger.info("USB port closed for " + commPortName);
            } catch (Exception ex) {
                logger.warn("USB port '" + commPortName + "' close failed.", ex);
            }
        } else {
            logger.warn("Serial port close attempted but port is null.  Port: " + commPortName);
        }
    }


    public synchronized String execute(String cmd, String portName, Integer explicitReqId) throws Exception {

        if (cmd == null || cmd.trim().isEmpty()) {
            cmd = config.getCmd();
        }

        if (portName == null || portName.isEmpty()) {
            portName = config.getCommPortRegEx();
        }

        if (commPortName != null && !commPortName.matches(portName)) {
            logger.warn("Existing serial port id '" + commPortName + "' does not match pattern '" + portName + "'.");
            close();
        }

        if (!isOpen()) {
            open(portName);
        }

        return execute(cmd, explicitReqId);
    }


    public synchronized boolean isOpen() {

        return bPortOpen;
    }


    public void processResponse(String strResp) throws Exception {
        JsonNode respNode = mapper.readTree(strResp);
        processResponse(respNode);
    }


    public void processResponse(JsonNode respNode) throws Exception {
        for (SerialBusResponseProcessor p : responseProcessors) {
            p.process(this,respNode);
        }
    }


    public void setCommPortName(String portName) {

        this.commPortName = portName;
    }


    static short requestId = 0;

    protected synchronized String execute(String command, Integer explicitReqId ) throws SerialBusException {

        logger.debug(command);

        try {
            int reqId = explicitReqId != null ? explicitReqId : ++requestId;
            OutputStream outputStream = serialPort.getOutputStream();
            String data = command + "|" + reqId;
            outputStream.write(data.getBytes());
            outputStream.flush();
            return extractResponse(serialPort.getInputStream(),reqId);
        } catch (SerialBusException ex) {
            logger.error("SerialBus.execute('" + command + "') failed.");
            close();
            throw ex;
        } catch (Exception ex) {
            close();
            throw new SerialBusException("SerialBus.execute('" + command + "') failed.", ex);
        }
    }


    private String extractResponse(InputStream respInputStream, int reqId) throws Exception {

        return extractResponse(new InputStreamReader(respInputStream), reqId);
    }


    private String extractResponse(Reader respReader, int reqId) throws Exception {

        try {

            final int waitForInputMs = 400;

            BufferedReader reader;

            if (respReader instanceof BufferedReader) {
                reader = (BufferedReader) respReader;
            } else {
                reader = new BufferedReader(respReader);
            }

            String line;
            StringBuilder sb = new StringBuilder();
            String resp = "";
            boolean bRespStarted = false;
            boolean bFoundEnd = false;
            boolean advancedToNextPound = false;

            for (int readAttempt = 0; !bFoundEnd && readAttempt < 10; readAttempt++) {
                if (readAttempt > 0) {
                    logger.debug("Characters read: " + sb.length() + "  (waiting for input...)");
                    Thread.sleep(waitForInputMs);
                }
                while ((line = reader.readLine()) != null) {
                    if (advancedToNextPound) {
                        advancedToNextPound = false;
                        line = '#' + line;
                    }
                    if (line.matches("#END[:0-9]*#")) {
                        resp = sb.toString();
                        bFoundEnd = true;
                        String[] tokens = line.replace("#","").split(":");
                        String strErrMsg = null;
                        if ( tokens.length != 4 ) {
                            strErrMsg = "Invalid #END# footer. Expected '#END:{req id}:{resp lenght}:{checksum}#'. Found: "
                            + line;
                        } else if ( reqId != NO_VALIDATION_REQ_ID ) {
                            int id = Integer.parseInt(tokens[1]);
                            if ( id != reqId ) {
                                strErrMsg = "Request #" + reqId + " response footer request ID does not match: " + id;
                            } else {
                                int len = Integer.parseInt(tokens[2]);
                                int respLen = resp.length() - 1;
                                if ( len != respLen ) {
                                    strErrMsg = "Request #" + reqId + " response footer contains length=" + len
                                            + " but received " + respLen;
                                }
                                else if ( tokens.length > 3 ) {
                                    int checksum = Integer.parseInt(tokens[3]);
                                    int computedChecksum = 0;
                                    byte[] bytes = resp.getBytes();
                                    for ( int i = 0; i < respLen; i++) {
                                        computedChecksum += bytes[i];
                                    }
                                    long computedChecksumL = 0 | computedChecksum;
                                    if ( checksum != computedChecksumL ) {
                                        strErrMsg = "Request #" + reqId + " response footer contains checksum=" + checksum
                                                + " but received " + computedChecksumL;
                                    }
                                }

                            }
                        }
                        if ( strErrMsg != null ) {
                            throw new SerialBusException(strErrMsg, 97);
                        }
                        break;
                    } else if (line.matches(".*#BEGIN[:0-9]*#")) {
                        if (sb.length() > 0) {
                            logger.warn("Found another #BEGIN#.  Ignoring buffered data: " + sb.toString());
                            sb.setLength(0);
                        }
                        if (!line.matches("#BEGIN[:0-9]*#")) {
                            String strIgnored = line.replaceAll("#BEGIN[:0-9]*#","");
                            logger.warn("Ignoring unexpected data before #BEGIN#: '" + strIgnored + "'");
                            line = line.substring(strIgnored.length());
                        }
                        String[] tokens = line.replace("#","").split("[:]+");
                        if ( tokens.length != 2 ) {
                            throw new SerialBusException("Invalid #BEGIN# header. Expected '#BEGIN:{req id}#'. Found: " + line, 98 );
                        } else if ( reqId != NO_VALIDATION_REQ_ID ){
                            try {
                                int id = Integer.parseInt(tokens[1]);
                                if ( id != reqId ) {
                                    throw new SerialBusException("Request ID mismatch in #BEGIN#.  Expected: " + reqId + " Found: " + id, 99);
                                }
                            } catch (Exception ex) {
                                throw new SerialBusException("Failed extracting request id from #BEGIN# header: " + line, ex );
                            }
                        }
                        bRespStarted = true;
                    } else if (bRespStarted) {
                        sb.append(line).append("\n");
                    } else {
                        if (!line.trim().isEmpty()) {
                            // Invalid state... try to find a #BEGIN#
                            StringBuilder ignoredText = new StringBuilder(line);
                            ignoredText.append('\n');
                            int ch;
                            do {
                                ch = reader.read();
                                if (ch == -1) {
                                    for (int i = 0; i < 10; i++) {
                                        Thread.sleep(waitForInputMs);
                                        if (ch != -1) {
                                            break;
                                        }
                                    }
                                }
                                if (ch == '#') {
                                    advancedToNextPound = true;
                                    break;
                                }
                                if (ignoredText.length() < 2048) {
                                    ignoredText.append((char) ch);
                                }
                            } while (ch != -1);

                            logger.warn("Unexpected input before #BEGIN# marker.");
                            logger.warn("Skipping USB data: " + ignoredText.toString());
                        }
                    }
                }
            }
            if (!bFoundEnd) {
                logger.debug("#END# not found. Ignoring response: " + sb.toString());
                throw new SerialBusException("Did not find #END# marker in serial bus response.", -1);
            }
            return resp;
        } catch (SerialBusException ex) {
            throw ex;
        } catch (Exception ex) {
            String msg = "Failed reading or parsing data from Arduino";
            throw new SerialBusException(msg, ex);
        }
    }


    private CommPortIdentifier findPortId(String commPortName) throws Exception {

        Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            CommPortIdentifier id = (CommPortIdentifier) portList.nextElement();
            if (id.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (id.getName().matches(commPortName)) {
                    this.commPortName = id.getName();
                    logger.info("Found serial port matching '" + commPortName + "': '" + id.getName() + "'");
                    portId = id;
                    return portId;
                }
            }
        }
        throw new SerialBusException("No serial port matched regex: '" + commPortName + "'", -1);
    }


    private void open(String commPortName) throws Exception {

        final int baudRate = config.getBaudRate();
        findPortId(commPortName);
        SerialPort port = (SerialPort) portId.open("java-arduino", 2000);
        port.setSerialPortParams(baudRate,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_ODD);
                //SerialPort.PARITY_NONE);
        serialPort = port;
        serialPort.notifyOnDataAvailable(true);
        serialPort.notifyOnOutputEmpty(true);
        Thread.sleep(2000);
        bPortOpen = true;
        logger.info("USB port opened for '" + this.commPortName + "'. Baud rate: " + baudRate);
    }


}
