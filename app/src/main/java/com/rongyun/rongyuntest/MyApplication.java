package com.rongyun.rongyuntest;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

/**
 * Created by Administrator on 2018/4/11.
 */

public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        if (isMainProcess()) {   //当前进程是主进程时初始化
            //RongIM.init(this);

            Logger.addLogAdapter(new AndroidLogAdapter() {
                @Override public boolean isLoggable(int priority, String tag) {
                    return BuildConfig.DEBUG;
                }
            });
        }
    }


    /**
     * 获取当前进程名
     */
    private String getCurrentProcessName() {
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
            if (process.pid == pid) {
                processName = process.processName;
            }
        }
        return processName;
    }

    /**
     * 包名判断是否为主进程
     *
     * @param
     * @return
     */
    public boolean isMainProcess() {
        return getApplicationContext().getPackageName().equals(getCurrentProcessName());
    }
}
