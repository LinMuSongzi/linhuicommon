package com.linhuicommon.common;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

public class MyApplication extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this,BookService.class));
        context = this;
    }
}
