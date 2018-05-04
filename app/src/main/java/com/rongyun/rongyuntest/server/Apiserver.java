package com.rongyun.rongyuntest.server;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2018/4/20.
 */

public interface Apiserver {
    String CACHE_CONTROL_NETWORK = "Cache-Control: public, max-age=3600";

    @GET("nc/article/{type}/{id}/{startPage}-20.html")
    Observable<Response<ResponseBody>>  getNewsList(@Path("type") String type, @Path("id") String id,
                                                    @Path("startPage") int startPage);


    @GET("http://chapter2.zhuishushenqi.com/chapter/{url}")
    Observable<ResponseBody> getChapterInfoPackage(@Path("url") String url);
}
