package com.xmonit.solar.arduino;

import java.util.Optional;

public class ArduinoException extends Exception {

    public Optional<Integer> reasonCode = Optional.empty();

    public ArduinoException(String msg, Exception cause) {

        super(msg,cause);
    }

    public ArduinoException(String msg, int reasonCode) {
        this(msg,null);
        this.reasonCode = Optional.of(reasonCode);
    }

    @Override
    public String getMessage() {
        return reasonCode.map(c-> "Error(" + c + "): ").orElse("") + super.getMessage();
    }
}
