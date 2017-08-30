package com.yann.demo.application;

import android.content.Context;
import android.os.StrictMode;
import android.support.multidex.BuildConfig;
import android.support.multidex.MultiDex;

import com.squareup.leakcanary.LeakCanary;

import java.util.Locale;

import lib.core.ExApplication;

/**
 * Created by yayun.xia on 2017/8/30.
 */

public class BaseApplication extends ExApplication {

    /**
     * Set the base context for this ContextWrapper.  All calls will then be
     * delegated to the base context.  Throws
     * IllegalStateException if a base context has already been set.
     *
     * @param base The new base context for this wrapper.
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    protected void initApp() {
        super.initApp();

        setLanguage(Locale.CHINA);

        AppConfig.setLogEnable(BuildConfig.DEBUG);
        if (BuildConfig.DEBUG) { //debug开启，release版本一定要关闭
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()  //ThreadPolicy线程策略检测
                    .detectCustomSlowCalls() // API等级11，使用StrictMode.noteSlowCode
                    .detectDiskReads()       // 磁盘读取操作
                    .detectDiskWrites()      // 磁盘写入操作
                    .detectNetwork()         // 网络操作or .detectAll() for all detectable problems
                    .penaltyDialog()         // 弹出违规提示对话框
                    .penaltyLog()            // 在Logcat 中打印违规异常信息
                    .penaltyFlashScreen()    // API等级11
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()          //VmPolicy虚拟机策略检测
                    .detectLeakedSqlLiteObjects()  // 监测泄露的Sqlite对象
                    .detectLeakedClosableObjects() // API等级11  未关闭的Closable对象泄露
                    .detectActivityLeaks()         // Activity泄露
                    .penaltyLog()
                    .penaltyDeath()
                    .build());

            LeakCanary.install(this);
        }
    }

}
