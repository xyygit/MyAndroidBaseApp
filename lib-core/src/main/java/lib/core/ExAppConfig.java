package lib.core;


import java.io.File;

import lib.core.utils.ExAppUtil;

public abstract class ExAppConfig {

    public static String checkFile;

    public static String appCachePath = ExAppUtil.getApplicationContext().getCacheDir().getAbsolutePath() + File.separator;

    public static String appImageCachePath = ExAppUtil.getApplicationContext().getCacheDir().getAbsolutePath() + File.separator;

    public static String appHttpCachePath = ExAppUtil.getApplicationContext().getCacheDir().getAbsolutePath() + File.separator;

    public static String appLogPath = ExAppUtil.getApplicationContext().getFilesDir().getAbsolutePath() + File.separator;

    public static String appDownloadPath = ExAppUtil.getApplicationContext().getFilesDir().getAbsolutePath() + File.separator;

    public static boolean isLogEnable = true;
}
