package com.yann.demo.application;

import android.graphics.Bitmap;
import android.support.v4.util.ArrayMap;

import com.alibaba.fastjson.JSON;
import com.yann.demo.BuildConfig;
import com.yann.demo.common.bean.NetCode;
import com.yann.demo.common.bean.NetResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import lib.core.common.ExAppID;
import lib.core.http.BitmapCallback;
import lib.core.http.CacheMode;
import lib.core.http.FileCallback;
import lib.core.http.HttpCache;
import lib.core.http.JHttp;
import lib.core.http.RequestConfig;
import lib.core.http.RequestMethod;
import lib.core.http.ResponseCallback;
import lib.core.http.definition.HttpCallback;
import lib.core.utils.ExAppUtil;
import lib.core.utils.ExCommonUtil;
import lib.core.utils.ExDeviceUtil;
import lib.core.utils.ExLogUtil;
import lib.core.utils.ExToastUtil;


public class HttpRequest {

    private ArrayMap<String, Object> params = new ArrayMap<String, Object>();
    private ArrayMap<String, Object> data = new ArrayMap<String, Object>();
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    private Builder builder;

    private HttpRequest(Builder builder) {
        this.builder = builder;
        data.put("apiVersion", "a" + BuildConfig.VERSION_NAME);
        data.put("appVersion", ExAppUtil.getInstance().getVersionName());
        data.put("channel", ExAppUtil.getInstance().getMetaValue("UMENG_CHANNEL"));
        data.put("deviceId", ExAppID.getAppID());
        data.put("isSimulator", ExDeviceUtil.getInstance().isEmulator());
        data.put("networkType", ExDeviceUtil.getInstance().getNetType());
        data.put("reRule", getDeviceRule());
        data.put("viewSize", ExDeviceUtil.getInstance().getDeviceResolution());
        data.put("osType", 1);
        data.put("httpsEnable", 1);
    }

    private ResponseCallback responseCallback;

    public void setResponseCallback(ResponseCallback responseCallback) {
        this.responseCallback = responseCallback;
    }

    public static final class Builder {
        private HttpRequest request;
        String url;
        ArrayMap<String, Object> params;
        int what;
        HttpCallback httpCallback;
        boolean hasNoParams;
        Class cls;
        int method = -1;
        ArrayList<Integer> cacheStrategy;
        long persistTime;
        File file;
        File[] files;
        Object pageInstance;
        String track = null;
        private boolean needProgress;
        boolean noCache = false;

        public Builder(String url) {
            this.url = url;
        }

        public void url(String url) {
            this.url = url;
        }

        public void setParams(ArrayMap<String, Object> params) {
            this.params = params;
        }

        public void what(int what) {
            this.what = what;
        }

        public void addCallback(HttpCallback httpCallback) {
            this.httpCallback = httpCallback;
        }

        public void setHasNoParams(boolean hasNoParams) {
            this.hasNoParams = hasNoParams;
        }

        public void setConvertClass(Class cls) {
            this.cls = cls;
        }

        public void method(int method) {
            this.method = method;
        }

        public void setPersistTime(long persistTime) {
            this.persistTime = persistTime;
        }

        public void file(File file) {
            this.file = file;
        }

        public void file(File[] files) {
            this.files = files;
        }

        public void setPageInstance(Object pageInstance) {
            this.pageInstance = pageInstance;
        }

        public void track(String track) {
            this.track = track;
        }

        public void needProgress(boolean needProgress) {
            this.needProgress = needProgress;
        }

        public void cache(int cacheMode) {
            if(cacheMode == CacheMode.CACHE_NO) {
                noCache = true;
            } else {
                if (cacheStrategy == null) {
                    cacheStrategy = new ArrayList<Integer>();
                }
                cacheStrategy.add(cacheMode);
            }
        }

        public HttpRequest build() {
            request = new HttpRequest(this);
            return request;
        }
    }

    public lib.core.http.HttpRequest request() {
        ExLogUtil.e(builder.url);
        if (!ExDeviceUtil.isNetworkConnect()) {
            ExToastUtil.showLong("网络不给力，请重试");
            if(builder.httpCallback != null) {
                builder.httpCallback.onFailed(0, 0, "网络不给力，请重试", null);
                builder.httpCallback.onResponseFinish(0);
            }
            return null;
        }

        if(builder.httpCallback instanceof BitmapCallback) {
            builder.hasNoParams = true;
        }

        if (builder.hasNoParams) {
            params = null;
        } else {
            data.put("addrId","");
            data.put("time", timeFormat.format(new Date(System.currentTimeMillis())));
            data.put("token", "2015bfa922204bbb5322d5209559572b");
            data.put("body", builder.params);
            String result = JSON.toJSONString(data);
            if (ExCommonUtil.isEmpty(params)) {
                params = new ArrayMap<String, Object>();
            }
            params.put("data", result);
            params.put("paramsMD5", getParamMD5(result));

            ExLogUtil.e(builder.url + " request", result);

            if (!ExCommonUtil.isEmpty(builder.file)) {
                params.put("userfile", builder.file);
            }
            if (!ExCommonUtil.isEmpty(builder.files)) {
                params.put("userfile", builder.files);
            }
        }

        int method = RequestMethod.POST_FORM;

        if(builder.httpCallback instanceof BitmapCallback) {
            method = RequestMethod.GET;
            if(!builder.noCache)builder.cache(CacheMode.CACHE_LOAD_FIRST);
        }

        if (builder.method != -1) {
            method = builder.method;
        }

        RequestConfig requestConfig = configRequest();

        setRequest(builder.pageInstance, builder.request);

        JHttp.Builder jbuilder = new JHttp.Builder(builder.url);
        jbuilder.method(method);
        jbuilder.setParams(params);
        jbuilder.what(builder.what);
        jbuilder.setRequestConfig(requestConfig);
        jbuilder.addCallback(builder.httpCallback instanceof BitmapCallback ? new BitmapCallback(){

            @Override
            public void onSucceed(int what, Bitmap result) {
                super.onSucceed(what, result);
                if(builder.httpCallback != null)((BitmapCallback) builder.httpCallback).onSucceed(what, result);
            }

            @Override
            public void onFailed(int what, int responseCode, String errorMsg, Bitmap cacheResult) {
                super.onFailed(what, responseCode, errorMsg, cacheResult);
                if(builder.httpCallback != null)((BitmapCallback) builder.httpCallback).onFailed(what, responseCode, errorMsg, cacheResult);
            }

        } : new HttpCallback() {
            @Override
            public void onRequestStart(int what) {
                if(builder.httpCallback != null)builder.httpCallback.onRequestStart(what);
            }

            @Override
            public void onSucceed(int what, Object result) {
                if (result == null) return;
                ExLogUtil.e(builder.url + " response", result.toString());
                NetResponse response = null;
                if (builder.cls != null) {
                    try {
                        response = (NetResponse) JSON.parseObject(result.toString(), builder.cls);
                    } catch (Exception e) {
                        ExLogUtil.e(e.getMessage());
                        e.printStackTrace();
                    }
                }

                if (response == null) {
                    if(builder.httpCallback != null)builder.httpCallback.onSucceed(what, result);
                    if(responseCallback != null)responseCallback.onSucceed(what, result);
                    return;
                }
                switch (response.errorCode) {
                    case NetCode.SUCCESS:
                        if(builder.httpCallback != null)builder.httpCallback.onSucceed(what, response.body);
                        if(responseCallback != null)responseCallback.onSucceed(what, response.body);
                        break;
                    case NetCode.ERROR_CORD_PASSWORD_CHANGE:
                    case NetCode.ERROR_CODE_BLACK_ACCOUNT:
                        try {
                            loginOutToLogin(response.errorDesc, "下线提示");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case NetCode.ERROR_CODE_LOGIN_EXPIRED:
                        loginOutToLogin("您的登录信息过期，点击“确定”重新登录", "下线提示");
                        break;
                    default:
                        if(builder.httpCallback != null)builder.httpCallback.onFailed(what, response.errorCode, response.errorDesc, response.body);
                        if(responseCallback != null)responseCallback.onFailed(what, response.errorCode, response.errorDesc, response.body);
                        if (!ExCommonUtil.isEmpty(builder.pageInstance) && ExCommonUtil.isEmpty(response.body)) {
                            showNoDataPage(builder.pageInstance, builder.request);
                        }
                }
            }

            @Override
            public void onFailed(int what, int responseCode, String errorMsg, Object cacheResult) {
                if(builder.httpCallback != null && !ExCommonUtil.isEmpty(errorMsg))builder.httpCallback.onFailed(what, responseCode, errorMsg, cacheResult);
                if(responseCallback != null)responseCallback.onFailed(what, responseCode, errorMsg, cacheResult);
                if (!ExCommonUtil.isEmpty(builder.pageInstance) && responseCallback == null) {
                    showNoDataPage(builder.pageInstance, builder.request);
                }
            }

            @Override
            public void onResponseFinish(int what) {
                if(builder.httpCallback != null)builder.httpCallback.onResponseFinish(what);
            }

            @Override
            public void onProgress(long progress, long length, boolean done) {
                if(builder.httpCallback != null)builder.httpCallback.onProgress(progress, length, done);
            }
        });

        return jbuilder.build().request();
    }

    public lib.core.http.HttpRequest requestTrack() {

        if (!ExDeviceUtil.isNetworkConnect()) {
            builder.httpCallback.onFailed(0, 0, "", null);
            return null;
        }

        if(!ExCommonUtil.isEmpty(builder.track)) {
            if (ExCommonUtil.isEmpty(params)) {
                params = new ArrayMap<String, Object>();
            }
            params.put("data", builder.track);
        }

        RequestConfig requestConfig = configRequest();

        JHttp.Builder jbuilder = new JHttp.Builder(builder.url);
        jbuilder.method(RequestMethod.POST_FORM);
        jbuilder.setParams(params);
        jbuilder.what(builder.what);
        jbuilder.setRequestConfig(requestConfig);
        jbuilder.addCallback(new HttpCallback() {
            @Override
            public void onRequestStart(int what) {
                builder.httpCallback.onRequestStart(what);
            }

            @Override
            public void onSucceed(int what, Object result) {
                if (result == null) return;
                NetResponse response = null;
                if (builder.cls != null) {
                    try {
                        response = (NetResponse) JSON.parseObject(result.toString(), builder.cls);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (response == null) {
                    builder.httpCallback.onSucceed(what, result);
                    return;
                }
                switch (response.errorCode) {
                    case NetCode.SUCCESS:
                        builder.httpCallback.onSucceed(what, response.body);
                        break;
                    default:
                        builder.httpCallback.onFailed(what, response.errorCode, response.errorDesc, response.body);
                }
            }

            @Override
            public void onFailed(int what, int responseCode, String errorMsg, Object cacheResult) {
                builder.httpCallback.onFailed(what, responseCode, errorMsg, cacheResult);
            }

            @Override
            public void onResponseFinish(int what) {
                builder.httpCallback.onResponseFinish(what);
            }

            @Override
            public void onProgress(long progress, long length, boolean done) {
                builder.httpCallback.onProgress(progress, length, done);
            }
        });

        return jbuilder.build().requestTrack();
    }

    public lib.core.http.HttpRequest requestExternal() {

        if (!ExDeviceUtil.isNetworkConnect()) {
            ExToastUtil.showLong("网络不给力，请稍后重试！");
            if(builder.httpCallback != null) {
                builder.httpCallback.onFailed(0, 0, "", null);
                builder.httpCallback.onResponseFinish(0);
            }
            return null;
        }

        RequestConfig requestConfig = configRequest();

        JHttp.Builder jbuilder = new JHttp.Builder(builder.url);
        jbuilder.method(builder.method);
        jbuilder.setParams(builder.params);
        jbuilder.what(builder.what);
        jbuilder.setRequestConfig(requestConfig);
        jbuilder.addCallback(new HttpCallback() {
            @Override
            public void onRequestStart(int what) {
                builder.httpCallback.onRequestStart(what);
            }

            @Override
            public void onSucceed(int what, Object result) {
                if (result == null) return;
                NetResponse response = null;
                if (response == null) {
                    builder.httpCallback.onSucceed(what, result);
                    return;
                }
            }

            @Override
            public void onFailed(int what, int responseCode, String errorMsg, Object cacheResult) {
                builder.httpCallback.onFailed(what, responseCode, errorMsg, cacheResult);
            }

            @Override
            public void onResponseFinish(int what) {
                builder.httpCallback.onResponseFinish(what);
            }

            @Override
            public void onProgress(long progress, long length, boolean done) {
                builder.httpCallback.onProgress(progress, length, done);
            }
        });

        return jbuilder.build().request();
    }

    public lib.core.http.HttpRequest downloadFile() {

        if (!ExDeviceUtil.isNetworkConnect()) {
            ExToastUtil.showLong("网络不给力，请稍后重试！");
            if(builder.httpCallback != null) {
                builder.httpCallback.onFailed(0, 0, "", null);
                builder.httpCallback.onResponseFinish(0);
            }
            return null;
        }

        RequestConfig requestConfig = configRequest();

        JHttp.Builder jbuilder = new JHttp.Builder(builder.url);
        jbuilder.method(builder.method);
        jbuilder.setParams(builder.params);
        jbuilder.what(builder.what);
        jbuilder.setRequestConfig(requestConfig);
        jbuilder.addCallback(new FileCallback() {
            @Override
            public void onRequestStart(int what) {
                builder.httpCallback.onRequestStart(what);
            }

            @Override
            public void onSucceed(int what, Object result) {
                if (result == null) return;
                NetResponse response = null;
                if (response == null) {
                    builder.httpCallback.onSucceed(what, result);
                    return;
                }
            }

            @Override
            public void onFailed(int what, int responseCode, String errorMsg, Object cacheResult) {
                builder.httpCallback.onFailed(what, responseCode, errorMsg, cacheResult);
            }

            @Override
            public void onResponseFinish(int what) {
                builder.httpCallback.onResponseFinish(what);
            }

            @Override
            public void onProgress(long progress, long length, boolean done) {
                builder.httpCallback.onProgress(progress, length, done);
            }
        });

        return jbuilder.build().request();
    }

    // 基准屏幕密度
    private static final float BASE_DENSITYDPI = 160;

    /**
     * 目前图片服务器切图使用的几个倍率
     */
    private static final int DENSITY_RATE_BASE = 1;
    private static final float DENSITY_RATE_1_5_TIMES = 1.5f;
    private static final int DENSITY_RATE_2_TIMES = 2;
    private static final int DENSITY_RATE_3_TIMES = 3;
    private static final int DENSITY_RATE_4_TIMES = 4;

    /**
     * 获取设备屏幕密度相对倍率 （用于提供给api，针对不同手机屏幕密度取回不同尺寸图标）
     */
    private static String getDeviceRule() {

        int densityDpi = ExAppUtil.getApplicationContext().getResources().getDisplayMetrics().densityDpi;

        float tmpRule = densityDpi / BASE_DENSITYDPI;

        String mRule = String.valueOf(DENSITY_RATE_4_TIMES);

        if (tmpRule <= DENSITY_RATE_BASE) {

            mRule = String.valueOf(DENSITY_RATE_BASE);

        } else if (tmpRule > DENSITY_RATE_BASE && tmpRule <= DENSITY_RATE_1_5_TIMES) {

            mRule = String.valueOf(DENSITY_RATE_1_5_TIMES);

        } else if (tmpRule > DENSITY_RATE_1_5_TIMES && tmpRule <= DENSITY_RATE_2_TIMES) {

            mRule = String.valueOf(DENSITY_RATE_2_TIMES);

        } else if (tmpRule > DENSITY_RATE_2_TIMES && tmpRule <= DENSITY_RATE_3_TIMES) {

            mRule = String.valueOf(DENSITY_RATE_3_TIMES);

        } else if (tmpRule > DENSITY_RATE_3_TIMES) {

            mRule = String.valueOf(DENSITY_RATE_4_TIMES);
        }

        return mRule;
    }

    public void cancel() {
        builder.addCallback(null);
        JHttp.cancelByUrl(builder.url);
    }

    public static void cancelByUrl(String url) {
        JHttp.cancelByUrl(url);
    }

    public static void cancelByTag(Object tag) {
        JHttp.cancelByTag(tag);
    }

    public static String getParamMD5(String result) {
        try {
            JSONObject obj = new JSONObject(result);
            boolean isSimulator = false;
            if (obj.has("isSimulator")) {
                isSimulator = obj.getBoolean("isSimulator");
            }

            String view_size = "";
            if (obj.has("viewSize")) {
                view_size = obj.getString("viewSize");
            }

            String networkType = "";
            if (obj.has("networkType")) {
                networkType = obj.getString("networkType");
            }

            String time = "";
            if (obj.has("time")) {
                time = obj.getString("time");
            }
            String extString = isSimulator + view_size + networkType + time;
            return ExCrypto.encrypt(result + extString);
        } catch (JSONException e) {
        }
        return ExCrypto.encrypt(result);
    }

    public static class ExCrypto {

        public static final String encrypt(String input) {
            String result = null;
            String salt = "@456yx#*^&HrUU99";
            if (Environment.APP_ENV_CODE_DEV.equals(Environment.apiConfigOfEnv) || Environment.APP_ENV_CODE_BETA.equals(Environment.apiConfigOfEnv)) {
                salt = "@yx123*&^DKJ##CC";
            }

            try {
                byte[] key = salt.getBytes();
                SecretKey restoreSecretKey = new SecretKeySpec(key, "HmacSHA256");
                Mac mac = Mac.getInstance(restoreSecretKey.getAlgorithm());
                mac.init(restoreSecretKey);
                result = android.util.Base64.encodeToString(mac.doFinal(input.getBytes()), android.util.Base64.NO_WRAP);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    private RequestConfig configRequest() {
        RequestConfig requestConfig = new RequestConfig();
        if (!ExCommonUtil.isEmpty(builder.cacheStrategy)) {
            String result = "";
            if(!ExCommonUtil.isEmpty(builder.params))result = JSON.toJSONString(builder.params);
            HttpCache httpCache = new HttpCache(builder.url, result);
            httpCache.setCacheStrategy(builder.cacheStrategy);
            httpCache.setPersistTime(builder.persistTime);
            requestConfig.setHttpCache(httpCache);
        }
        if (Environment.APP_ENV_CODE_PREVIEW.equals(Environment.apiConfigOfEnv) || Environment.APP_ENV_CODE_ONLINE.equals(Environment.apiConfigOfEnv)) {
            requestConfig.certificates = Environment.getCertificates();
            requestConfig.bksFile = Environment.getBKSFile();
            requestConfig.authorityPassword = Environment.getAuthorityPassword();
        }
        requestConfig.setNeedProgress(builder.needProgress);
        return requestConfig;
    }

    private void showNoDataPage(Object pageInstance, HttpRequest request) {
        if(pageInstance == null)return;
        try {
            pageInstance.getClass().getMethod("operaShowNoData", HttpRequest.class).invoke(pageInstance, request);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setRequest(Object pageInstance, HttpRequest request) {
        if(pageInstance == null)return;
        try {
            pageInstance.getClass().getMethod("setRequest", HttpRequest.class).invoke(pageInstance, request);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loginOutToLogin(String msg, String title) {

    }

}
