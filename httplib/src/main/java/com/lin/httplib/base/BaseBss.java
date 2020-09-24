package com.lin.httplib.base;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.lin.httplib.itf.BusinessServiceSupport;
import com.lin.httplib.itf.ISession;
import com.lin.httplib.itf.RequestObject;

public abstract class BaseBss implements BusinessServiceSupport {

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler(new Token(intent,flags,startId));
        return Service.START_NOT_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected abstract void handler(Token token);


    protected static class Token implements ISession {
        Intent intent;
        int flags,startId;
        RequestObject requestObject;
        public Token(Intent intent, int flags, int startId) {
            this.intent = intent;
            this.flags = flags;
            this.startId = startId;
            requestObject = (RequestObject) intent.getSerializableExtra("Token");
        }

        @Override
        public RequestObject get() {
            return requestObject;
        }
    }

}
