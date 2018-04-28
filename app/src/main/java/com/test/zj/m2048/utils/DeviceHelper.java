package com.test.zj.m2048.utils;

import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 获取设备的屏幕宽度
 */

public class DeviceHelper {

    public static int getDeviceWidth(WindowManager manager){

        if(manager != null){
            DisplayMetrics metrics = new DisplayMetrics();

            manager.getDefaultDisplay().getMetrics(metrics);

            return metrics.widthPixels;
        }

        return 0;
    }

    public static int getDeviceHeight(WindowManager manager){

        if(manager != null){
            DisplayMetrics metrics = new DisplayMetrics();

            manager.getDefaultDisplay().getMetrics(metrics);

            return metrics.heightPixels;
        }

        return 0;
    }
}
