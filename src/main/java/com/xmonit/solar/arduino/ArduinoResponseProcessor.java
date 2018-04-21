package com.xmonit.solar.arduino;

import org.codehaus.jackson.JsonNode;

public interface ArduinoResponseProcessor {

    public void process(ArduinoSerialBus serialBus, JsonNode resp) throws ArduinoException;

    /**
     * Mechanism to set prometheus stats to NaN or something to show metrics
     * are stale due to reading or parsing issues
     */
    public void invalidate(ArduinoSerialBus serialBus, Exception ex);
}
