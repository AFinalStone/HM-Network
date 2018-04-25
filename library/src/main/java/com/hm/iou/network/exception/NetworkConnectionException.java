package com.hm.iou.network.exception;

/**
 * Created by hjy on 18/4/25.<br>
 * 网络连接异常
 */

public class NetworkConnectionException extends ApiException {

    public NetworkConnectionException(String message) {
        super(message);
    }

    public NetworkConnectionException(String code, String message) {
        super(code, message);
    }
}
