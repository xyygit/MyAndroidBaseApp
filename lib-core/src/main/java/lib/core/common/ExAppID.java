package lib.core.common;

import android.os.Environment;

import java.util.UUID;

import lib.core.ExAppConfig;
import lib.core.utils.ExCommonUtil;
import lib.core.utils.ExDeviceUtil;
import lib.core.utils.ExFileUtil;
import lib.core.utils.ExSharePreferencesUtil;

/**
 * 用于标识设备的唯一ID
 */
public class ExAppID {

    private static String appId;
    private static String uuid;
    private static final String INSTALLATION = "INSTALLATION";
    private static final String INSTALLATIONSD = Environment.getExternalStorageDirectory() + "/INSTALLATION.ng";

    public static String getAppID() {
        return ExCommonUtil.isEmpty(appId) ? generateAppId() : appId;
    }

    private static String generateAppId() {
        String androidId = ExDeviceUtil.getInstance().getAndroidId();
        if (androidId != null && androidId.length() > 15 && !androidId.equals("9774d56d682e549c")) {
            try {
                appId = androidId + "-" + androidId.substring(4, 8)  + "-" + androidId.substring(0, 4) + "-" + androidId.substring(12, 16) + "-" + androidId.substring(8, 12);
            } catch (Exception e) {
                e.printStackTrace();
                appId = androidId;
            }
        } else {
            appId = getUUID();
        }
        return appId;
    }

    private static String getUUID() {
        return ExCommonUtil.isEmpty(uuid) ? generateUUID() : uuid;
    }

    private synchronized static String generateUUID() {
        String uuid = ExSharePreferencesUtil.getInstance().getString(INSTALLATION);
        if (ExCommonUtil.isEmpty(uuid)) {
            if (ExFileUtil.getInstance().existSDcard(ExAppConfig.checkFile)) {
                uuid = ExFileUtil.getInstance().readFileSdcardOrData(INSTALLATIONSD);
                if (ExCommonUtil.isEmpty(uuid)) {
                    uuid = UUID.randomUUID().toString();
                    ExFileUtil.getInstance().writeFileSdcardOrData(INSTALLATIONSD, uuid);
                }
                ExAppID.uuid = uuid;
                ExSharePreferencesUtil.getInstance().putString(INSTALLATION, uuid);
            } else {
                uuid = UUID.randomUUID().toString();
                ExAppID.uuid = uuid;
                ExSharePreferencesUtil.getInstance().putString(INSTALLATION, uuid);
            }
        } else {
            ExAppID.uuid = uuid;
        }
        return uuid;
    }

}
