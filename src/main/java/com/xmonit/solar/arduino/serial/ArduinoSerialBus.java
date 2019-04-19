package com.xmonit.solar.arduino.serial;

import com.xmonit.solar.arduino.ArduinoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;


public class ArduinoSerialBus {

    private static final Logger logger = LoggerFactory.getLogger(ArduinoSerialBus.class);

    protected static AtomicInteger requestId = new AtomicInteger();

    static long checksum(byte[] bytes) {
        return checksum(bytes, bytes.length);
    }

    static long checksum(byte[] bytes, int byteCnt) {
        long computedChecksum = 0;
        for (int i = 0; i < byteCnt; i++) {
            computedChecksum += bytes[i];
        }
        return computedChecksum;
    }

    public Integer id;
    public String name;
    public ArduinoSerialPort serialPort;


    public ArduinoSerialBus(ArduinoSerialPort serialPort) {
        this.serialPort = serialPort;
    }

    public synchronized void close() {

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


    public String execute(String cmd) throws ArduinoException {
        return execute(cmd, null,true);
    }


    protected synchronized String execute(String command, Integer explicitReqId, boolean validate ) throws ArduinoException {

        try {
            if ( !serialPort.isOpen() ) {
                serialPort.open();
            }
            int reqId;
            if ( explicitReqId != null ) {
                if ( explicitReqId > 0x7FFF ) {
                    throw new ArduinoException("Request ID exceeeds maximum size: " + explicitReqId, -1);
                }
                reqId = explicitReqId;
            } else {
                if ( requestId.incrementAndGet() < 0 ) { // >= 0x7FFF ) {
                    requestId.set(1);
                }
                reqId = requestId.get();
            }

            String data = command + "|" + reqId + "\n";
            if ( !command.equalsIgnoreCase("get,sensors") ) {
                logger.info(data);
            }
            send(data);

            String strResp = readUntilLine("^#END:"+reqId+":[:0-9]*#$",30000);
            InputStream inputStream = new ByteArrayInputStream(strResp.getBytes());

            return extractResponse(inputStream,reqId,validate);
        } catch (ArduinoException ex) {
            logger.error("ArduinoSerialBus.execute('" + command + "') failed.");
            close();
            throw ex;
        } catch (Exception ex) {
            close();
            throw new ArduinoException("ArduinoSerialBus.execute('" + command + "') failed.", ex);
        }
    }

    private String extractResponse(InputStream respInputStream, int reqId, boolean validate) throws Exception {

        String resp = "";
        try {

            StringBuilder sb = new StringBuilder();
            boolean bRespStarted = false;
            boolean bFoundEnd = false;
            boolean advancedToNextPound = false;

            while (true) {
                String line = readLine(respInputStream);

                if (advancedToNextPound) {
                    advancedToNextPound = false;
                    line = '#' + line;
                }
                if (line.matches("#END[:0-9]*#\n?")) {
                    logger.debug("Found #END in line: " + line);

                    resp = sb.toString();
                    bFoundEnd = true;
                    String[] tokens = line.replace("#","").split(":");
                    String strErrMsg = null;
                    if ( tokens.length != 6 ) {
                        strErrMsg = "Invalid #END# footer. Expected '#END:{req id}:{resp length}:{checksum}:{timeSetIndicator}:{deviceId}#'. Found: "
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
                            long computedChecksum = ArduinoSerialBus.checksum(resp.getBytes(),respLen);
                            if ( checksum != computedChecksum ) {
                                strErrMsg = "Request #" + reqId + " response footer contains checksum=" + checksum
                                        + " but actual checksum=" + computedChecksum;
                            }
                        }
                    }
                    if ( strErrMsg != null ) {
                        if ( validate ) {
                            throw new ArduinoException(strErrMsg, 97);
                        } else {
                            logger.warn("Ignoring validation error: '" + strErrMsg + "'");
                        }
                    }
                    break;
                } else if (line.matches(".*#BEGIN:"+reqId+"#\n?")) {
                    logger.debug("Found #BEGIN in line: " + line);
                    if (sb.length() > 0) {
                        logger.warn("Found another #BEGIN:" + reqId + "#.  Ignoring buffered data: " + sb.toString());
                        sb.setLength(0);
                    }
                    if (!line.matches("#BEGIN[:0-9]*#\n?")) {
                        String strIgnored = line.replaceAll("#BEGIN[:0-9]*#","");
                        logger.warn("Ignoring unexpected data before #BEGIN#: '" + strIgnored + "'");
                        line = line.substring(strIgnored.length());
                    }
                    bRespStarted = true;
                } else if (bRespStarted) {
                    if ( line.matches("^[\n]$")){
                        //logger.warn("Ignoring serial response data because all newlines... this is hack for mega 2560 quirk");
                    } else {
                        sb.append(line);
                    }
                } else {
                    if (!line.trim().isEmpty()) {
                        logger.warn("Unexpected input before #BEGIN:" + reqId + "#");
                        StringBuilder ignoredText = new StringBuilder(line);
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
            logger.error(resp);
            throw ex;
        } catch (Exception ex) {
            logger.error(resp);
            String msg = "Failed reading or parsing data from Arduino";
            throw new ArduinoException(msg, ex);
        }
    }


    public String getPortName() {
        return serialPort.getPortName();
    }


    public void init(String name, Integer id) {
        this.name = name;
        this.id = id;
    }


    public boolean isOpen() {
        return serialPort.isOpen();
    }


    private void open() throws ArduinoException {

        //serialPort.baudRate = config.getBaudRate();
        serialPort.open();
        logger.info("USB port opened for '" + serialPort.getPortName() + "'. Baud rate: " + serialPort.baudRate);
    }


    /**
     * Workaround to buffered reader behaving differently between different serial port implementations
     * and arduino board types
     *
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
            sb.append((char)c);
            if (c == '\n') {
                if ( sb.length() == 0 ) {
                    logger.warn("empty input from arduino");
                }
                break;
            }
        }
        //logger.debug(sb.toString());
        return sb.toString();
    }


    public String readUntilLine(String lineRegEx, int timeoutMs) throws IOException {
        StringBuilder sb = new StringBuilder();
        StringBuilder lineBuilder = new StringBuilder();
        InputStream inputStream = serialPort.getInputStream();
        Pattern linePattern = Pattern.compile(lineRegEx);
        long startMs = System.currentTimeMillis();
        final int maxLen = 10 * 1024;
        try {
            while (true) {

                if (sb.length() >= maxLen) {
                    throw new IOException("Exceeded max buffer size (" + maxLen + ") while reading arduino response");
                }
                if (System.currentTimeMillis() - startMs > timeoutMs) {
                    logger.info("Timed out. Abandoning data: " + sb.toString());
                    throw new IOException("Timed out waiting for arduino input matching '" + lineRegEx + "' (exceeded "
                            + timeoutMs + " ms)");
                }

                int c = inputStream.read();

                if (c == 0 || c == '\r') {
                    continue;
                }

                if (c == '\n') {
                    String line = lineBuilder.toString();
                    sb.append(line);
                    lineBuilder.setLength(0);
                    if (linePattern.matcher(line).matches()) {
                        break;
                    } else {
                        sb.append((char) c);
                    }
                } else {
                    lineBuilder.append((char) c);
                }
            }
        } finally {
            inputStream.close();
        }
        return sb.toString();
    }

    public void send(String data) throws IOException {
        OutputStream outputStream = serialPort.getOutputStream();
        outputStream.write(data.getBytes());
        outputStream.flush();
    }


}
