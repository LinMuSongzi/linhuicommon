package com.linhuicommon.common;

import android.os.Bundle;
import android.os.Handler;

import com.linhuicommon.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SceeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scee);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },3000);
    }



}
