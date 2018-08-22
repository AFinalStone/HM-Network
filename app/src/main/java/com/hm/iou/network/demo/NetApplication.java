package com.hm.iou.network.demo;

import android.app.Application;

import com.hm.iou.network.HttpReqManager;
import com.hm.iou.network.HttpRequestConfig;

/**
 * Created by hjy on 18/4/25.<br>
 */

public class NetApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initNetwork();
    }

    private void initNetwork() {
        System.out.println("init-----------");
        HttpRequestConfig config = new HttpRequestConfig.Builder(this)
                .setDebug(true)
                .setAppChannel("yyb")
                .setAppVersion("1.0.2")
                .setDeviceId("123abc123")
//                .setBaseUrl("http://192.168.1.217")
                .setBaseUrl("http://api.54jietiao.com")
                .build();
        HttpReqManager.init(config);
    }

}
