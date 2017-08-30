package com.yann.demo.application;

import lib.core.ExAppConfig;

/**
 * Created by yayun.xia on 2017/8/30.
 */

public class AppConfig extends ExAppConfig{
    public static final void setLogEnable(boolean enable) {
        ExAppConfig.isLogEnable = enable;
    }
}
