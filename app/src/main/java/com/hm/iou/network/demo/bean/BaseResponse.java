package com.hm.iou.network.demo.bean;

public class BaseResponse<T> {
    private boolean success;
    private int errorCode;
    private String message;
    private int retCode = -1;
    private String retMsg;
    private T data;

    public BaseResponse() {
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getErrorCode() {
        return this.retCode != -1 ? this.retCode : this.errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return this.retCode != -1 ? this.retMsg : this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "success=" + success +
                ", errorCode=" + errorCode +
                ", message='" + message + '\'' +
                ", retCode=" + retCode +
                ", retMsg='" + retMsg + '\'' +
                ", data=" + data +
                '}';
    }
}
