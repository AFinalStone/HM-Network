package com.hm.iou.network.demo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23)
public class ApiTest {

    @Before
    public void init() {

    }

    @Test
    public void testInitApi() throws Exception {

        System.out.println("hello world");

/*        HttpRequestConfig config = new HttpRequestConfig.Builder(ShadowApplication.getInstance().getApplicationContext())
                .setDebug(true)
                .setOsType("android")
                .setOsVersion("19")
                .setAppVersion("1.0.2")
                .setDeviceId("123abc123")
                .build();
        HttpReqManager.init(config);*/

    /*    HttpReqManager.getInstance().getService(TestService.class)
                .appInit("android", "19", "1.0.2")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseResult<String>>() {
                    @Override
                    public void accept(ResponseResult<String> stringResponseResult) throws Exception {
                        System.out.println("success...");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });*/
    }

}