package com.warm.viewfinder;

import android.app.Application;

/**
 * 作者：warm
 * 时间：2018-08-25 13:14
 * 描述：
 */
public class MyApp extends Application {
    private static MyApp mInstance;

    public static MyApp getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
    }
}
