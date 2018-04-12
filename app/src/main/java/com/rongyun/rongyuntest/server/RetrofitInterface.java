package com.rongyun.rongyuntest.server;

import com.rongyun.rongyuntest.server.param.LoginParams;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;



/**
 * Created by Administrator on 2018/4/12.
 */

public interface RetrofitInterface {
    @POST("user/login")
    Observable<ResponseBody> login(@Body LoginParams params);

    @POST("user/login")
    Call<ResponseBody> login2(@Body LoginParams params);

}
