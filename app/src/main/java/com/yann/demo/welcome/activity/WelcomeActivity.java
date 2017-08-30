package com.yann.demo.welcome.activity;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.yann.demo.R;
import com.yann.demo.application.Constants;
import com.yann.demo.application.Environment;
import com.yann.demo.application.HttpRequest;
import com.yann.demo.base.BaseActivity;
import com.yann.demo.home.activity.MainActivity;
import com.yann.demo.welcome.bean.GuideBean;
import com.yann.demo.welcome.bean.WelcomeBean;

import java.util.ArrayList;

import lib.core.annotation.inject.ViewInject;
import lib.core.bean.TitleBar;
import lib.core.common.ExThread;
import lib.core.http.ResponseCallback;
import lib.core.utils.ExCommonUtil;
import lib.core.utils.ExDeviceUtil;
import lib.core.utils.ExSharePreferencesUtil;

/**
 * Created by yayun.xia on 2017/8/30.
 */

public class WelcomeActivity extends BaseActivity{

    @ViewInject(R.id.itemImg)
    private SimpleDraweeView itemImg;

    @Override
    protected void exProcessOnCreateBefore(Bundle savedInstanceState) {

        if (!ExDeviceUtil.getInstance().checkDeviceHasNavigationBar(this)) {
            setTheme(R.style.AppStartWithOutMenuTheme);
        } else {
            setTheme(R.style.AppStartWithMenuTheme);
        }
        super.exProcessOnCreateBefore(savedInstanceState);
    }

    @Override
    protected boolean exInterceptOnCreate(Bundle savedInstanceState) {
        if (MainActivity.isRunning) {
            String payload = getPayload();
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            if (!ExCommonUtil.isEmpty(payload)) intent.putExtra(Constants.APP.APP_PAYLOAD, payload);
            startActivity(intent);
            finish();
            return true;
        }
        return false;
    }

    @Override
    protected boolean exInterceptInit() {
        return false;
    }

    @Override
    protected int exInitLayout() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void exInitView() {
        super.exInitView();
    }

    @Override
    protected void exInitToolbar(TitleBar toolbar) {
        toolbar.setVisibility(View.GONE);
    }

    @Override
    protected void exInitData() {
        super.exInitData();

        // 启动定位
//        GlobalAddressUtils.getInstance().startHomeLocation(getApplicationContext());
        // 注册个推
//        GetUI.register(this);

        ExThread.getInstance().executeByUIDelay(runnable, 3000);

        //拉取更新API接口
        Environment.getInstance().loadAPIConfig(new ResponseCallback() {
            @Override
            public void onSucceed(int what, Object result) {

                redirect();

//                TrackUtils.trackBegin(EntryMethod.ENTER);

                // 重新获取clientId
//                AdminUser.getInstance().setClientId(GetUI.getClientId(WelcomeActivity.this));

            }
        });

    }

    private void redirect() {
        String payload = getPayload();
        if (ExCommonUtil.isEmpty(payload)) {
            boolean closeFirstLaunch = ExSharePreferencesUtil.getInstance().getBoolean(Constants.SharePreferences.APP_SHARE_PRE_CLOSE_FIRST_LAUNCH, false);
            closeFirstLaunch = true;
            if(closeFirstLaunch) {
                loadWelcome();
            } else {
                loadGuide();
            }
        } else {
            redirectMain();
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (Constants.hasNetConfig()) redirectMain();
        }
    };

    private void loadWelcome() {
        HttpRequest.Builder builder = new HttpRequest.Builder(Constants.getNetConfig().wirelessAPI.welcomePage);
        builder.setConvertClass(WelcomeBean.class);
        builder.addCallback(new ResponseCallback() {
            @Override
            public void onSucceed(int what, Object result) {
                try {
                    final WelcomeBean welcomeBean = (WelcomeBean) result;
                    if (ExCommonUtil.isEmpty(welcomeBean) || ExCommonUtil.isEmpty(welcomeBean.items)) {
                        ExThread.getInstance().cancelByUI(runnable);
                        redirectMain();
                    } else if (!MainActivity.isRunning) {
                        GuideBean guideBean = welcomeBean.items.get(0);
                        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
                            @Override
                            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                                super.onFinalImageSet(id, imageInfo, animatable);
                                ExThread.getInstance().cancelByUI(runnable);
                                itemImg.setVisibility(View.GONE);
                                Intent intent = new Intent(WelcomeActivity.this, GuideActivity.class);
                                intent.putExtra("welcomeBean", welcomeBean);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onFailure(String id, Throwable throwable) {
                                super.onFailure(id, throwable);
                                redirectMain();
                            }
                        };
                        DraweeController controller = Fresco.newDraweeControllerBuilder()
                                .setControllerListener(controllerListener)
                                .setUri(guideBean.bgImg)
                                .build();
                        itemImg.setController(controller);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    redirectMain();
                }
            }

            @Override
            public void onFailed(int what, int responseCode, String errorMsg) {
                super.onFailed(what, responseCode, errorMsg);
                redirectMain();
            }
        });
        builder.build().request();
    }


    private void loadGuide() {
        ExThread.getInstance().cancelByUI(runnable);
        ExSharePreferencesUtil.getInstance().putBoolean(Constants.SharePreferences.APP_SHARE_PRE_CLOSE_FIRST_LAUNCH, true);

        WelcomeBean welcomeBean = new WelcomeBean();
        ArrayList<GuideBean> items = new ArrayList<GuideBean>();
        GuideBean guideBean1 = new GuideBean();
        guideBean1.bgImg = "res://drawable/" + R.drawable.guide1;
        guideBean1.isGuide = true;
        items.add(guideBean1);
        GuideBean guideBean2 = new GuideBean();
        guideBean2.bgImg = "res://drawable/" + R.drawable.guide2;
        guideBean2.isGuide = true;
        items.add(guideBean2);
        GuideBean guideBean3 = new GuideBean();
        guideBean3.bgImg = "res://drawable/" + R.drawable.guide3;
        guideBean3.isGuide = true;
        items.add(guideBean3);
        GuideBean guideBean4 = new GuideBean();
        guideBean4.bgImg = "res://drawable/" + R.drawable.guide4;
        guideBean4.isGuide = true;
        items.add(guideBean4);
        welcomeBean.items = items;
        Intent intent = new Intent(WelcomeActivity.this, GuideActivity.class);
        intent.putExtra("welcomeBean", welcomeBean);
        startActivity(intent);
        finish();
    }


    private String getPayload() {

        String payload = null;
        Uri uri = getIntent().getData();
        if (uri != null) {
            payload = uri.toString();
        }

        return payload;
    }

    private void redirectMain() {
        if(MainActivity.isRunning) {
            finish();
            return;
        }
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ExThread.getInstance().cancelByUI(runnable);
        runnable = null;
    }

}
