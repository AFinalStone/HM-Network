package com.hm.iou.network;

import android.text.TextUtils;

import com.hm.iou.network.interceptor.RequestInterceptor;
import com.hm.iou.network.interceptor.ResponseInterceptor;

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
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(config.getConnectTimeout(), config.getConnectTimeUnit())
                .readTimeout(config.getReadTimeout(), config.getReadTimeUnit());
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        if (config.isDebug()) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        okHttpClientBuilder.addInterceptor(new RequestInterceptor(config));
        okHttpClientBuilder.addInterceptor(new ResponseInterceptor(config));
        okHttpClientBuilder.addInterceptor(loggingInterceptor);
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