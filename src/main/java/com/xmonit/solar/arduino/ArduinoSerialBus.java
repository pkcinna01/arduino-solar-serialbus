package com.xmonit.solar.arduino;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Collections;

import java.util.LinkedList;
import java.util.List;


public class ArduinoSerialBus {

    protected static short requestId = 0;

    private static final Logger logger = LoggerFactory.getLogger(ArduinoSerialBus.class);

    public ArduinoSerialPort serialPort = new JSerialCommSerialPortImpl();
    //public ArduinoSerialPort serialPort = new PureJavaCommSerialPortImpl();

    protected ArduinoConfig config;
    protected LinkedList<ArduinoResponseProcessor> responseProcessors = new LinkedList();

    private boolean bPortOpen = false;
    private ObjectMapper mapper = new ObjectMapper();


    public ArduinoSerialBus(ArduinoConfig config, List<ArduinoResponseProcessor> rpList) {
        this.config = config;
        responseProcessors.addAll(rpList);
    }


    public ArduinoSerialBus(ArduinoConfig config, ArduinoResponseProcessor rp) {
        this(config, Collections.singletonList(rp));
    }


    public synchronized void close() {

        bPortOpen = false;
        if (serialPort != null) {
            try {
                serialPort.close();
                logger.info("USB port closed for " + serialPort.getPortName());
            } catch (Exception ex) {
                logger.warn("USB port '" + serialPort.getPortName() + "' close failed.", ex);
            }
        } else {
            logger.warn("Serial port close attempted but port is null.  Port: " + serialPort.getPortName());
        }
    }


    public synchronized String execute(String cmd, String portName, Integer explicitReqId) throws Exception {

        if (cmd == null || cmd.trim().isEmpty()) {
            cmd = config.getCmd();
        }

        if (portName == null || portName.isEmpty()) {
            portName = config.getCommPortRegEx();
        }

        if ( isOpen() ) {
            if ( !serialPort.portNameMatches(portName) ) {
                logger.warn("Existing serial port id '" + serialPort.getPortName() + "' does not match pattern '" + portName + "'.");
                close();
            }
        } else {
            open(portName);
        }

        return execute(cmd, explicitReqId);
    }


    public boolean isOpen() {
        return serialPort.isOpen();
    }


    public void processResponse(String strResp) throws Exception {
        JsonNode respNode = mapper.readTree(strResp);
        processResponse(respNode);
    }


    public void processResponse(JsonNode respNode) throws Exception {
        for (ArduinoResponseProcessor p : responseProcessors) {
            p.process(this,respNode);
        }
    }


    public String getPortName() {
        return serialPort.getPortName();
    }


    protected synchronized String execute(String command, Integer explicitReqId ) throws ArduinoException {

        try {

            int reqId;
            if ( explicitReqId != null ) {
                if ( explicitReqId > 0x7FFF ) {
                    throw new ArduinoException("Request ID exceeeds maximum size: " + explicitReqId, -1);
                }
                reqId = explicitReqId;
            } else {
                if ( ++requestId > 0x7FFF ) {
                    requestId = 0;
                }
                reqId = requestId;
            }

            OutputStream outputStream = serialPort.getOutputStream();
            String data = command + "|" + reqId + "\n";
            if ( !command.startsWith("GET") ) {
                logger.info(data);
            }
            serialPort.send(data);

            //InputStream inputStream = serialPort.getInputStream();
            String strResp = serialPort.readUntilLine("^#END:"+reqId+":[:0-9]*#$",10000);
            InputStream inputStream = new ByteArrayInputStream(strResp.getBytes());

            return extractResponse(inputStream,reqId);
        } catch (ArduinoException ex) {
            logger.error("ArduinoSerialBus.execute('" + command + "') failed.");
            close();
            throw ex;
        } catch (Exception ex) {
            close();
            throw new ArduinoException("ArduinoSerialBus.execute('" + command + "') failed.", ex);
        }
    }


    /**
     * Workaround to buffered reader behaving differently between different serial port implementations
     * @param inputStream
     * @return one line from arduino USB response
     */
    private String readLine(InputStream inputStream) throws IOException {

        StringBuilder sb = new StringBuilder();
        int c;
        while ( (c=inputStream.read()) != -1 ) {
            if (c == 0 || c == '\r') {
                continue;
            }
            if (c == '\n') {
                break;
            }
            sb.append((char)c);
        }
        //logger.debug(sb.toString());
        return sb.toString();
    }

    private String extractResponse(InputStream respInputStream, int reqId) throws Exception {

        try {

            String line;
            StringBuilder sb = new StringBuilder();
            String resp = "";
            boolean bRespStarted = false;
            boolean bFoundEnd = false;
            boolean advancedToNextPound = false;

            while ((line = readLine(respInputStream)) != null) {
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
                        strErrMsg = "Invalid #END# footer. Expected '#END:{req id}:{resp length}:{checksum}#'. Found: "
                        + line;
                    } else {
                        int id = Integer.parseInt(tokens[1]);
                        if ( id != reqId ) {
                            strErrMsg = "Request #" + reqId + " response footer request ID does not match: " + id;
                        } else {
                            int len = Integer.parseInt(tokens[2]);
                            int respLen = resp.length() - 1;
                            if ( len != respLen ) {
                                strErrMsg = "Request #" + reqId + " response footer contains length=" + len
                                        + " but actual length=" + respLen;
                            }
                            int checksum = Integer.parseInt(tokens[3]);
                            long computedChecksum = ArduinoSerialPort.checksum(resp.getBytes(),respLen);
                            if ( checksum != computedChecksum ) {
                                strErrMsg = "Request #" + reqId + " response footer contains checksum=" + checksum
                                        + " but actual checksum=" + computedChecksum;
                            }
                        }
                    }
                    if ( strErrMsg != null ) {
                        throw new ArduinoException(strErrMsg, 97);
                    }
                    break;
                } else if (line.matches(".*#BEGIN:"+reqId+"#")) {
                    if (sb.length() > 0) {
                        logger.warn("Found another #BEGIN:" + reqId + "#.  Ignoring buffered data: " + sb.toString());
                        sb.setLength(0);
                    }
                    if (!line.matches("#BEGIN[:0-9]*#")) {
                        String strIgnored = line.replaceAll("#BEGIN[:0-9]*#","");
                        logger.warn("Ignoring unexpected data before #BEGIN#: '" + strIgnored + "'");
                        line = line.substring(strIgnored.length());
                    }
                    bRespStarted = true;
                } else if (bRespStarted) {
                    sb.append(line).append("\n");
                } else {
                    if (!line.trim().isEmpty()) {
                        logger.warn("Unexpected input before #BEGIN:" + reqId + "#");
                        StringBuilder ignoredText = new StringBuilder(line);
                        ignoredText.append('\n');
                        int ch;
                        while ( (ch=respInputStream.read()) != -1 ) {
                            if (ch == '#') {
                                advancedToNextPound = true;
                                break;
                            }
                            if (ignoredText.length() < 2048) {
                                ignoredText.append((char) ch);
                            }
                        };
                        logger.warn("Skipping USB data: [" + ignoredText.toString() + "]");
                    }
                }
            }
            if (!bFoundEnd) {
                logger.debug("#END# not found. Ignoring response: " + sb.toString());
                throw new ArduinoException("Did not find #END# marker in serial bus response.", -1);
            }
            return resp;
        } catch (ArduinoException ex) {
            throw ex;
        } catch (Exception ex) {
            String msg = "Failed reading or parsing data from Arduino";
            throw new ArduinoException(msg, ex);
        }
    }


    private void open(String commPortNamePattern) throws Exception {

        serialPort.baudRate = config.getBaudRate();
        serialPort.open(commPortNamePattern);
        logger.info("USB port opened for '" + serialPort.getPortName() + "'. Baud rate: " + serialPort.baudRate);
    }


}
