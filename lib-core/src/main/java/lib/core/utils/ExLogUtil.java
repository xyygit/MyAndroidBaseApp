package lib.core.utils;

import android.util.Log;

import lib.core.ExAppConfig;

public class ExLogUtil {

    private static final String TAG = ExLogUtil.class.getName();
    private static final String TAG_CONTENT_PRINT = "%s:%s.%s:%d";

    private static final StackTraceElement getCurrentStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }

    private static final String getContent(StackTraceElement trace) {
        return String.format(TAG_CONTENT_PRINT, TAG, trace.getClassName(), trace.getMethodName(), trace.getLineNumber());
    }

    public static final void d(String tag, String msg) {
        if (ExAppConfig.isLogEnable) {
            Log.d(tag, getContent(getCurrentStackTraceElement()) + ">" + msg);
        }
    }

    public static final void d(String tag, int msg) {
        if (ExAppConfig.isLogEnable) {
            Log.d(tag, getContent(getCurrentStackTraceElement()) + ">" + msg);
        }
    }

    public static final void d(String msg) {
        d(TAG, msg);
    }

    public static final void i(String tag, String msg) {
        if (ExAppConfig.isLogEnable) {
            Log.i(tag, getContent(getCurrentStackTraceElement()) + ">" + msg);
        }
    }

    public static final void i(String tag, int msg) {
        if (ExAppConfig.isLogEnable) {
            Log.i(tag, getContent(getCurrentStackTraceElement()) + ">" + msg);
        }
    }

    public static final void i(String msg) {
        i(TAG, msg);
    }

    public static final void w(String tag, String msg) {
        if (ExAppConfig.isLogEnable) {
            Log.w(tag, getContent(getCurrentStackTraceElement()) + ">" + msg);
        }
    }

    public static final void w(String tag, int msg) {
        if (ExAppConfig.isLogEnable) {
            Log.w(tag, getContent(getCurrentStackTraceElement()) + ">" + msg);
        }
    }

    public static final void w(String msg) {
        w(TAG, msg);
    }

    public static final void v(String tag, String msg) {
        if (ExAppConfig.isLogEnable) {
            Log.v(tag, getContent(getCurrentStackTraceElement()) + ">" + msg);
        }
    }

    public static final void v(String tag, int msg) {
        if (ExAppConfig.isLogEnable) {
            Log.v(tag, getContent(getCurrentStackTraceElement()) + ">" + msg);
        }
    }

    public static final void v(String msg) {
        v(TAG, msg);
    }

    public static final void e(String tag, String msg) {
        if (ExAppConfig.isLogEnable) {
            Log.e(tag, getContent(getCurrentStackTraceElement()) + ">" + msg);
        }
    }

    public static final void e(String tag, int msg) {
        if (ExAppConfig.isLogEnable) {
            Log.e(tag, getContent(getCurrentStackTraceElement()) + ">" + msg);
        }
    }

    public static final void e(String msg) {
        e(TAG, msg);
    }
}
