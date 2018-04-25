package com.hm.iou.network.exception;

/**
 * Created by hjy on 18/4/25.<br>
 * 无网络连接异常，例如WIFI、4G没有开启
 */

public class NoNetworkException extends ApiException {

    public NoNetworkException(String message) {
        super(message);
    }

    public NoNetworkException(String code, String message) {
        super(code, message);
    }

}
