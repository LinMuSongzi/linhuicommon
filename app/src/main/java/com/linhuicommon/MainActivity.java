package com.linhuicommon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.linhuicommon.common.BookService;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        DataManager.getSimpleConfig();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("BinService", "onResume: 包com.linhuicommon："+ Arrays.toString(BookService.bookList.toArray()));
    }
}
