package com.rongyun.rongyuntest.server.interceptor;

import android.content.Context;

import com.rongyun.rongyuntest.utils.NetUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/4/18.
 */

public class CacheInterceptor implements Interceptor {
    private Context mContext;
    public CacheInterceptor (Context context) {
        mContext = context;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        // 无网络时，始终使用本地Cache
        if (!NetUtils.isNetworkConnected(mContext)) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response response = chain.proceed(request);
        if (NetUtils.isNetworkConnected(mContext)) {
            // 有网络时，设置缓存过期时间0个小时
            //int maxAge = 12;
            CacheControl cacheControl = new CacheControl.Builder()
                    .maxAge(100, TimeUnit.SECONDS )
                    .build();
            response.newBuilder()
                    .removeHeader("Pragma") // 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .header("Cache-Control", cacheControl.toString())
                    .build();
        } else {
            // 无网络时，设置缓存过期超时时间为4周
            int maxStale = 60 * 60 * 24 * 28;
            response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
        return response;

    }
}
