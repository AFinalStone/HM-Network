package com.hm.iou.network.exception;

/**
 * Created by hjy on 18/4/25.<br>
 * 响应状态码非200时的异常
 */

public class ResponseFailException extends ApiException {

    public ResponseFailException(String message) {
        super(message);
    }

    public ResponseFailException(String code, String message) {
        super(code, message);
    }

}
