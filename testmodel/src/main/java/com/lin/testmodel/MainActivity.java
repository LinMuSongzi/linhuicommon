package com.lin.testmodel;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.lin.httplib.RequestManager;
import com.lin.testmodel.common.CodeManager;
import com.linhuicommon.Book;
import com.linhuicommon.IBookManager;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.KeyEventDispatcher;

public class MainActivity extends AppCompatActivity implements ServiceConnection {


    private static final String TAG = "BinService";
    CodeManager.Runnable mRunnable;

    ///storage/emulated/0/DCIM/Camera/[迅播影院XunBo.Cc]猛鬼差馆DVD粤语中字.rmvb

    boolean isOk;

    ListView id_list_method;
    List<String> list = new ArrayList<>();
    private String[] methods;
    private Map<String, Method> methodMap;
    private IBookManager iBookManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x123);
        mRunnable = CodeManager.build(new CodeManager.Config() {
            @Override
            public String getAudioVoice() {
                return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()
                        + File.separator + "rap_jay.aac";
            }
        });

        id_list_method = findViewById(R.id.id_list_method);

        id_list_method.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, buildBookManagerMethodList()));
        id_list_method.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i(TAG, "onItemClick: " + methods[position]);
                try {

                    if (iBookManager != null) {
                        Object methodInvoke = null;
                        switch (position) {
                            case 0:
                                Book book = new Book();
                                book.setId(0x9123);
                                book.setName("小时代");
                                book.setAuthor("郭敬明");
                                book.setOrganization("中国");
                                iBookManager.addBook(book);
                                break;
                            case 1:
                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                            default:
                                methodInvoke = methodMap.get(methods[position]).invoke(iBookManager);
                                if (methodInvoke != null) {
                                    if (methodInvoke instanceof List) {
                                        Log.i(TAG, "onItemClick: " + ((List) methodInvoke).toArray());
                                    }
                                    Log.i(TAG, "onItemClick: " + methodInvoke);
                                }
                        }
                    }

                } catch (Exception ex) {
                    Log.i(TAG, "onItemClick: " + Arrays.toString(ex.getStackTrace()));
                    ex.printStackTrace();
                }

            }
        });

        bind();
    }

    private void bind() {

        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.BookService");
            intent.setPackage("com.linhuicommon");
            bindService(intent, this, Context.BIND_AUTO_CREATE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (iBookManager == null) {
                        Log.i(TAG, "run: 重连服务！");
                        bind();
                    }
                }
            }, 2000);
        } catch (Exception ex) {
            Log.i(TAG, "bind: error");
            ex.printStackTrace();
        }
    }

    private String[] buildBookManagerMethodList() {
        Method[] methods = com.linhuicommon.IBookManager.class.getDeclaredMethods();
        this.methods = new String[methods.length];
        methodMap = new ArrayMap<>();
        for (int i = 0; i < this.methods.length; i++) {

            Class[] classes = methods[i].getParameterTypes();
            if (classes == null || classes.length == 0) {
                this.methods[i] = methods[i].getName();
            } else {
                this.methods[i] = methods[i].getName() + " : " + Arrays.toString(classes);
            }
            methodMap.put(this.methods[i], methods[i]);
        }
        return this.methods;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    public void startTest(View view) {
        if (mRunnable.isStart()) {
            mRunnable.stop();
        } else {
            mRunnable.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(this);
        new Handler().removeCallbacksAndMessages(null);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        iBookManager = IBookManager.Stub.asInterface(service);
        new Handler().removeCallbacksAndMessages(null);
        Log.i(TAG, "onServiceConnected: ");
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.i(TAG, "onServiceDisconnected: ");
        iBookManager = null;
    }
}
