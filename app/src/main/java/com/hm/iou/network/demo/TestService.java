package com.hm.iou.network.demo;

import com.hm.iou.network.demo.bean.BaseResponse;
import com.hm.iou.network.demo.bean.CheckVersionResBean;
import com.hm.iou.network.demo.bean.MobileLoginReqBean;
import com.hm.iou.network.demo.bean.TestEncryptReqBean;
import com.hm.iou.network.demo.bean.UserInfo;

import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by hjy on 18/4/25.<br>
 */

public interface TestService {

    @GET("/sys/appInit")
    Flowable<ResponseResult<APPInitDataBean>> appInit(@Query("osType") String osType
            , @Query("osVer") String osVer, @Query("ver") String ver);

    @GET("/api/iou/user/v1/checkVersion")
    Flowable<ResponseResult<CheckVersionResBean>> checkVersion(@HeaderMap Map<String, String> header);

    @GET
    Flowable<ResponseBody> downFile(@Url String fileUrl);

    @Headers({
            "encrypt: 1"
    })
    @POST("/api/iou/user/v1/mobileLogin")
    Flowable<BaseResponse<UserInfo>> mobileLogin(@Body MobileLoginReqBean mobileLoginReqBean);

    @Headers({
            "encrypt: 1"
    })
    @POST("http://192.168.1.129:9100/p/v1/encryptTest")
    Flowable<BaseResponse<String>> testEncrypt(@Body TestEncryptReqBean reqBean);
}