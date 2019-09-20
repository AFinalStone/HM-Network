package com.hm.iou.network.exception

/**
 * Created by hjy on 2019/9/20
 *
 * 加解密异常
 */
class DecryptException(code: String?, msg: String?) : ApiException(code, msg) {

    constructor(msg: String?): this(null, msg)

}