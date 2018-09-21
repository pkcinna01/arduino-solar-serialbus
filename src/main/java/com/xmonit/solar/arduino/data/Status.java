package com.xmonit.solar.arduino.data;

import lombok.Data;

@Data
public class Status {
    public String msg = "";
    public Integer code = 0;

    public void copy(Status src) throws Exception {

        if (src == null) {
            invalidate();
        } else {
            msg = src.msg;
            code = src.code;
        }
    }

    public void invalidate() {
        msg = null;
        code = null;
    }
}
