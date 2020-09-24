package com.lin.httplib.itf;

import android.content.Intent;
import android.os.IBinder;

public interface BusinessServiceSupport {
    void onCreate();

    int onStartCommand(Intent intent, int flags, int startId);

    boolean onUnbind(Intent intent);

    void onDestroy();

    IBinder onBind(Intent intent);

}
