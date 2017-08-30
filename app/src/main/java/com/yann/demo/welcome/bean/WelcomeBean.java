package com.yann.demo.welcome.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.yann.demo.common.bean.NetResponse;

import java.util.ArrayList;

/**
 * Created by yayun.xia on 2017/8/30.
 */

public class WelcomeBean extends NetResponse<WelcomeBean> implements Parcelable{
    public int second; //欢迎页单页时停留时间，单位秒，只有单页才有效，如果多页，则需要手动
    public ArrayList<GuideBean> items; //欢迎页信息

    public WelcomeBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.second);
        dest.writeList(this.items);
    }

    protected WelcomeBean(Parcel in) {
        this.second = in.readInt();
        this.items = new ArrayList<GuideBean>();
        in.readList(this.items, GuideBean.class.getClassLoader());
    }

    public static final Creator<WelcomeBean> CREATOR = new Creator<WelcomeBean>() {
        @Override
        public WelcomeBean createFromParcel(Parcel source) {
            return new WelcomeBean(source);
        }

        @Override
        public WelcomeBean[] newArray(int size) {
            return new WelcomeBean[size];
        }
    };
}
