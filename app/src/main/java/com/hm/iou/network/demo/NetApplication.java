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

    /*    String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCQpKdfrwBtR4KnBIQSJkIFh3jlF+J3VsOTDAua" +
                "mqDXmVzacsvSpYQeSVh3gj/t8dti0r5Cs2J13SPNJKr5N9Y93GrEV25hl7tBw6xXf0zc7Sn1WcZh" +
                "y3IP6PG/U+eAI9u2gwYoAHIMN+AdSbztgAYbzC08GOFJcwWUDcesoJf2ywIDAQAB";
      */

        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCq6i/S/uZA4Yy83XqrNzI00HaSW7U9W8W9hevRNAihAbsxy8kzBMDzadJ1Kyj+r2vEeA0P0mSRnMIgyNg3lRwxJw9T0PyrzQmag9w23u9zTViPYdwQ9F16QFhGI0g0Xx4G1jr5IWn+qrmA9AsVvKmq1A/aGyjPmBwsu5/DryovuwIDAQAB";

        HttpRequestConfig config = new HttpRequestConfig.Builder(this)
                .setDebug(true)
                .setAppChannel("yyb")
                .setAppVersion("1.0.2")
                .setDeviceId("123abc123")
//                .setBaseUrl("http://192.168.1.217")
//                .setBaseUrl("http://dev.54jietiao.com")
                  .setBaseUrl("http://192.168.1.179:8071")
//                .setRsaPubVersion("RSAV1.0.0")
//                .setRsaPubKey(publicKey)
                .build();
        HttpReqManager.init(config);

        HttpReqManager.getInstance().setRsaKey("20190923", publicKey);
    }

}
