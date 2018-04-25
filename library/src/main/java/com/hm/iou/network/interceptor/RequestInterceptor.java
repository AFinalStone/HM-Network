package com.hm.iou.network.interceptor;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.hm.iou.network.HttpRequestConfig;
import com.hm.iou.network.R;
import com.hm.iou.network.exception.NetworkConnectionException;
import com.hm.iou.network.exception.NoNetworkException;
import com.hm.iou.network.utils.StringUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hjy on 18/4/25.<br>
 *
 * 添加公用请求头信息拦截器
 */

public class RequestInterceptor implements Interceptor {

    private HttpRequestConfig mConfig;

    public RequestInterceptor(HttpRequestConfig config) {
        mConfig = config;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        //如果没有联网, 则直接抛出异常
        if(!isConnected(mConfig.getContext())) {
            throw new NoNetworkException(mConfig.getContext().getString(R.string.net_no_network));
        }
        Request.Builder builder = chain.request()
                .newBuilder()
                .addHeader("Accept-Encoding", "gzip, deflate")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Connection", "keep-alive")
                .addHeader("Accept", "application/json")
                .addHeader("deviceId", mConfig.getDeviceId())
                .addHeader("rptTime", System.currentTimeMillis() + "")
                .addHeader("operKind", "CUSTOMER")
                .addHeader("id", StringUtil.getUnnullString(mConfig.getUserId()))
                .addHeader("token", StringUtil.getUnnullString(mConfig.getToken()))
                .addHeader("umDeviceToken", "")
                .addHeader("osType", StringUtil.getUnnullString(mConfig.getOsType()))
                .addHeader("osVer", StringUtil.getUnnullString(mConfig.getOsVersion()))
                .addHeader("appVer", StringUtil.getUnnullString(mConfig.getAppVersion()))
                .addHeader("rptGpsX", StringUtil.getUnnullString(mConfig.getGpsX()))
                .addHeader("rptGpsY", StringUtil.getUnnullString(mConfig.getGpsY()))
                .addHeader("rptIp", "");

        Request request = builder.build();
        try {
            return chain.proceed(request);
        } catch (IOException e) {
            throw new NetworkConnectionException(mConfig.getContext().getString(R.string.net_network_error));
        }
    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivity) {
            try {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (null != info && info.isConnected()) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return true;
            }
        }
        return false;
    }

}