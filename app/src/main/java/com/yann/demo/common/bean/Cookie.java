package com.yann.demo.common.bean;

import lib.core.common.ExAppID;

/**
 * Created by yayun.xia on 2017/8/30.
 */

public class Cookie {
    public String AUTH_TOKEN ;
    public String guid ;
    public String username ;
    public boolean islogin ;
    public String deviceId = ExAppID.getAppID();
}
