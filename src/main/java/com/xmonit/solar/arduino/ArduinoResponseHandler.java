package com.xmonit.solar.arduino;


import com.fasterxml.jackson.databind.JsonNode;

public interface ArduinoResponseHandler {

    public void process(ArduinoSerialBus serialBus, JsonNode resp) throws ArduinoException;

    /**
     * Mechanism to set prometheus stats to NaN or something to show metrics
     * are stale due to reading or parsing issues
     */
    public void invalidate(ArduinoSerialBus serialBus, Exception ex);
}
