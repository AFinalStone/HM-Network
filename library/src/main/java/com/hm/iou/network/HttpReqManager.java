package com.hm.iou.network;


import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;

/**
 * Retrofit统一管理
 */
public class HttpReqManager {

    private static HttpReqManager INSTANCE = null;

    /**
     * 初始化
     *
     */
    public static void init(HttpRequestConfig config) {
        if(config == null) {
            throw new IllegalArgumentException("RequestConfig cannot be null.");
        }
        INSTANCE = new HttpReqManager(config);
    }

    /**
     * 获取单例对象
     *
     * @return
     */
    public static HttpReqManager getInstance() {
        if(INSTANCE == null) {
            throw new IllegalArgumentException("Must call init() method before call this.");
        }
        return INSTANCE;
    }

    private HttpRequestConfig mConfig;
    private Map<String, Object> mServiceMap;

    private Retrofit mRetrofitClient;
    private Map<String, Retrofit> mRetrofitMap;

    private HttpReqManager(HttpRequestConfig config) {
        mConfig = config;
        mServiceMap = new HashMap<String, Object>();
        mRetrofitMap = new HashMap<>();
    }

    public HttpRequestConfig getRequestConfig() {
        return mConfig;
    }

    public void setUserId(String userId) {
        mConfig.setUserId(userId);
    }

    public void setToken(String token) {
        mConfig.setToken(token);
    }

    /**
     * 更新设置经纬度
     *
     * @param gpsX
     * @param gpsY
     */
    public void setLocation(String gpsX, String gpsY) {
        mConfig.setGpsX(gpsX);
        mConfig.setGpsY(gpsY);
    }

    /**
     * 创建retrofit api service实例
     *
     * @param serviceClass retrofit service
     * @param <S>
     * @return
     */
    public <S> S getService(Class<S> serviceClass) {
        if(mServiceMap.containsKey(serviceClass.getName())) {
            return (S) mServiceMap.get(serviceClass.getName());
        } else {
            S obj = createService(serviceClass);
            //缓存起来
            mServiceMap.put(serviceClass.getName(), obj);
            return obj;
        }
    }

    /**
     * 针对api出现多个不同baseUrl时采用该方法
     * TODO api不应该出现多个不同的域名，需要由服务端来规范。
     *
     * @param serviceClass
     * @param baseUrl
     * @param <S>
     * @return
     */
    public <S> S getService(Class<S> serviceClass, String baseUrl) {
        if (TextUtils.isEmpty(baseUrl)) {
            return getService(serviceClass);
        }
        String key = serviceClass.getName() + baseUrl;
        if(mServiceMap.containsKey(key)) {
            return (S) mServiceMap.get(key);
        } else {
            S obj = createService(serviceClass, baseUrl);
            //缓存起来
            mServiceMap.put(key, obj);
            return obj;
        }
    }

    private <S> S createService(Class<S> serviceClass) {
        if(mRetrofitClient == null) {
            mRetrofitClient = RetrofitFactory.createRetrofit(mConfig);
        }
        return mRetrofitClient.create(serviceClass);
    }

    private <S> S createService(Class<S> serviceClass, String baseUrl) {
        Retrofit retrofit = mRetrofitMap.get(baseUrl);
        if(retrofit == null) {
            retrofit = RetrofitFactory.createRetrofit(mConfig, baseUrl);
            mRetrofitMap.put(baseUrl, retrofit);
        }
        return retrofit.create(serviceClass);
    }

    public void resetConfig(String baseUrl) {
        mServiceMap.clear();
        mRetrofitClient = null;
        mConfig.setBaseUrl(baseUrl);
    }

}