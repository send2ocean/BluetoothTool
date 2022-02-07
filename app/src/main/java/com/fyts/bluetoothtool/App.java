package com.fyts.bluetoothtool;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;


public class App extends Application {

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
