package com.hm.iou.network.demo;

import com.hm.iou.network.demo.bean.CheckVersionResBean;
import com.hm.iou.network.demo.bean.UserInfo;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
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

    @POST("/acct/mobileLogin")
    @FormUrlEncoded
    Flowable<ResponseResult<UserInfo>> mobileLogin(@Field("loginName") String userPhone, @Field("queryPswd") String queryPswd);


    @GET("/api/iou/user/v1/checkVersion")
    Flowable<ResponseResult<CheckVersionResBean>> checkVersion();

    @GET
    Flowable<ResponseBody> downFile(@Url String fileUrl);
}