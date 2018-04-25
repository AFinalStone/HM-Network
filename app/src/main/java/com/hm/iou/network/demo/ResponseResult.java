package com.hm.iou.network.demo;

/**
 * Created by hjy on 18/4/25.<br>
 */

public class ResponseResult<T> {

    private boolean success;
    private String message;
    private T data;
    private int errorCode;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", errorCode=" + errorCode +
                '}';
    }
}
