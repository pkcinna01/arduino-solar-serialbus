package com.xmonit.solar.arduino.data;

import lombok.Data;


@Data
public class Status {

    public Integer code;
    public String msg;

    public Status() {}

    public Status(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
