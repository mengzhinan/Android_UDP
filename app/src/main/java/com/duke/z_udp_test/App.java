package com.duke.z_udp_test;

import android.app.Application;

/**
 * @Author: duke
 * @DateTime: 2019-05-12 15:33
 * @Description:
 */
public class App extends Application {

    public static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }
}
