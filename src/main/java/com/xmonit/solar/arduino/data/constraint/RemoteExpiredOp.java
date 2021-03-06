package com.xmonit.solar.arduino.data.constraint;

import lombok.Data;

// Used to lookup a sensor or other value source from a constraint.  Example, you have a max temp constraint and you
// want information about the sensor used to measure the temp.

@Data
public class RemoteExpiredOp {

    public String type;
    public Boolean expired;

    public RemoteExpiredOp() {}

    public RemoteExpiredOp(String type, Boolean bExpired) {
        this.type = type;
        this.expired = bExpired;
    }

}
