package com.xmonit.solar.arduino;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;

public abstract class ArduinoSerialPort {

	public static final int DATABITS_7 = 7;

	public static final int DATABITS_8 = 8;
	private static final Logger logger = LoggerFactory.getLogger(ArduinoSerialPort.class);

	public static final int PARITY_EVEN = 2;
	public static final int PARITY_NONE = 0;

	public static final int PARITY_ODD = 1;
	public static final int STOPBITS_1 = 1;
	public static final int STOPBITS_2 = 2;

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

	// public int parity = PARITY_ODD;
	public int baudRate = 38400;
	public int dataBits = DATABITS_8;
	public int parity = PARITY_NONE;
	public int stopBits = STOPBITS_1;

	public abstract void close();

	public abstract InputStream getInputStream() throws IOException;

	public abstract OutputStream getOutputStream() throws IOException;

	public abstract String getPortName();

	public abstract boolean isOpen();

	public abstract void open(String portNamePattern) throws ArduinoException;

	public boolean portNameMatches(String portNamePattern) {
		String portName = getPortName();
		return portName != null && portName.matches(portNamePattern);
	}

	public String readUntilLine(String lineRegEx, int timeoutMs) throws IOException {
		StringBuilder sb = new StringBuilder();
		StringBuilder lineBuilder = new StringBuilder();
		InputStream inputStream = getInputStream();
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
		OutputStream outputStream = getOutputStream();
		outputStream.write(data.getBytes());
		outputStream.flush();
	}

}
