package com.hm.iou.network.interceptor.file;

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

public class FileResponseInterceptor implements Interceptor {

    private HttpRequestConfig mConfig;
    private ProgressListener mProgressListener;

    public FileResponseInterceptor(HttpRequestConfig config, ProgressListener listener) {
        mConfig = config;
        mProgressListener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response originResponse;
        ProgressResponseBody body;
        try {
            originResponse = chain.proceed(request);
            body = new ProgressResponseBody(originResponse.body(), mProgressListener);
            originResponse = originResponse.newBuilder().body(body).build();
        } catch (IOException e) {
            throw new NetworkConnectionException(mConfig.getContext().getString(R.string.net_network_error));
        }
        if (!originResponse.isSuccessful()) {
            throw new ResponseFailException(mConfig.getContext().getString(R.string.net_network_error));
        }
        return originResponse;
    }
}
