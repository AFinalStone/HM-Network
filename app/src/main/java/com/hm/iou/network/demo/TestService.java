package com.hm.iou.network.demo;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by hjy on 18/4/25.<br>
 */

public interface TestService {

    @GET("/sys/appInit")
    Flowable<ResponseResult<APPInitDataBean>> appInit(@Query("osType") String osType
            , @Query("osVer") String osVer, @Query("ver") String ver);

}
