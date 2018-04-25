package com.hm.iou.network.interceptor;

import com.hm.iou.network.HttpRequestConfig;
import com.hm.iou.network.R;
import com.hm.iou.network.exception.NetworkConnectionException;
import com.hm.iou.network.exception.ResponseFailException;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hjy on 4/25/18.<br>
 * 对http返回结果进行拦截做额外处理
 */

public class ResponseInterceptor implements Interceptor {

    private HttpRequestConfig mConfig;

    public ResponseInterceptor(HttpRequestConfig config) {
        mConfig = config;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (IOException e) {
            throw new NetworkConnectionException(mConfig.getContext().getString(R.string.net_network_error));
        }

        if(!response.isSuccessful()) {
            throw new ResponseFailException(mConfig.getContext().getString(R.string.net_network_error));
        }
        return response;
    }
}
