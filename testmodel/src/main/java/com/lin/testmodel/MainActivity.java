package com.lin.testmodel;

import android.Manifest;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.lin.testmodel.common.CodeManager;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    CodeManager.Runnable mRunnable;

    ///storage/emulated/0/DCIM/Camera/[迅播影院XunBo.Cc]猛鬼差馆DVD粤语中字.rmvb

    boolean isOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x123);
        mRunnable = CodeManager.build(new CodeManager.Config() {
            @Override
            public String getAudioVoice() {
                return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()
                        + File.separator + "Camera" + File.separator + "rap_jay.aac";
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    public void startTest(View view) {
        if(mRunnable.isStart()){
            mRunnable.stop();
        }else {
            mRunnable.start();
        }
    }


}
