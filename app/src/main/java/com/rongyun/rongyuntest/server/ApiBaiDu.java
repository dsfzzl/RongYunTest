package com.rongyun.rongyuntest.server;

import com.rongyun.rongyuntest.bean.TokenBean;
import com.rongyun.rongyuntest.server.param.ParamsJianCheLian;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Administrator on 2018/5/10.
 */

public interface ApiBaiDu {


    @POST("oauth/2.0/token")
    Observable<Response<TokenBean>> getToken(@QueryMap HashMap<String,String> params);

    @Headers("Content-Type: application/json")
    @POST("rest/2.0/face/v3/detect")
    Observable<Response<ResponseBody>> jianCheLianShuXin(@Query("access_token") String accessToken , @Body ParamsJianCheLian params);

}
