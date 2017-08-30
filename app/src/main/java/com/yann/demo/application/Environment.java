package com.yann.demo.application;

import com.alibaba.fastjson.JSON;
import com.yann.demo.BuildConfig;
import com.yann.demo.R;
import com.yann.demo.common.bean.NetConfig;

import java.io.InputStream;

import lib.core.common.ExThread;
import lib.core.http.*;
import lib.core.utils.ExAppUtil;
import lib.core.utils.ExCommonUtil;
import lib.core.utils.ExFileUtil;
import lib.core.utils.ExSharePreferencesUtil;
import okio.Buffer;

/**
 * 应用环境管理
 * Created by yayun.xia on 2017/8/30.
 */

public class Environment {
    public static final String APP_ENV_PLATFORM_FN = "a"; // 平台标识: a
    public static final String APP_ENV_PLATFORM_RT = "ar"; // 平台标识: ar
    public static String platform = APP_ENV_PLATFORM_FN;

    public static final String APP_ENV_CODE_MOCK = "Mock"; // 应用环境标识: mock
    public static final String APP_ENV_CODE_DEV = "Dev"; // 应用环境标识: dev
    public static final String APP_ENV_CODE_BETA = "Beta"; // 应用环境标识: beta
    public static final String APP_ENV_CODE_PREVIEW = "Preview"; // 应用环境标识: preview
    public static final String APP_ENV_CODE_ONLINE = "Online"; // 应用环境标识 online
    public static String apiConfigOfEnv = BuildConfig.apiConfigOfEnv;

    public static final String APP_ENV_DEV_FILE = "app_config_of_dev"; // dev assets
    public static final String APP_ENV_BETA_FILE = "app_config_of_beta"; // beta assets
    public static final String APP_ENV_PREVIEW_FILE = "app_config_of_preview"; // preview assets
    public static final String APP_ENV_ONLINE_FILE = "app_config_of_online"; // online assets

    public static final String APP_ENV_MOCK_CONFIG_URL = "http://10.200.242.147:8080/api/getConfig";
    public static final String APP_ENV_DEV_CONFIG_URL = "http://membase-yxapp01.dev1.fn/config/getConfig/" + platform + BuildConfig.apiVersion; // dev url
    public static final String APP_ENV_BETA_CONFIG_URL = "http://membase-yxapp.beta1.fn/config/getConfig/" + platform + BuildConfig.apiVersion; // beta url
    public static final String APP_ENV_PREVIEW_CONFIG_URL = "http://preview-membase-yxapp.feiniu.com/config/getConfig/" + platform + BuildConfig.apiVersion; // preview url
    public static final String APP_ENV_ONLINE_CONFIG_URL = "http://membase-yxapp.feiniu.com/config/getConfig/" + platform + BuildConfig.apiVersion; // online url

    private static Environment environment;

    private Environment() {
        String evn = ExSharePreferencesUtil.getInstance().getString(Constants.SharePreferences.APP_SHARE_PRE_CURRENT_EVN);
        if(!ExCommonUtil.isEmpty(evn) && !APP_ENV_CODE_ONLINE.equals(apiConfigOfEnv)) {
            apiConfigOfEnv = evn;
        }
    }

    public static Environment getInstance() {
        if (environment == null) {
            environment = new Environment();
        }
        return environment;
    }

    public static InputStream[] certificates;
    public static InputStream bksFile;

    public static InputStream[] getCertificates() {
        if(ExCommonUtil.isEmpty(certificates)) {
            String certificate = "-----BEGIN CERTIFICATE-----\n" +
                    "MIIFBDCCA+ygAwIBAgIQSLcEuHKK1TkYBmakj2c0hjANBgkqhkiG9w0BAQsFADBE\n" +
                    "MQswCQYDVQQGEwJVUzEWMBQGA1UEChMNR2VvVHJ1c3QgSW5jLjEdMBsGA1UEAxMU\n" +
                    "R2VvVHJ1c3QgU1NMIENBIC0gRzMwHhcNMTUwOTA3MDAwMDAwWhcNMTgwOTA2MjM1\n" +
                    "OTU5WjCBiTELMAkGA1UEBhMCQ04xDzANBgNVBAgMBuS4iua1tzEPMA0GA1UEBwwG\n" +
                    "5LiK5rW3MS0wKwYDVQQKDCTkuIrmtbfmtqblm73kv6Hmga/mioDmnK/mnInpmZDl\n" +
                    "hazlj7gxEjAQBgNVBAsMCeaKgOacr+mDqDEVMBMGA1UEAwwMKi5mZWluaXUuY29t\n" +
                    "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoJsgthlO9EzpHA7j3bAE\n" +
                    "bnpc/JCWmrYssjekMYGfoPE+lrDxKz3jDuUWvVFKwZWWXt/vpj+QgSUFkSPjdQDA\n" +
                    "BpLPDSxfVqRc6YE6i/p6hvp/HJFt5w8ah+pZtsx+SVaVKynGSwxm/q3BnI1ihQ7Y\n" +
                    "lgCOsFouXbd51ibaas2k3r4rzbYUXQihBm2ICWnyfI3SUhAkLpJ80ltkrvZNyn5n\n" +
                    "CSb7gnoGTrb0GurmtWkamAiVPYQjL8Nw1Sby4O6wafZVXXSgqwT3LqChZ6fVW04S\n" +
                    "oaQFUN/AfrEcxwdLzZDRksi/StaLfmZ/OxbdZB5WNP+6yw3yJiRkvuKv13JTPrvA\n" +
                    "pQIDAQABo4IBqjCCAaYwIwYDVR0RBBwwGoIMKi5mZWluaXUuY29tggpmZWluaXUu\n" +
                    "Y29tMAkGA1UdEwQCMAAwDgYDVR0PAQH/BAQDAgWgMCsGA1UdHwQkMCIwIKAeoByG\n" +
                    "Gmh0dHA6Ly9nbi5zeW1jYi5jb20vZ24uY3JsMIGdBgNVHSAEgZUwgZIwgY8GBmeB\n" +
                    "DAECAjCBhDA/BggrBgEFBQcCARYzaHR0cHM6Ly93d3cuZ2VvdHJ1c3QuY29tL3Jl\n" +
                    "c291cmNlcy9yZXBvc2l0b3J5L2xlZ2FsMEEGCCsGAQUFBwICMDUMM2h0dHBzOi8v\n" +
                    "d3d3Lmdlb3RydXN0LmNvbS9yZXNvdXJjZXMvcmVwb3NpdG9yeS9sZWdhbDAdBgNV\n" +
                    "HSUEFjAUBggrBgEFBQcDAQYIKwYBBQUHAwIwHwYDVR0jBBgwFoAU0m/3lvSFP3I8\n" +
                    "MH0j2oV4m6N8WnwwVwYIKwYBBQUHAQEESzBJMB8GCCsGAQUFBzABhhNodHRwOi8v\n" +
                    "Z24uc3ltY2QuY29tMCYGCCsGAQUFBzAChhpodHRwOi8vZ24uc3ltY2IuY29tL2du\n" +
                    "LmNydDANBgkqhkiG9w0BAQsFAAOCAQEAVyA83jI0S/w9pnEgKJkUWJeovwf7hcG1\n" +
                    "5BaBXhV5OR6KquKhRVmyWFnJuf/dQ0zw8BORk5rG8ve/vXqPe0qIKTANkEQN7QI7\n" +
                    "rRw4DmfX0qJfWA9mWKiRPoQDavQ8sUFil9wx1OQIeIAkFnDFn4FuOmmZSRrgjjDl\n" +
                    "3VjmPjjioiEZ2UXJeIKMiQOfln+du7zh1x6Ty+f0gm8jgxWTRh47kp0eg+fu6qZa\n" +
                    "Y6hKpAoxoDB5QY79ZGqmsXTck/uIjJPckhFhtzKa46kI722kA0Uf19xY48OJj0N8\n" +
                    "Bk1ecOx+SmnADSGsuBwXlrhUpkb4WWlm4uIlMaCXd/vciX3pV/BBPQ==\n" +
                    "-----END CERTIFICATE-----";
            certificates = new InputStream[1];
            certificates[0] = new Buffer().writeUtf8(certificate).inputStream();
        }
        return certificates;
    }

    public static InputStream getBKSFile() {
        if(ExCommonUtil.isEmpty(bksFile)) {
            bksFile = ExAppUtil.getApplicationContext().getResources().openRawResource(R.raw.yannssl);
        }
        return bksFile;
    }

    public static String getAuthorityPassword() {
        return "feiniuapp";
    }


    public String getEnvironmentConfigAssets() {
        if (APP_ENV_CODE_DEV.equals(apiConfigOfEnv)) {
            return "api_"+ platform + "/" + APP_ENV_DEV_FILE;
        } else if (APP_ENV_CODE_BETA.equals(apiConfigOfEnv)) {
            return "api_"+ platform + "/" +APP_ENV_BETA_FILE;
        } else if (APP_ENV_CODE_PREVIEW.equals(apiConfigOfEnv)) {
            return "api_"+ platform + "/" +APP_ENV_PREVIEW_FILE;
        } else {
            return "api_"+ platform + "/" +APP_ENV_ONLINE_FILE;
        }
    }

    public void loadAPIConfig(final ResponseCallback callback) {
        if (Constants.hasNetConfig()) {
            ExThread.getInstance().executeByUIDelay(new Runnable() {
                @Override
                public void run() {
                    AdminUser.getInstance().getToken(callback);
                }
            }, 1000);
            return;
        }
        HttpRequest.Builder builder = new HttpRequest.Builder(getConfigURL());
        builder.setHasNoParams(true);
        builder.setConvertClass(NetConfig.class);
        builder.addCallback(new ResponseCallback<NetConfig>() {

            @Override
            public void onSucceed(int what, NetConfig result) {
                super.onSucceed(what, result);
                if (result != null) {
                    if (result.wirelessAPI != null) {
                        Constants.setNetConfig(result);
                    } else {
                        loadNativeConfig();
                    }
                } else {
                    loadNativeConfig();
                }
                AdminUser.getInstance().getToken(callback);
            }

            @Override
            public void onFailed(int what, int responseCode, String errorMsg) {
                super.onFailed(what, responseCode, errorMsg);
                loadNativeConfig();
                AdminUser.getInstance().getToken(callback);
            }
        });
        builder.build().request();
    }

    public void loadNativeConfig() {
        String content = ExFileUtil.getInstance().readFileFromAssets(Environment.getInstance().getEnvironmentConfigAssets());
        if (!ExCommonUtil.isEmpty(content)) {
            try {
                NetConfig netConfig = JSON.parseObject(content, NetConfig.class);
                Constants.setNetConfig(netConfig.body);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getConfigURL() {
        if (APP_ENV_CODE_DEV.equals(apiConfigOfEnv)) {
            return APP_ENV_DEV_CONFIG_URL;
        } else if (APP_ENV_CODE_BETA.equals(apiConfigOfEnv)) {
            return APP_ENV_BETA_CONFIG_URL;
        } else if (APP_ENV_CODE_PREVIEW.equals(apiConfigOfEnv)) {
            return APP_ENV_PREVIEW_CONFIG_URL;
        } else if (APP_ENV_CODE_MOCK.equals(apiConfigOfEnv)) {
            return APP_ENV_MOCK_CONFIG_URL;
        } else {
            return APP_ENV_ONLINE_CONFIG_URL;
        }
    }


    public void loadAPIConfig() {
        HttpRequest.Builder builder = new HttpRequest.Builder(getConfigURL());
        builder.setHasNoParams(true);
        builder.setConvertClass(NetConfig.class);
        builder.addCallback(new ResponseCallback<NetConfig>() {

            @Override
            public void onSucceed(int what, NetConfig result) {
                super.onSucceed(what, result);
                if (result != null) {
                    if (result.wirelessAPI != null) {
                        Constants.setNetConfig(result);
                    } else {
                        loadNativeConfig();
                    }
                } else {
                    loadNativeConfig();
                }
                AdminUser.getInstance().getToken(null);
            }

            @Override
            public void onFailed(int what, int responseCode, String errorMsg) {
                super.onFailed(what, responseCode, errorMsg);
                loadNativeConfig();
                AdminUser.getInstance().getToken(null);
            }
        });
        builder.build().request();
    }
}
