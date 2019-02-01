package com.xmonit.solar.arduino;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.PrintWriter;

public class PrintResponseHandler implements ArduinoResponseHandler {

    PrintWriter pw;

    public PrintResponseHandler() {
        pw = new PrintWriter(System.out);
    }

    public PrintResponseHandler(PrintWriter pw) {
        this.pw = pw;
    }

    @Override
    public void invalidate(ArduinoSerialBus serialBus, Exception ex) {
    }

    @Override
    public void process(ArduinoSerialBus serialBus, JsonNode resp) throws ArduinoException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Object json = mapper.readValue(resp.toString(), Object.class);
            pw.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));
            pw.flush();
        } catch (Exception ex) {
            invalidate(serialBus, ex);
            throw new ArduinoException("Failed writing JSON response.", ex);
        }
    }
}
