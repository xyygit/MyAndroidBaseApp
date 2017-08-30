package com.yann.demo.welcome.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yann.demo.R;
import com.yann.demo.application.Constants;
import com.yann.demo.application.HttpRequest;
import com.yann.demo.home.activity.MainActivity;
import com.yann.demo.welcome.bean.GuideBean;

import java.util.ArrayList;

import lib.core.common.ExThread;
import lib.core.http.BitmapCallback;
import lib.core.utils.ExCommonUtil;

/**
 * Created by yayun.xia on 2017/8/30.
 */

public class WelcomePagerAdapter extends PagerAdapter{

    private Activity mContext;
    private ArrayList<GuideBean> mList;

    public WelcomePagerAdapter(Activity context, ArrayList<GuideBean> mList){
        this.mContext = context;
        this.mList = mList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_welcome_page_item,null);
        SimpleDraweeView itemImg = (SimpleDraweeView)view.findViewById(R.id.itemImg);
        final ImageView itemBtn = (ImageView)view.findViewById(R.id.itemBtn);
        final LinearLayout skipBtn = (LinearLayout)view.findViewById(R.id.skipBtn);
        final TextView time = (TextView)view.findViewById(R.id.time);

        final GuideBean guide = mList.get(position);
        if(guide != null) {
            itemImg.setImageURI(Uri.parse(guide.bgImg));
            if(!ExCommonUtil.isEmpty(guide.buttonImg)) {
                HttpRequest.Builder builder = new HttpRequest.Builder(guide.buttonImg);
                builder.addCallback(new BitmapCallback(){
                    @Override
                    public void onSucceed(int what, Bitmap result) {
                        super.onSucceed(what, result);
                        itemBtn.setImageBitmap(result);
                    }

                    @Override
                    public void onFailed(int what, int responseCode, String errorMsg, Bitmap cacheResult) {
                        super.onFailed(what, responseCode, errorMsg, cacheResult);
                        itemBtn.setVisibility(View.GONE);
                    }
                });
                builder.build().request();
                if(!ExCommonUtil.isEmpty(guide.url)) {
                    itemBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(mContext!=null) {
                                Intent intent = new Intent(mContext, MainActivity.class);
                                intent.putExtra(Constants.APP.APP_PAYLOAD, guide.url);
                                mContext.startActivity(intent);
                                mContext.finish();
                                mContext = null;
                            }
                        }
                    });
                }
            } else {
                itemBtn.setVisibility(View.GONE);
            }

            skipBtn.setVisibility(View.VISIBLE);
            if(getCount() == 1) {
                if(guide.second == 0) guide.second = 3;
                time.setText(guide.second + "s");

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if(mContext!=null) {
                            time.setText((--guide.second <= 0 ? 1 : guide.second) + "s");
                            if (guide.second <= 0) {
                                redirectMain();
                            } else {
                                ExThread.getInstance().executeByUIDelay(this, 1000);
                            }
                        }
                    }
                };
                ExThread.getInstance().executeByUIDelay(runnable, 1000);
            } else {
                if(!guide.isGuide) {
                    skipBtn.setGravity(Gravity.CENTER);
                    time.setVisibility(View.GONE);
                } else {
                    skipBtn.setVisibility(View.GONE);
                }
            }

            skipBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    redirectMain();
                }
            });

        }

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    private void redirectMain() {
        if(mContext!=null) {
//            new FMBridge().redirectURL("fnfresh://homepage?isrefresh=1");
            mContext.finish();
        }
    }

}
