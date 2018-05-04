package com.rongyun.rongyuntest.server;

import com.rongyun.rongyuntest.server.param.LoginParams;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


/**
 * Created by Administrator on 2018/4/12.
 *
 */

 interface RetrofitInterface {
    @POST("user/login")
    Observable<Response<ResponseBody>> login(@Body LoginParams params);

    @POST("user/login")
    Call<ResponseBody> login2(@Body LoginParams params);

    @Streaming
    @GET("https://xiazaiceshia.oss-cn-beijing.aliyuncs.com/tupian.zip")
    Observable<ResponseBody> download();


    @POST
    Observable<ResponseBody> doPost(@Url String url, @Body RequestBody params);

   //图片上传
   @Multipart
   @POST("system/upload")
   Observable<Response<ResponseBody>> upImage(@Part MultipartBody.Part file);

}
