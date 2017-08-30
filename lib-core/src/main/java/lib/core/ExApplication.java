package lib.core;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.webkit.CookieSyncManager;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;

import java.io.File;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import lib.core.common.AppCrashHandler;
import lib.core.receiver.ExNetReceiver;
import lib.core.utils.ExAppUtil;
import lib.core.utils.ExCommonUtil;
import lib.core.utils.ExDeviceUtil;
import lib.core.utils.ExFileUtil;
import lib.ext.fresco.AppCacheKeyFactory;
import lib.ext.fresco.BitmapMemoryCacheSupplier;

public class ExApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        AppCrashHandler.create();

        String processName = ExDeviceUtil.getInstance().getCurrentProcessPackageName(this);
        String packageName = getPackageName();
        if (!packageName.equals(processName)) return;

        mContext = getApplicationContext();
        resetApplicationContext();

        initApp();

    }

    protected void initApp() {
        configAppPath();

        CookieSyncManager.createInstance(this);
        frescoInitialize();

        ExNetReceiver.getInstance().register();
    }

    protected void configAppPath() {
        File cacheFile, imageCacheFile, httpCacheFile, logFile, downloadFile;
        File cacheDir = getExternalCacheDir();
        if(!ExCommonUtil.isEmpty(cacheDir)) {
            ExAppConfig.checkFile = cacheDir.getAbsolutePath()+ "/check.tmp";
        }
        if (!ExCommonUtil.isEmpty(cacheDir) && ExFileUtil.getInstance().existSDcard(ExAppConfig.checkFile)) {
            cacheFile = new File(cacheDir.getAbsolutePath());
            imageCacheFile = new File(getExternalFilesDir("img").getAbsolutePath());
            httpCacheFile = new File(getExternalFilesDir("http").getAbsolutePath());
            logFile = new File(getExternalFilesDir("log").getAbsolutePath());
            downloadFile = new File(getExternalFilesDir("download").getAbsolutePath());
        } else {
            cacheFile = new File(ExAppUtil.getApplicationContext().getCacheDir().getAbsolutePath());
            imageCacheFile = new File(ExAppUtil.getApplicationContext().getFilesDir().getAbsolutePath() + File.separator + "img");
            httpCacheFile = new File(ExAppUtil.getApplicationContext().getFilesDir().getAbsolutePath() + File.separator + "http");
            logFile = new File(ExAppUtil.getApplicationContext().getFilesDir().getAbsolutePath() + File.separator + "log");
            downloadFile = new File(ExAppUtil.getApplicationContext().getFilesDir().getAbsolutePath() + File.separator + "download");
        }

        ExAppConfig.appCachePath = cacheFile.getAbsolutePath();
        ExAppConfig.appImageCachePath = imageCacheFile.getAbsolutePath();
        ExAppConfig.appHttpCachePath = httpCacheFile.getAbsolutePath();
        ExAppConfig.appLogPath = logFile.getAbsolutePath();
        ExAppConfig.appDownloadPath = downloadFile.getAbsolutePath();

        if (!cacheFile.exists()) {
            cacheFile.mkdirs();
        }

        if (!imageCacheFile.exists()) {
            imageCacheFile.mkdirs();
        }

        if (!httpCacheFile.exists()) {
            httpCacheFile.mkdirs();
        }

        if (!logFile.exists()) {
            logFile.mkdirs();
        }

        if (!downloadFile.exists()) {
            downloadFile.mkdirs();
        }
    }

    public static void resetApplicationContext() {
        ExAppUtil.getInstance().setContext(mContext);
    }

    /**
     * fresco initialize
     */
    private void frescoInitialize() {
        ImagePipelineConfig.Builder configBuilder = ImagePipelineConfig.newBuilder(this);

        // 设置缓存配置
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(this)
                .setBaseDirectoryPath(new File(ExAppConfig.appImageCachePath))
                .setBaseDirectoryName("frescoImage")
                .setMaxCacheSize(50 * ByteConstants.MB)
                .build();

        configBuilder.setMainDiskCacheConfig(diskCacheConfig);
        configBuilder.setCacheKeyFactory(new AppCacheKeyFactory());

        // 设置请求监听器配置
        Set<RequestListener> requestListeners = new HashSet<>();
        requestListeners.add(new RequestLoggingListener());
        configBuilder.setRequestListeners(requestListeners);

        configBuilder.setDownsampleEnabled(true);
        configBuilder.experiment().setWebpSupportEnabled(true);

        //当内存紧张时采取的措施
        MemoryTrimmableRegistry memoryTrimmableRegistry = NoOpMemoryTrimmableRegistry.getInstance();
        memoryTrimmableRegistry.registerMemoryTrimmable(new MemoryTrimmable() {
            @Override
            public void trim(MemoryTrimType trimType) {
                final double suggestedTrimRatio = trimType.getSuggestedTrimRatio();

                if (MemoryTrimType.OnCloseToDalvikHeapLimit.getSuggestedTrimRatio() == suggestedTrimRatio
                        || MemoryTrimType.OnSystemLowMemoryWhileAppInBackground.getSuggestedTrimRatio() == suggestedTrimRatio
                        || MemoryTrimType.OnSystemLowMemoryWhileAppInForeground.getSuggestedTrimRatio() == suggestedTrimRatio
                        ) {
                    //清除内存缓存
                    Fresco.getImagePipeline().clearMemoryCaches();
                }
            }
        });
        configBuilder.setMemoryTrimmableRegistry(memoryTrimmableRegistry);
        configBuilder.setBitmapMemoryCacheParamsSupplier(new BitmapMemoryCacheSupplier());

        Fresco.initialize(this, configBuilder.build());
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        try {
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            if (imagePipeline != null) {
                imagePipeline.clearMemoryCaches();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setLanguage(Locale locale) {
        if (locale == null){
            return;
        }
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config, null);
    }
}
