package com.xmonit.solar.serialbus;

import java.util.Optional;

public class SerialBusException extends Exception {

    public Optional<Integer> reasonCode = Optional.empty();

    public SerialBusException(String msg, Exception cause) {

        super(msg,cause);
    }

    public SerialBusException(String msg, int reasonCode) {
        this(msg,null);
        this.reasonCode = Optional.of(reasonCode);
    }

    @Override
    public String getMessage() {
        return reasonCode.map(c-> "Error(" + c + "): ").orElse("") + super.getMessage();
    }
}
