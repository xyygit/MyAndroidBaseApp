package lib.core.common;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Process;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lib.core.ExAppConfig;
import lib.core.utils.ExAppUtil;
import lib.core.utils.ExFileUtil;
import lib.core.utils.ExLogUtil;

@SuppressLint("DefaultLocale")
public class AppCrashHandler implements Thread.UncaughtExceptionHandler {

    private AppCrashHandler() {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    private static class AppCrashHandlerHolder {
        private static final AppCrashHandler ach = new AppCrashHandler();
    }

    public static final AppCrashHandler create() {
        return AppCrashHandlerHolder.ach;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {

        writeToFile(thread, throwable);

        ExLogUtil.e(Log.getStackTraceString(throwable));

        Process.killProcess(Process.myPid());
    }

    @SuppressLint("DefaultLocale")
    private File writeToFile(Thread thread, Throwable tr) {

        String logMessage;
        File logDir = new File(ExAppConfig.appLogPath);

        if (logDir.exists()) {
            //添加异常处理方案，本地日志记录子项先删除7天前，后判断大于10M删除整个目录
            ExFileUtil.getInstance().delete(logDir, 7 * 24 * 60 * 60 * 1000);

            long size = ExFileUtil.getInstance().getFileSizes(logDir);

            if (size > 10 * 1024 * 1024) {
                ExFileUtil.getInstance().delete(logDir);
            }
        }

        logDir.mkdirs();

        File log = new File(logDir.getAbsolutePath() + File.separator + getFileName());

        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(new FileWriter(log, true));

            PackageInfo pinfo = ExAppUtil.getApplicationContext().getPackageManager().getPackageInfo(ExAppUtil.getApplicationContext().getPackageName(), 0);

            logMessage = String.format("%s\r\n\r\nThread: %d\r\n\r\nMessage: %s\r\n\r\nManufacturer: " +
                            "%s\r\nModel: %s\r\nProduct: %s\r\n\r\nAndroid Version: %s\r\nAPI Level: %d\r\nHeap Size: " +
                            "%sMB\r\n\r\nVersion Code: %s\r\nVersion Name: %s\r\n\r\nStack Trace:\r\n\r\n%s",
                    new Date(), thread.getId(), tr.getMessage(),
                    Build.MANUFACTURER, Build.MODEL, Build.PRODUCT,
                    Build.VERSION.RELEASE, Build.VERSION.SDK_INT,
                    Runtime.getRuntime().maxMemory() / 1024 / 1024,
                    pinfo.versionCode, pinfo.versionName,
                    Log.getStackTraceString(tr));

            printWriter.print(logMessage);
            printWriter.print("\n\n---------------------------------------------------------------------------\n\n");
            return log;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private String getFileName() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        return df.format(new Date()) + ".txt";
    }

}
