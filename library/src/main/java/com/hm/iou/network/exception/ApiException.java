package com.hm.iou.network.exception;

import java.io.IOException;

/**
 * Created by hjy on 18/4/25.<br>
 */

public class ApiException extends IOException {

    private String code;

    public ApiException(String message) {
        this(null, message);
    }

    public ApiException(String code ,String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}