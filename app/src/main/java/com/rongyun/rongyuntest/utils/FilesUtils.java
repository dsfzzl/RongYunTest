package com.rongyun.rongyuntest.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by Administrator on 2018/4/13.
 * 文件操作工具类
 */

public class FilesUtils {
    private FilesUtils(){}


    /**
     *
     * @param context
     * @return true 已经获得权限 ，false 需要申请权限
     */
    public static boolean hasExternalStoragePermission(Context context) {
        int perm = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return perm == PackageManager.PERMISSION_GRANTED;
    }
}
