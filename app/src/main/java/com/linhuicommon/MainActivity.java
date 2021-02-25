package com.linhuicommon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.linhuicommon.common.BookService;
import com.linhuicommon.common.MyApplication;
import com.linhuicommon.common.SceeActivity;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements ServiceConnection {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        DataManager.getSimpleConfig();

    }

    public void click(View v){
        moveTaskToBack(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bindService(new Intent(MyApplication.context, BookService.class), MainActivity.this, Context.BIND_AUTO_CREATE);
            }
        },2000);


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("BinService", "onResume: 包com.linhuicommon："+ Arrays.toString(BookService.bookList.toArray()));
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        IBookManager iBookManager = IBookManager.Stub.asInterface(service);
        try {
            iBookManager.test2();
        } catch (RemoteException e) {
            e.printStackTrace();
            Log.i("BookService", "onServiceConnected: ");
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
