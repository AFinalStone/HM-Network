package com.hm.iou.network.interceptor;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;

import com.hm.iou.network.HttpRequestConfig;
import com.hm.iou.network.R;
import com.hm.iou.network.exception.DecryptException;
import com.hm.iou.network.exception.NetworkConnectionException;
import com.hm.iou.network.exception.NoNetworkException;
import com.hm.iou.network.exception.ResponseFailException;
import com.hm.iou.network.utils.AesUtil;
import com.hm.iou.network.utils.RsaUtil;
import com.hm.iou.network.utils.StringUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;

/**
 * Created by hjy on 18/4/25.<br>
 * <p>
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
        if (!isConnected(mConfig.getContext())) {
            throw new NoNetworkException(mConfig.getContext().getString(R.string.net_no_network));
        }
        Request originalRequest = chain.request();
        Request.Builder builder = originalRequest
                .newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("deviceId", mConfig.getDeviceId())
                .addHeader("rptTime", System.currentTimeMillis() + "")
                .addHeader("operKind", "CUSTOMER")
                .addHeader("id", StringUtil.getUnnullString(mConfig.getUserId()))
                .addHeader("token", StringUtil.getUnnullString(mConfig.getToken()))
                .addHeader("Authorization", StringUtil.getUnnullString(mConfig.getAuthorization()))
                .addHeader("osType", "android")
                .addHeader("osVer", Build.VERSION.RELEASE)
                .addHeader("appChannel", StringUtil.getUnnullString(mConfig.getAppChannel()))
                .addHeader("deviceType", Build.BRAND + " " + Build.MODEL)
                .addHeader("appVer", StringUtil.getUnnullString(mConfig.getAppVersion()))
                .addHeader("rptGpsX", TextUtils.isEmpty(mConfig.getGpsX()) ? "0" : mConfig.getGpsX())
                .addHeader("rptGpsY", TextUtils.isEmpty(mConfig.getGpsY()) ? "0" : mConfig.getGpsY())
                .addHeader("bundleId", StringUtil.getUnnullString(mConfig.getBundleId()));

        if (!TextUtils.isEmpty(mConfig.getUmDeviceToken())) {
            builder.addHeader("umDeviceToken", mConfig.getUmDeviceToken());
        }

        //根据请求头标记，来判断是否需要进行加密传输
        String isEncrypt = originalRequest.header("encrypt");
        String aesEncryptKey = AesUtil.generateRandomKey();
        String encryptSigner = RsaUtil.encryptByPublicKey(aesEncryptKey, mConfig.getRsaPubKey());
        builder.addHeader("random", StringUtil.getUnnullString(encryptSigner));
        builder.addHeader("pubVersion", StringUtil.getUnnullString(mConfig.getRsaPubVersion()));
        //对需要加密的接口，先通过头信息"encrypt=1"来标识是否要加密
        if ("1".equals(isEncrypt)) {
            //只对 POST 请求，除文件上传之外的 body 体进行加密
            String contentType = originalRequest.header("Content-Type");
            if (originalRequest.method().toUpperCase().equals("POST") &&
                    !(contentType != null && contentType.contains("multipart/form-data"))) {
                Buffer buffer = new Buffer();
                originalRequest.body().writeTo(buffer);
                String reqStr = buffer.readString(Charset.forName("UTF-8"));
                reqStr = AesUtil.encryptEcb(reqStr, aesEncryptKey);
                builder.post(RequestBody.create(MediaType.parse("application/json"), reqStr));
            }
        }
        Request request = builder.build();

        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            throw new NetworkConnectionException(mConfig.getContext().getString(R.string.net_network_error));
        }
        if (!response.isSuccessful()) {
            throw new ResponseFailException(mConfig.getContext().getString(R.string.net_network_error));
        }

        //根据 Response 里的头信息字段，来判断是否需要对 Response 里的内容进行解密
        if (!TextUtils.isEmpty(aesEncryptKey) && "1".equals(response.header("encrypt"))) {
            Response.Builder responseBuilder = response.newBuilder();
            String resp = response.body().source().readString(Charset.forName("UTF-8"));
            resp = AesUtil.decryptECB(resp, aesEncryptKey);
            //如果解密失败，则抛出异常
            if (TextUtils.isEmpty(resp)) {
                throw new DecryptException(mConfig.getContext().getString(R.string.net_decrypt_error));
            }
            responseBuilder.body(new RealResponseBody(response.headers(), Okio.buffer(buildNewSource(resp))));
            return responseBuilder.build();
        }
        return response;
    }

    private Source buildNewSource(String resp) {
        return Okio.source(new ByteArrayInputStream(resp.getBytes()));
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

    public final class RealResponseBody extends ResponseBody {

        private final Headers headers;
        private final BufferedSource source;

        public RealResponseBody(Headers headers, BufferedSource source) {
            this.headers = headers;
            this.source = source;
        }

        @Override public MediaType contentType() {
            String contentType = headers.get("Content-Type");
            return contentType != null ? MediaType.parse(contentType) : null;
        }

        @Override public long contentLength() {
            return stringToLong(headers.get("Content-Length"));
        }

        private long stringToLong(String s) {
            if (s == null) return -1;
            try {
                return Long.parseLong(s);
            } catch (NumberFormatException e) {
                return -1;
            }
        }

        @Override public BufferedSource source() {
            return source;
        }
    }

}