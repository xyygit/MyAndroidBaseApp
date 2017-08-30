package lib.core.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Rect;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import lib_core.R;

/**
 *
 * 获取系统相关信息
 */
public class ExDeviceUtil {

    private TelephonyManager tm; // 手机管理
    private WifiManager wm; // 网络管理
    private PackageManager pm; // 包管理
    private DisplayMetrics dm; // 显示管理
    private ActivityManager am; // 界面管理

    private ExDeviceUtil() {
    }

    private static class ExDeviceUtilHolder {
        private static final ExDeviceUtil esu = new ExDeviceUtil();
    }

    public static final ExDeviceUtil getInstance() {
        return ExDeviceUtilHolder.esu;
    }

    /**
     * Method_获取手机管理对象
     *
     * @return 构造管理对象
     */
    public final TelephonyManager getTM() {
        if (tm == null) {
            try {
                tm = (TelephonyManager) ExAppUtil.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return tm;
    }

    /**
     * Method_获取网络管理对象
     *
     * @return 网络管理对象
     */
    public final WifiManager getWM() {
        if (wm == null) {
            try {
                wm = (WifiManager) ExAppUtil.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return wm;
    }

    /**
     * Method_获取包管理对象
     *
     * @return 包管理对象
     */
    public final PackageManager getPM() {
        if (pm == null) {
            pm = ExAppUtil.getApplicationContext().getPackageManager();
        }
        return pm;
    }

    /**
     * Method_获取显示管理对象
     *
     * @return 显示对象
     */
    public final DisplayMetrics getDM() {
        if (dm == null) {
            dm = new DisplayMetrics();
        }
        return dm;
    }

    /**
     * Method_获取界面管理对象
     *
     * @return 界面管理对象
     */
    public final ActivityManager getAM() {
        if (am == null) {
            try {
                am = (ActivityManager) ExAppUtil.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return am;
    }

    /**
     * 获取当前运行的进程
     */
    public final String getCurrentProcessPackageName(Context cxt) {
        if (cxt != null) {
            try {
                ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
                if (runningApps == null) {
                    return "";
                }
                for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
                    if (procInfo.pid == android.os.Process.myPid()) {
                        return procInfo.processName;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 获取设备id
     *
     * @return String
     * @method getDeviceId
     * @author lightning
     */
    public final String getDeviceId() {
        TelephonyManager telephonyManager = getTM();
        return ExCommonUtil.dealEmpty(telephonyManager.getDeviceId());
    }

    /**
     * 获取SIM IMSI编码
     *
     * @return String
     * @method getIMSI
     * @author lightning
     */
    public final String getIMSI() {
        TelephonyManager telephonyManager = getTM();
        return ExCommonUtil.dealEmpty(telephonyManager.getSubscriberId());
    }

    /**
     * Method_获取 IMEI
     *
     * @return SIM
     */
    public final String getIMEI() {

        try {
            String SIM = getTM().getSimSerialNumber();
            return ExCommonUtil.dealEmpty(SIM);
        } catch (Exception e) {
            e.printStackTrace();

            return "";
        }
    }

    /**
     * 获取android Id
     *
     * @return String
     * @method getAndroidId
     * @author lightning
     */
    public final String getAndroidId() {
        String androidId = Settings.Secure.getString(ExAppUtil.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID).toLowerCase();
        return androidId;
    }

    /**
     * 获取系统版本号
     *
     * @return int
     * @method getSystemVerson
     * @author lightning
     */
    public final int getAndroidSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取MAC地址
     *
     * @return
     */
    public final String getLocalMacAddress() {
        WifiManager wifi = getWM();
        WifiInfo info = wifi.getConnectionInfo();
        if (info != null) {
            return info.getMacAddress();
        }
        return "";
    }

    /**
     * 获取IP地址
     *
     * @return
     */
    public final String getLocalIpAddress() {
        //获取wifi服务
        WifiManager wifiManager = getWM();
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = formatToIp(ipAddress);
        if (!ExCommonUtil.isEmpty(ip)) return ip;
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface networkInterface = en.nextElement();
                for (Enumeration<InetAddress> enumeration = networkInterface.getInetAddresses();
                     enumeration.hasMoreElements(); ) {
                    InetAddress inetAddress = enumeration.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return "";
    }

    private String formatToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    public final float getScreenWidth() {
        float screenWidth = 0;
        try {
            DisplayMetrics mDisplayMetrics = new DisplayMetrics();
            ((WindowManager) ExAppUtil.getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(mDisplayMetrics);
            screenWidth = mDisplayMetrics.widthPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenWidth;
    }

    public final float getScreenHeight() {
        float screenHeight = 0;
        try {
            DisplayMetrics mDisplayMetrics = new DisplayMetrics();
            ((WindowManager) ExAppUtil.getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(mDisplayMetrics);
            screenHeight = mDisplayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenHeight;
    }

    public final float getStatusHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    /**
     * Method_获取语言
     *
     * @return 语言
     */
    public final String getLanguage() {

        Locale l = Locale.getDefault();
        return String.format("%s-%s", l.getLanguage(), l.getCountry());
    }

    /**
     * Method_获取手机号码
     *
     * @param context 上下文
     * @return 手机号码
     */
    public final String getPhoneNumber(Context context) {

        try {
            String phoneNo = getTM().getLine1Number();

            return ExCommonUtil.dealEmpty(phoneNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Method_获取手机模式(含CPU)
     *
     * @return 手机模式
     */
    public final String getProductModel() {

        try {
            String productModel = Build.MODEL;
            return ExCommonUtil.dealEmpty(productModel);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Method_获取系统版本
     *
     * @return 系统版本
     */
    public final String getOSVersion() {

        try {
            String osVer = Build.VERSION.RELEASE;
            return ExCommonUtil.dealEmpty(osVer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 判断网络连接
     *
     * @return boolean
     * @method isNetwork
     * @author lightning
     */
    public static boolean isNetworkConnect() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ExAppUtil.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断GPS是否打开
     *
     * @param content
     * @return boolean
     * @method isGPS
     * @author lightning
     */
    public static boolean isGPSOpen(final Activity content) {
        LocationManager locationManager = (LocationManager) content.getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取运营商名称
     *
     * @return
     */
    public final String getSimOperatorInfo() {
        TelephonyManager telephonyManager = getTM();
        String operatorString = telephonyManager.getSimOperator();
        if (operatorString == null) {
            return "";
        }
        if (operatorString.equals("46000") || operatorString.equals("46002")) {
            //中国移动
            return ExAppUtil.getApplicationContext().getString(R.string.ex_yidong);
        } else if (operatorString.equals("46001")) {
            //中国联通
            return ExAppUtil.getApplicationContext().getString(R.string.ex_liantong);
        } else if (operatorString.equals("46003")) {
            //中国电信
            return ExAppUtil.getApplicationContext().getString(R.string.ex_dianxin);
        }
        //error
        return "";
    }

    /**
     * 获取手机App列表
     *
     * @return
     */
    public final String getAPP() {
        StringBuilder appList = new StringBuilder();
        PackageManager pm = ExAppUtil.getApplicationContext().getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 通过查询，获得所有ResolveInfo对象.
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent, 0);
        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(pm));

        for (ResolveInfo reInfo : resolveInfos) {
            String appLabel = (String) reInfo.loadLabel(pm);
            appList.append(appLabel);
            appList.append(",");
        }
        return appList.substring(0, appList.lastIndexOf(","));
    }

    public static final int NETWORN_NONE = 0;
    public static final int NETWORN_WIFI = 1;
    public static final int NETWORN_MOBILE = 2;

    /**
     * 获取网络类型
     *
     * @return
     */
    public final int getNetworkState() {
        ConnectivityManager connManager = (ConnectivityManager) ExAppUtil.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // Wifi
        NetworkInfo.State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
            return NETWORN_WIFI;
        }

        // 3G
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
            return NETWORN_MOBILE;
        }
        return NETWORN_NONE;
    }

    /**
     * 获取联网方式
     *
     * @return
     */
    public static String getNetType() {
        ConnectivityManager connectionManager = (ConnectivityManager) ExAppUtil.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
        if (networkInfo == null) return "offline";
        if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return networkInfo.getExtraInfo();
        } else {
            return networkInfo.getTypeName();
        }
    }

    private static String resolution;

    /**
     * 获取设备分辨率
     *
     * @return
     */
    public final String getDeviceResolution() {
        if (ExCommonUtil.isEmpty(resolution)) {
            try {
                WindowManager wm = (WindowManager) ExAppUtil.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                Display mDisplay = wm.getDefaultDisplay();
                resolution = mDisplay.getWidth() + "x" + mDisplay.getHeight();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resolution;
    }

    /**
     * 检查是否有虚拟按键
     *
     * @param context
     * @return
     */
    public final boolean checkDeviceHasNavigationBar(Context context) {
        if (context == null) return false;
        boolean hasNavigationBar = false;
        try {
            Resources rs = context.getResources();
            int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
            if (id > 0) {
                hasNavigationBar = rs.getBoolean(id);
            }

            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
        }

        return hasNavigationBar;

    }

    /**
     * 获取虚拟按键的高度
     *
     * @return
     */
    public final int getVirtualHeight(Context context) {
        if (context == null) return 0;
        int navigationBarHeight = 0;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0 && checkDeviceHasNavigationBar(context)) {
            navigationBarHeight = rs.getDimensionPixelSize(id);
        }
        return navigationBarHeight;
    }

    /**
     * 判断是否为模拟器
     *
     * @return
     */
    public boolean isEmulator() {
        try {
            String imei = getTM().getDeviceId();
            if (imei != null && imei.equals("000000000000000")) {
                return true;
            }
            return (Build.MODEL.equals("sdk")) || (Build.MODEL.equals("google_sdk"));
        } catch (Exception ioe) {

        }
        return true;
    }

}
