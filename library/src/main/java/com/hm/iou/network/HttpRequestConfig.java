package com.hm.iou.network;

import android.content.Context;
import android.text.TextUtils;

import java.util.concurrent.TimeUnit;

/**
 * Created by hjy on 18/4/25.<br>
 */

public class HttpRequestConfig {

    private Context context;
    private boolean debug;
    private String baseUrl;
    private String deviceId;
    private String appChannel;
    private String appVersion;
    private String authorization;//信用人生游戏单独使用的字段

    private long readTimeout;
    private TimeUnit readTimeUnit;
    private long connectTimeout;
    private TimeUnit connectTimeUnit;

    private String umDeviceToken;
    private String userId;
    private String token;
    private String gpsX;
    private String gpsY;

    private String bundleId;

    public HttpRequestConfig(Builder builder) {
        context = builder.context;
        baseUrl = builder.baseUrl;
        debug = builder.debug;
        readTimeout = builder.readTimeout;
        readTimeUnit = builder.readTimeUnit;
        connectTimeout = builder.connectTimeout;
        connectTimeUnit = builder.connectTimeUnit;
        deviceId = builder.deviceId;
        appChannel = builder.appChannel;
        userId = builder.userId;
        token = builder.token;
        authorization = builder.authorization;
        appVersion = builder.appVersion;
        umDeviceToken = builder.umDeviceToken;
        bundleId = context.getPackageName();
    }

    public Context getContext() {
        return context;
    }

    public boolean isDebug() {
        return debug;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getAppChannel() {
        return appChannel;
    }

    public String getUmDeviceToken() {
        return umDeviceToken;
    }

    public String getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getGpsX() {
        return gpsX;
    }

    public String getGpsY() {
        return gpsY;
    }

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public TimeUnit getConnectTimeUnit() {
        return connectTimeUnit;
    }

    public long getReadTimeout() {
        return readTimeout;
    }

    public TimeUnit getReadTimeUnit() {
        return readTimeUnit;
    }

    public void setUmDeviceToken(String umDeviceToken) {
        this.umDeviceToken = umDeviceToken;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setGpsX(String gpsX) {
        this.gpsX = gpsX;
    }

    public void setGpsY(String gpsY) {
        this.gpsY = gpsY;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String getBundleId() {
        return bundleId;
    }

    public static class Builder {

        private Context context;
        private boolean debug;
        private String deviceId;
        private String appChannel;
        private String baseUrl;
        private String appVersion;
        private String authorization;//信用人生游戏单独使用的字段

        private long readTimeout;
        private TimeUnit readTimeUnit;
        private long connectTimeout;
        private TimeUnit connectTimeUnit;

        private String userId;
        private String token;
        private String umDeviceToken;

        public Builder(Context context) {
            this.context = context.getApplicationContext();
        }

        public Builder setDebug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public Builder setDeviceId(String deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public Builder setAppChannel(String appChannel) {
            this.appChannel = appChannel;
            return this;
        }

        public Builder setReadTimeout(long timeout, TimeUnit timeUnit) {
            this.readTimeout = timeout;
            this.readTimeUnit = timeUnit;
            return this;
        }

        public Builder setConnectTimeout(long timeout, TimeUnit timeUnit) {
            this.connectTimeout = timeout;
            this.connectTimeUnit = timeUnit;
            return this;
        }

        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        public Builder setAuthorization(String authorization) {
            this.authorization = authorization;
            return this;
        }

        public Builder setAppVersion(String appVersion) {
            this.appVersion = appVersion;
            return this;
        }

        public Builder setUmDeviceToken(String umDeviceToken) {
            this.umDeviceToken = umDeviceToken;
            return this;
        }

        public HttpRequestConfig build() {
            initEmptyInitialValues();
            return new HttpRequestConfig(this);
        }

        private void initEmptyInitialValues() {
            if (readTimeout <= 0) {
                readTimeout = 20;
                readTimeUnit = TimeUnit.SECONDS;
            }
            if (connectTimeout <= 0) {
                connectTimeout = 20;
                connectTimeUnit = TimeUnit.SECONDS;
            }
            if (TextUtils.isEmpty(this.baseUrl)) {
                throw new IllegalArgumentException("The base url should not be null.");
            }
        }

    }

}