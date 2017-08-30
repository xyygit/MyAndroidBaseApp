package com.yann.demo.welcome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.yann.demo.R;
import com.yann.demo.base.BaseActivity;
import com.yann.demo.home.view.BannerIndicatorView;
import com.yann.demo.welcome.adapter.WelcomePagerAdapter;
import com.yann.demo.welcome.bean.GuideBean;
import com.yann.demo.welcome.bean.WelcomeBean;

import lib.core.annotation.inject.ViewInject;
import lib.core.bean.TitleBar;

/**
 * Created by lightning on 17/3/2.
 */

public class GuideActivity extends BaseActivity {

    @ViewInject(R.id.welcomePager)
    private ViewPager welcomePager;
    @ViewInject(R.id.welcomeIndicator)
    private BannerIndicatorView welcomeIndicator;

    private WelcomeBean welcomeBean;
    private boolean isLastPage = false;
    private boolean isDragPage = false;
    private boolean canJumpPage = true;

    @Override
    protected void exInitBundle(Bundle savedInstanceState, Intent intent) {
        super.exInitBundle(savedInstanceState, intent);
        welcomeBean = intent.getParcelableExtra("welcomeBean");
    }

    @Override
    protected int exInitLayout() {
        return R.layout.activity_guide;
    }

    @Override
    protected boolean exInterceptInit() {
        return false;
    }

    @Override
    protected void exInitToolbar(TitleBar toolbar) {
        toolbar.setVisibility(View.GONE);
    }

    @Override
    protected void exInitView() {
        super.exInitView();
        int size = welcomeBean.items.size();
        if (size == 1 && welcomeBean.second != 0) {
            GuideBean guideBean = welcomeBean.items.get(0);
            guideBean.second = welcomeBean.second;
        }
        if (size == 1) {
            welcomeIndicator.setVisibility(View.GONE);
        }
        final WelcomePagerAdapter pagerAdapter = new WelcomePagerAdapter(this, welcomeBean.items);
        welcomePager.setAdapter(pagerAdapter);
        welcomeIndicator.initDotView(size);
        welcomeIndicator.setDotSelected(0);
        welcomePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (isLastPage && isDragPage && positionOffsetPixels == 0){   //当前页是最后一页，并且是拖动状态，并且像素偏移量为0
                    if (canJumpPage){
                        canJumpPage = false;
                        redirectMain();
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                welcomeIndicator.setDotSelected(position);
                isLastPage = position == pagerAdapter.getCount()-1;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                isDragPage = state == 1;
            }
        });
    }

    private void redirectMain() {
//        new FMBridge().redirectURL("fnfresh://homepage?isrefresh=1");
        finish();
    }
}
