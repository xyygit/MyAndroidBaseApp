package com.yann.demo.common.bean;

/**
 * Created by yayun.xia on 2017/8/30.
 */

public class NetResponse<T> {
    public int errorCode = -1;
    public String elapsedTime = "";
    public String errorDesc = "";
    public T body;
}
