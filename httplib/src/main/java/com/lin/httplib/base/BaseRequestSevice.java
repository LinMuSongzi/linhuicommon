package com.lin.httplib.base;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.lin.httplib.RequestManager;
import com.lin.httplib.itf.BusinessServiceSupport;

public class BaseRequestSevice extends Service {
    BusinessServiceSupport bss;

    @Override
    public void onCreate() {
        super.onCreate();
        bss = RequestManager.BuildBss();
        bss.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return bss.onStartCommand(intent,flags,startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return bss.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bss.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return bss.onBind(intent);
    }
}
