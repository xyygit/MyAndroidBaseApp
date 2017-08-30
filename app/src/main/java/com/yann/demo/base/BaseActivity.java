package com.yann.demo.base;

import android.os.Bundle;

import lib.core.ExActivity;

/**
 * Created by yayun.xia on 2017/8/30.
 */

public class BaseActivity extends ExActivity {
    @Override
    protected void exProcessOnCreateBefore(Bundle savedInstanceState) {

    }

    @Override
    protected boolean exInterceptOnCreate(Bundle savedInstanceState) {
        return false;
    }

    @Override
    protected int exInitLayout() {
        return 0;
    }

    @Override
    protected boolean exInterceptInit() {
        return false;
    }

    @Override
    protected void exInitView() {

    }

    @Override
    protected void applicationBackground() {

    }
}
