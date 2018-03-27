package com.xmonit.solar.serialbus;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.PrintWriter;

public class PrintResponseHandler implements SerialBusResponseProcessor {

    PrintWriter pw;

    public PrintResponseHandler() {
        pw = new PrintWriter(System.out);
    }

    public PrintResponseHandler(PrintWriter pw) {
        this.pw = pw;
    }

    @Override
    public void invalidate(SerialBus serialBus, Exception ex) {
    }

    @Override
    public void process(SerialBus serialBus, JsonNode resp) throws SerialBusException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Object json = mapper.readValue(resp.toString(), Object.class);
            pw.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));
            pw.flush();
        } catch (Exception ex) {
            invalidate(serialBus, ex);
            throw new SerialBusException("Failed writing JSON response.", ex);
        }
    }
}
