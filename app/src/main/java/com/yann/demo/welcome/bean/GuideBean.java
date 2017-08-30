package com.yann.demo.welcome.bean;

import java.io.Serializable;

/**
 * Created by lightning on 17/4/20.
 */

public class GuideBean implements Serializable {

    public String bgImg = ""; //背景图片
    public String buttonImg = ""; //按钮图片
    public String url = ""; //按钮跳转地址
    public int second;
    public boolean isGuide;

}
