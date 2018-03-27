package com.xmonit.solar.serialbus;

import org.codehaus.jackson.JsonNode;

public interface SerialBusResponseProcessor {

    public void process(SerialBus serialBus, JsonNode resp) throws SerialBusException;

    /**
     * Mechanism to set prometheus stats to NaN or something to show metrics
     * are stale due to reading or parsing issues
     */
    public void invalidate(SerialBus serialBus, Exception ex);
}
