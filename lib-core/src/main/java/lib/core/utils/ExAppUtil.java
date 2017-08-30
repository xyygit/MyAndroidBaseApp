package lib.core.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import lib.core.ExApplication;
import lib_core.R;

/**
 * 获取应用相关信息
 */
public class ExAppUtil {

    private ExAppUtil() {
    }

    private static class ExAppUtilHolder {
        private static final ExAppUtil eau = new ExAppUtil();
    }

    public static final ExAppUtil getInstance() {
        return ExAppUtilHolder.eau;
    }

    private static Context context;

    public void setContext(Context context) {
        ExAppUtil.context = context;
    }

    /**
     * 获取application ontext
     *
     * @return Context
     */
    public static final Context getApplicationContext() {
        if (context == null) {
            ExApplication.resetApplicationContext();
        }
        return context;
    }

    /**
     * 判断当前的线程是否为主线程
     * @return
     */
    public final boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();

    }

    /**
     * 获取项目版本号
     *
     * @return String
     * @method getVersionName
     * @author lightning
     */
    public final String getVersionName() {
        PackageInfo pinfo;
        try {
            pinfo = getApplicationContext().getPackageManager().getPackageInfo(
                    getApplicationContext().getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return pinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "1.0";
    }

    /**
     * 获取项目版本号
     *
     * @return String
     * @method getVersionName
     * @author lightning
     */
    public final int getVersionCode() {
        PackageInfo pinfo;
        try {
            pinfo = getApplicationContext().getPackageManager().getPackageInfo(
                    getApplicationContext().getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return pinfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 判断应用是否处于后台
     *
     * @return
     */
    public final boolean isApplicationBroughtToBackground() {
        try {
            ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
            if (!tasks.isEmpty()) {
                ComponentName topActivity = tasks.get(0).topActivity;
                if (!topActivity.getPackageName().equals(getApplicationContext().getPackageName())) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Method_显示系统键盘
     *
     * @param v 操作对象
     */
    public final void showKeyBord(View v) {
        try {
            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(v, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method_显示或隐藏系统键盘
     */
    public final void toggleKeyBord() {
        try {
            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method_点击任何地方隐藏系统键盘
     */
    public final void hideKeyBord(Activity activity) {
        if (activity != null) {
           try {
               InputMethodManager imeManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
               imeManager.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
           } catch (Exception e) {
               e.printStackTrace();
           }
        }
    }

    /**
     * Method_隐藏系统键盘
     *
     * @param v 操作对象
     */
    public final void hideKeyBord(View v) {
        if (v != null) {
            try {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromInputMethod(v.getWindowToken(), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method_打电话
     *
     * @param activity    当前 Activity
     * @param phoneNumber 电话号码
     */
    public final void callPhone(Activity activity, String phoneNumber) {
        if (activity != null && !ExCommonUtil.isEmpty(phoneNumber)) {
            try {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                PackageManager pm = context.getPackageManager();
                ComponentName cn = intent.resolveActivity(pm);
                if(cn != null) {
                    activity.startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method_发送邮件
     *
     * @param activity  当前 Activity
     * @param emailAddr 邮件地址
     * @param selectTip 选择客户端提示
     */
    public final void sendEmail(Activity activity, String[] emailAddr, String selectTip) {
        if (activity != null && !ExCommonUtil.isEmpty(emailAddr)) {
            try {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, emailAddr);
                intent.setType("plain/text");
                activity.startActivity(Intent.createChooser(intent, selectTip));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method_发送短信
     *
     * @param activity    当前 Activity
     * @param phoneNumber 电话号码
     * @param content     内容
     */
    public final void sendSMS(Activity activity, String phoneNumber, String content) {
        if (activity != null && !ExCommonUtil.isEmpty(phoneNumber)) {
            try {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
                PackageManager pm = context.getPackageManager();
                ComponentName cn = intent.resolveActivity(pm);
                if(cn != null) {
                    intent.putExtra("sms_body", content);
                    activity.startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method_跳转浏览器显示
     *
     * @param activity 当前 Activity
     * @param url
     */
    public final void showPageByWeb(Activity activity, String url) {
        if(activity != null && !ExCommonUtil.isEmpty(url)) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(url));
            activity.startActivity(intent);
        }
    }

    /**
     * Method_打开 Excel
     *
     * @param activity 当前 Activity
     * @param path     路径
     */
    public final void openExcel(Activity activity, String path) {

        openFile(activity, path, "application/vnd.ms-excel");
    }

    /**
     * Method_打开 PPT
     *
     * @param activity 当前 Activity
     * @param path     路径
     */
    public final void openPPT(Activity activity, String path) {

        openFile(activity, path, "application/vnd.ms-powerpoint");
    }

    /**
     * Method_打开 Word
     *
     * @param activity 当前 Activity
     * @param path     路径
     */
    public final void openWord(Activity activity, String path) {

        openFile(activity, path, "application/msword");
    }

    /**
     * Method_打开文件
     *
     * @param activity 当前 Activity
     * @param path     路径
     * @param type     文件路径
     */
    public final void openFile(Activity activity, String path, String type) {
        if(activity != null && !ExCommonUtil.isEmpty(path)) {
            try {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.addCategory("");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(new File(path)), type);
                activity.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method_应用安装
     *
     * @param activity 当前 Activity
     * @param filename 名
     * @return 结果
     */
    @SuppressLint("WorldReadableFiles")
    @SuppressWarnings("deprecation")
    public final boolean install(Activity activity, String filename) {

        if (ExCommonUtil.isEmpty(filename)) {

            return false;
        }

        int result = Settings.Secure.getInt(activity.getContentResolver(), Settings.Secure.INSTALL_NON_MARKET_APPS, 0);

        if (result == 0) {
            ExToastUtil.showShort(R.string.ex_please_allow_app_install);

            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            activity.startActivity(intent);
        } else {

            if (!filename.contains("//sdcard")) {

                String oldname = filename;
                filename = "sharetemp_" + System.currentTimeMillis();

                try {
                    FileInputStream fis = new FileInputStream(oldname);
                    FileOutputStream fos = activity.openFileOutput(filename, Context.MODE_PRIVATE);

                    filename = activity.getFilesDir() + "/" + filename;
                    byte[] buffer = new byte[1024];
                    int len = 0;

                    while ((len = fis.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }

                    buffer = null;
                    fis.close();
                    fis = null;
                    fos.flush();
                    fos.close();
                    fos = null;
                } catch (IOException e) {
                    e.printStackTrace();

                    return false;
                }
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("file://" + filename), "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);

            activity.startActivity(intent);

            return true;
        }

        return false;
    }

    /**
     * Method_卸载应用
     *
     * @param activity    当前 Activity
     * @param packageName 包名
     * @return 结果
     */
    public final boolean uninstall(Activity activity, String packageName) {

        if (ExCommonUtil.isEmpty(packageName)) {

            return false;
        }

        Uri packageURI = Uri.parse("package:" + packageName);
        Intent i = new Intent(Intent.ACTION_DELETE, packageURI);

        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        activity.startActivityForResult(i, 10);

        return true;

    }

    /**
     * Method_运行应用
     *
     * @param activity    当前 Activity
     * @param packageName 包名
     * @param bundle      参数
     * @return 结果
     */
    public final boolean run(Activity activity, String packageName, Bundle bundle) {

        try {
            activity.getPackageManager().getPackageInfo(packageName, PackageManager.SIGNATURE_MATCH);

            try {
                Intent intent = activity.getPackageManager().getLaunchIntentForPackage(packageName);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                if (bundle != null) {
                    intent.putExtras(bundle);
                }

                activity.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

            return false;
        }
    }

    /**
     * Method_运行应用
     *
     * @param activity    当前 Activity
     * @param packageName 包名
     * @return 结果
     */
    public final boolean run(Activity activity, String packageName) {

        return run(activity, packageName, null);
    }

    /**
     * Method_获取包名
     *
     * @return 包名
     */
    public final String getPackageName() {

        try {
            String pkgName = getApplicationContext().getPackageName();
            return ExCommonUtil.dealEmpty(pkgName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Method_获取 Meta 值
     *
     * @param metaKey meta 键
     * @return meta 值
     */
    public final String getMetaValue(String metaKey) {

        Bundle meta = null;
        String value = null;

        if (ExCommonUtil.isEmpty(metaKey)) {
            return "";
        }

        try {
            ApplicationInfo ai = ExDeviceUtil.getInstance().getPM().getApplicationInfo(ExAppUtil.getInstance().getPackageName(), PackageManager.GET_META_DATA);

            if (ai != null) {
                meta = ai.metaData;
            }
            if (meta != null) {
                value = meta.getString(metaKey);
            }

            return ExCommonUtil.dealEmpty(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 截取当前屏幕截图
     * @param activity
     * @return
     */
    public static Bitmap takeScreenShot(Activity activity) {
        try {
            // View是你需要截图的View
            View view = activity.getWindow().getDecorView();
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();
            Bitmap b1 = view.getDrawingCache();

            // 获取状态栏高度
            Rect frame = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;

            // 获取屏幕长和高
            int width = activity.getWindowManager().getDefaultDisplay().getWidth();
            int height = activity.getWindowManager().getDefaultDisplay().getHeight();
            Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
            view.destroyDrawingCache();
            return b;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
