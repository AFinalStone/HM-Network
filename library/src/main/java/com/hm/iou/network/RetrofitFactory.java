package com.hm.iou.network;

import android.text.TextUtils;

import com.hm.iou.network.interceptor.RequestInterceptor;
import com.hm.iou.network.interceptor.file.FileResponseInterceptor;
import com.hm.iou.network.interceptor.file.ProgressListener;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hjy on 18/4/25.<br>
 */

public class RetrofitFactory {

    public static Retrofit createRetrofit(HttpRequestConfig config) {
        return createRetrofit(config, "");
    }

    /**
     * 创建Retrofit对象
     *
     * @param config
     * @param baseUrl 指定的baseUrl，如果传空值，则采用config里配置的baseUrl
     * @return
     */
    public static Retrofit createRetrofit(HttpRequestConfig config, String baseUrl) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(config.getConnectTimeout(), config.getConnectTimeUnit())
                .readTimeout(config.getReadTimeout(), config.getReadTimeUnit());
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClientBuilder.addInterceptor(new RequestInterceptor(config));
        if (config.isDebug()) {
            okHttpClientBuilder.addInterceptor(loggingInterceptor);
        }
        okHttpClientBuilder.retryOnConnectionFailure(true);

        OkHttpClient okHttpClient = okHttpClientBuilder.build();
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        if (!TextUtils.isEmpty(baseUrl)) {
            retrofitBuilder.baseUrl(baseUrl);
        } else {
            if (!TextUtils.isEmpty(config.getBaseUrl())) {
                retrofitBuilder.baseUrl(config.getBaseUrl());
            }
        }
        Retrofit retrofit = retrofitBuilder.build();
        return retrofit;
    }

    /**
     * 创建Retrofit对象
     *
     * @param config
     * @return
     */
    public static Retrofit createRetrofit(HttpRequestConfig config, ProgressListener progressListener) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(config.getConnectTimeout(), config.getConnectTimeUnit())
                .readTimeout(config.getReadTimeout(), config.getReadTimeUnit());
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        okHttpClientBuilder.addInterceptor(new RequestInterceptor(config));
        okHttpClientBuilder.addInterceptor(new FileResponseInterceptor(config, progressListener));
        if (config.isDebug()) {
            okHttpClientBuilder.addInterceptor(loggingInterceptor);
        }
        okHttpClientBuilder.retryOnConnectionFailure(true);

        OkHttpClient okHttpClient = okHttpClientBuilder.build();
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        if (!TextUtils.isEmpty(config.getBaseUrl())) {
            retrofitBuilder.baseUrl(config.getBaseUrl());
        }
        Retrofit retrofit = retrofitBuilder.build();
        return retrofit;
    }
}