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
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import lib.core.bean.ActionListener;
import lib.core.bean.TitleBar;
import lib.core.common.ExMonitor;
import lib.core.common.ExNetStatusListener;
import lib.core.definition.OperaCallBack;
import lib.core.utils.ExAnnotationUtil;
import lib.core.utils.ExAppUtil;
import lib.core.utils.ExCommonUtil;
import lib_core.R;


public abstract class ExActivity extends FragmentActivity {

    private ArrayList<BroadcastReceiver> localReceiverList;
    private ArrayList<BroadcastReceiver> systemReceiverList;
    private LocalBroadcastManager mLocalBroadcastManager;
    private ArrayList<ActionListener> listeners;
    protected View contentView;
    protected ProgressBar progressBar;
    private int currentProgress = 0;
    private boolean isAnimStart = false;
    private boolean mIsForeground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        exProcessOnCreateBefore(savedInstanceState);

        super.onCreate(savedInstanceState);

        if (exInterceptOnCreate(savedInstanceState)) {
            return;
        }

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootViewFrame = layoutInflater.inflate(R.layout.base_container, null);
        TitleBar titleBar = (TitleBar) rootViewFrame.findViewById(R.id.titleBar);
        titleBar.setToolbarTitle((TextView) rootViewFrame.findViewById(R.id.toolbar_title));
        titleBar.setToolbarShadow(rootViewFrame.findViewById(R.id.toolbarShadow));
        titleBar.setExtendFrame((FrameLayout) rootViewFrame.findViewById(R.id.extendFrame));
        progressBar = (ProgressBar) rootViewFrame.findViewById(R.id.progressbar);
        FrameLayout rootView = (FrameLayout) rootViewFrame.findViewById(R.id.rootView);

        int layoutId = exInitLayout();
        if (layoutId == 0) {
            contentView = exInitLayoutView();
        } else {
            contentView = layoutInflater.inflate(layoutId, null);
        }
        if (contentView != null) rootView.addView(contentView);

        setContentView(rootViewFrame);

        ExAnnotationUtil.inject(this);

        exInitBundle(savedInstanceState, getIntent());

        exInitToolbar(titleBar);

        exInitView();

        if(!exInterceptInit()) {
            exInitData();
            initActionListener();
        }

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
    protected void exInitBundle(Bundle savedInstanceState, Intent intent) {

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
    protected abstract void exInitView();

    /**
     * Method_初始化数据： 在基础数据初始化完成之后可以使用该方法
     */
    protected void exInitData() {

    }

    /**
     * 初始化toolbar
     *
     * @param toolbar
     */
    protected void exInitToolbar(TitleBar toolbar) {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    public void startActivities(Intent[] intents) {
        super.startActivities(intents);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    public void finish() {
        super.finish();
    }

    protected void back() {
        finish();
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsForeground = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mIsForeground = false;
        if (ExAppUtil.getInstance().isApplicationBroughtToBackground()) {
            applicationBackground();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unRegisterLocalReceiver();

        unRegisterSystemReceiver();

        if(!ExCommonUtil.isEmpty(listeners)) {
            for (ActionListener listener : listeners) {
                ExMonitor.getInstance().remove(listener);
            }
        }
    }

    protected abstract void applicationBackground();

    protected void registerLocalReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        if (mLocalBroadcastManager == null)
            mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        mLocalBroadcastManager.registerReceiver(receiver, filter);
        if (localReceiverList == null) localReceiverList = new ArrayList<BroadcastReceiver>();
        localReceiverList.add(receiver);
    }

    protected void registerSystemReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        registerReceiver(receiver, filter);
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
        if (systemReceiverList == null) return;
        for (BroadcastReceiver receiver : systemReceiverList) {
            unregisterReceiver(receiver);
        }
    }

    protected void sendLocalBroadcast(Intent intent) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    protected void sendSystemBroadcast(Intent intent) {
        sendBroadcast(intent);
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

    public boolean isForeground() {
        return mIsForeground;
    }

    protected void addActionListener(ActionListener listener) {
        if(listeners == null) listeners = new ArrayList<>();
        listeners.add(listener);
        ExMonitor.getInstance().add(listener);
    }

    private void initActionListener() {
        addActionListener(new ExNetStatusListener(ExNetStatusListener.SYSTEM_NET_STATUS){
            @Override
            public void onNetConnect() {
                ExActivity.this.onNetConnect();
            }

            @Override
            public void onNetDisConnect() {
                ExActivity.this.onNetDisConnect();
            }
        });
    }

    /**
     * 网络重连时调用
     */
    protected void onNetConnect() {

    }

    /**
     * 网络断开时调用
     */
    protected void onNetDisConnect() {

    }

}
