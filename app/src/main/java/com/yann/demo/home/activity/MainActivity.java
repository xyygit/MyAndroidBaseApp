package com.yann.demo.home.activity;

import android.os.Bundle;

import com.yann.demo.R;
import com.yann.demo.base.BaseActivity;


public class MainActivity extends BaseActivity {

    public static boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
