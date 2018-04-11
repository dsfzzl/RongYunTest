package com.rongyun.rongyuntest;

import android.app.Application;

import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2018/4/11.
 */

public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        RongIM.init(this);
    }
}
