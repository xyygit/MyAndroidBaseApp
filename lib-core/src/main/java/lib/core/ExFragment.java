package lib.core;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import lib.core.bean.TitleBar;
import lib.core.definition.OperaCallBack;
import lib.core.utils.ExAnnotationUtil;
import lib_core.R;

/**
 * Created by lightning on 2016/1/3.
 */
public abstract class ExFragment extends Fragment {

    private ArrayList<BroadcastReceiver> localReceiverList;
    private ArrayList<BroadcastReceiver> systemReceiverList;
    private LocalBroadcastManager mLocalBroadcastManager;
    protected View contentView;
    protected ProgressBar progressBar;
    private int currentProgress = 0;
    private boolean isAnimStart = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        exProcessOnCreateBefore(savedInstanceState);
        super.onCreateView(inflater, container, savedInstanceState);

        if (exInterceptOnCreate(savedInstanceState)) {
            return null;
        }

        View rootViewFrame = inflater.inflate(R.layout.base_container, null);
        TitleBar titleBar = (TitleBar) rootViewFrame.findViewById(R.id.titleBar);
        titleBar.setToolbarTitle((TextView) rootViewFrame.findViewById(R.id.toolbar_title));
        titleBar.setToolbarShadow(rootViewFrame.findViewById(R.id.toolbarShadow));
        titleBar.setExtendFrame((FrameLayout) rootViewFrame.findViewById(R.id.extendFrame));
        titleBar.setVisibility(View.GONE);
        titleBar.getToolbarShadow().setVisibility(View.GONE);
        progressBar = (ProgressBar) rootViewFrame.findViewById(R.id.progressbar);
        FrameLayout rootView = (FrameLayout) rootViewFrame.findViewById(R.id.rootView);

        int layoutId = exInitLayout();
        if (layoutId == 0) {
            contentView = exInitLayoutView();
        } else {
            contentView = inflater.inflate(layoutId, null);
        }
        if (contentView != null) rootView.addView(contentView);

        ExAnnotationUtil.inject(this, rootViewFrame);

        exInitBundle(getArguments());

        exInitToolbar(titleBar);

        exInitView(contentView);

        if(!exInterceptInit()) {
            exInitData();
        }

        return rootViewFrame;
    }

    /**
     * Method ：在 OnCreate 前执行
     */
    protected abstract void exProcessOnCreateBefore(Bundle savedInstanceState);


    /**
     * Method_拦截 ：对 OnCreate 拦截处理
     *
     * @return 是否拦截 OnCreate
     */
    protected abstract boolean exInterceptOnCreate(Bundle savedInstanceState);


    /**
     * Method_初始化传入参数 ：处理进入之前传入的数据
     */
    protected void exInitBundle(Bundle bundle) {

    }

    /**
     * Method_初始化布局 ：对展示布局进行设置
     *
     * @return 布局资源 ID
     */
    protected abstract int exInitLayout();

    /**
     * Method_初始化布局 ：对展示布局进行设置
     *
     * @return 布局资源 View
     */
    protected View exInitLayoutView() {
        return null;
    }

    /**
     * Method_初始化状态页面： 一般包括无网络或无数据页面的处理
     */
    protected OperaCallBack exInitStatusView() {
        return null;
    }

    protected abstract boolean exInterceptInit();

    /**
     * Method_初始化控件参数： 在该方法中，可以对已绑定的控件数据初始化
     */
    protected abstract void exInitView(View contentView);

    /**
     * Method_初始化数据： 在基础数据初始化完成之后可以使用该方法
     */
    protected void exInitData() {

    }

    /**
     * 初始化toolbar
     *
     */
    protected void exInitToolbar(TitleBar titleBar) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unRegisterLocalReceiver();

        unRegisterSystemReceiver();
    }

    protected void registerLocalReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        if (mLocalBroadcastManager == null)
            mLocalBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        mLocalBroadcastManager.registerReceiver(receiver, filter);
        if (localReceiverList == null) localReceiverList = new ArrayList<BroadcastReceiver>();
        localReceiverList.add(receiver);
    }

    protected void registerSystemReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        Context context = getContext();
        if(context == null) return;
        context.registerReceiver(receiver, filter);
        if (systemReceiverList == null) systemReceiverList = new ArrayList<BroadcastReceiver>();
        systemReceiverList.add(receiver);
    }

    protected void unRegisterLocalReceiver() {
        if (localReceiverList == null) return;
        for (BroadcastReceiver receiver : localReceiverList) {
            mLocalBroadcastManager.unregisterReceiver(receiver);
        }
    }

    protected void unRegisterSystemReceiver() {
        Context context = getContext();
        if(context == null) return;
        if (systemReceiverList == null) return;
        for (BroadcastReceiver receiver : systemReceiverList) {
            context.unregisterReceiver(receiver);
        }
    }

    protected void sendLocalBroadcast(Intent intent) {
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    protected void sendSystemBroadcast(Intent intent) {
        Context context = getContext();
        if(context == null) return;
        context.sendBroadcast(intent);
    }

    public void progressUpdate(int newProgress) {
        currentProgress = progressBar.getProgress();
        if (newProgress >= 100 && !isAnimStart) {
            isAnimStart = true;
            progressBar.setProgress(newProgress);
            startDismissAnimation(progressBar.getProgress());
        } else {
            startProgressAnimation(newProgress);
        }
    }

    private void startProgressAnimation(int newProgress) {
        ObjectAnimator animator = ObjectAnimator.ofInt(progressBar, "progress", currentProgress, newProgress);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }

    private void startDismissAnimation(final int progress) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(progressBar, "alpha", 1.0f, 0.0f);
        anim.setDuration(1500);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float fraction = valueAnimator.getAnimatedFraction();
                int offset = 100 - progress;
                progressBar.setProgress((int) (progress + offset * fraction));
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressBar.setProgress(100);
                progressBar.setVisibility(View.GONE);
                isAnimStart = false;
            }
        });
        anim.start();
    }

    public void setProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }
}
