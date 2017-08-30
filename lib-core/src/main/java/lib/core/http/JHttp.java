package lib.core.http;

import android.support.v4.util.ArrayMap;
import java.nio.charset.Charset;
import java.util.LinkedList;

import lib.core.common.ExHttpThread;
import lib.core.common.ExSingleThread;
import lib.core.definition.OperationTask;
import lib.core.definition.TaskCallback;
import lib.core.http.definition.HttpActuator;
import lib.core.http.definition.HttpCallback;
import lib.core.utils.ExCommonUtil;
import lib.core.utils.ExConvertUtil;
import lib.core.utils.ExDeviceUtil;
import lib.core.utils.ExToastUtil;

public class JHttp {

    public static final Charset UTF_8 = Charset.forName("UTF-8");
    private HttpConfig httpConfig;

    private HttpActuator getHttpActuator() {
        return OKHttpRequest.getInstance(httpConfig);
    }

    private HttpActuator newHttpActuator() {
        return new OKHttpRequest(httpConfig);
    }

    private Builder builder;

    private JHttp(Builder builder) {
        this.builder = builder;
    }

    public static final class Builder {

        int requestMethod;
        String url;
        Object params;
        int what;
        RequestConfig requestConfig;
        RequestHeader requestHeader;
        HttpCallback httpCallback;

        public Builder(String url) {
            this.url = url;
        }

        public void url(String url) {
            this.url = url;
        }

        public void setParams(Object params) {
            this.params = params;
        }

        public void what(int what) {
            this.what = what;
        }

        public void addCallback(HttpCallback httpCallback) {
            this.httpCallback = httpCallback;
        }

        public void method(int method) {
            this.requestMethod = method;
        }

        public void setRequestConfig(RequestConfig requestConfig) {
            this.requestConfig = requestConfig;
        }

        public void setRequestHeader(RequestHeader requestHeader) {
            this.requestHeader = requestHeader;
        }

        public JHttp build() {
            return new JHttp(this);
        }

    }

    public HttpRequest request() {

        if (ExCommonUtil.isEmpty(builder.url)) {
            //ExToastUtil.showShort("url is null");
            return null;
        }

        if (builder.requestConfig != null && httpConfig == null) {
            httpConfig = new HttpConfig();
            httpConfig.certificates = builder.requestConfig.certificates;
            httpConfig.bksFile = builder.requestConfig.bksFile;
            httpConfig.authorityPassword = builder.requestConfig.authorityPassword;
        }

        if(httpConfig == null) {
            httpConfig = new HttpConfig();
        }

        final HttpRequest httpRequest = new HttpRequest(builder.requestMethod, builder.url, builder.params, builder.what);
        HttpTrack.getInstance().addHttpRequest(httpRequest);

        if (builder.httpCallback != null) {
            builder.httpCallback.onRequestStart(builder.what);
            if (builder.requestConfig != null) {
                builder.requestConfig.setHttpCallback(builder.httpCallback);
            }
        }

        ExHttpThread.getInstance().execute(new OperationTask() {
            @Override
            public Object execute() {

                httpRequest.setRequestHeader(builder.requestHeader);
                httpRequest.setRequestConfig(builder.requestConfig);

                HttpResponse httpResponse = cacheRequest(httpRequest);
                if (httpResponse != null) return httpResponse;

                HttpActuator httpActuator = httpRequest.needRebuild() ? newHttpActuator() : getHttpActuator();
                httpResponse = httpActuator.performRequest(httpRequest);

                httpResponse = cacheResponse(httpResponse, httpRequest);

                return httpResponse;
            }
        }, new TaskCallback() {
            @Override
            public void callback(Object callback) {
                try {
                    if (builder.httpCallback != null) {
                        if (callback != null) {
                            HttpResponse httpResponse = (HttpResponse) callback;
                            int code = httpResponse.getCode();
                            switch (code) {
                                case HttpStatus.STATUS_OK:
                                    if (builder.httpCallback instanceof BitmapCallback) {
                                        builder.httpCallback.onSucceed(builder.what, ExConvertUtil.getInstance().bytesToBitmap(httpResponse.getResponseBody()));
                                    } else if(builder.httpCallback instanceof FileCallback) {
                                        builder.httpCallback.onSucceed(builder.what, httpResponse.getResponseBody());
                                    } else {
                                        builder.httpCallback.onSucceed(builder.what, new String(httpResponse.getResponseBody(), UTF_8));
                                    }
                                    break;
                                case HttpStatus.STATUS_NO_CONTENT:
                                    builder.httpCallback.onSucceed(builder.what, null);
                                    break;
                                case HttpStatus.STATUS_PARTIAL_CONTENT:
                                    break;
                                case HttpStatus.STATUS_NOT_MODIFIED:
                                case HttpStatus.STATUS_CACHE:
                                    if (builder.httpCallback instanceof BitmapCallback) {
                                        builder.httpCallback.onSucceed(builder.what, ExConvertUtil.getInstance().bytesToBitmap(httpResponse.getResponseBody()));
                                    } else if(builder.httpCallback instanceof FileCallback) {
                                        builder.httpCallback.onSucceed(builder.what, httpResponse.getResponseBody());
                                    } else {
                                        builder.httpCallback.onSucceed(builder.what, new String(httpResponse.getResponseBody(), UTF_8));
                                    }
                                    break;
                                case HttpStatus.STATUS_BAD_REQUEST:
                                case HttpStatus.STATUS_UNAUTHORIZED:
                                case HttpStatus.STATUS_FORBIDDEN:
                                case HttpStatus.STATUS_NOT_FOUND:
                                case HttpStatus.STATUS_REQUEST_TIMEOUT:
                                case HttpStatus.STATUS_INTERNAL_SERVER_ERROR:
                                case HttpStatus.STATUS_SERVICE_UNAVAILABLE:
                                case HttpStatus.STATUS_NO_NET:
                                    if (builder.httpCallback instanceof BitmapCallback) {
                                        builder.httpCallback.onFailed(builder.what, code, "请求超时", ExConvertUtil.getInstance().bytesToBitmap(httpResponse.getResponseBody()));
                                    } else if(builder.httpCallback instanceof FileCallback) {
                                        builder.httpCallback.onFailed(builder.what, code, "请求超时", httpResponse.getResponseBody());
                                    } else {
                                        builder.httpCallback.onFailed(builder.what, code, "请求超时", new String(httpResponse.getResponseBody(), UTF_8));
                                    }
                                    break;
                                case HttpStatus.STATUS_CANCEL:
                                    break;
                                case HttpStatus.STATUS_NO:
                                    if (builder.httpCallback instanceof BitmapCallback) {
                                        builder.httpCallback.onFailed(builder.what, code, "请求超时", ExConvertUtil.getInstance().bytesToBitmap(httpResponse.getResponseBody()));
                                    } else if(builder.httpCallback instanceof FileCallback) {
                                        builder.httpCallback.onFailed(builder.what, code, "请求超时", httpResponse.getResponseBody());
                                    } else {
                                        builder.httpCallback.onFailed(builder.what, code, "请求超时", new String(httpResponse.getResponseBody(), UTF_8));
                                    }
                                    break;
                                default:
                                    if (builder.httpCallback instanceof BitmapCallback) {
                                        builder.httpCallback.onFailed(builder.what, code, "请求超时", ExConvertUtil.getInstance().bytesToBitmap(httpResponse.getResponseBody()));
                                    } else if(builder.httpCallback instanceof FileCallback) {
                                        builder.httpCallback.onFailed(builder.what, code, "请求超时", httpResponse.getResponseBody());
                                    }else {
                                        builder.httpCallback.onFailed(builder.what, code, "请求超时", new String(httpResponse.getResponseBody(), UTF_8));
                                    }
                                    break;
                            }
                            removeRequest(httpResponse.url());
                        } else {
                            builder.httpCallback.onFailed(builder.what, HttpStatus.STATUS_NO, "请求超时", null);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (builder.httpCallback != null) builder.httpCallback.onResponseFinish(builder.what);
                    HttpTrack.getInstance().removeRequest(httpRequest);
                }
            }
        });

        return httpRequest;
    }

    public byte[] requestSyn() {
        if (ExCommonUtil.isEmpty(builder.url)) {
            ExToastUtil.showShort("url is null");
            return null;
        }

        final HttpRequest httpRequest = new HttpRequest(builder.requestMethod, builder.url, builder.params, 0);

        httpRequest.setRequestConfig(builder.requestConfig);

        HttpResponse httpResponse = cacheRequest(httpRequest);
        if (httpResponse != null) return httpResponse.getResponseBody();

        HttpActuator httpActuator = httpRequest.needRebuild() ? newHttpActuator() : getHttpActuator();
        httpResponse = httpActuator.performRequest(httpRequest);

        httpResponse = cacheResponse(httpResponse, httpRequest);
        if (httpResponse != null) return httpResponse.getResponseBody();

        return null;
    }

    public HttpRequest requestTrack() {

        if (ExCommonUtil.isEmpty(builder.url)) {
            builder.httpCallback.onFailed(builder.what, HttpStatus.STATUS_NO, "", null);
            return null;
        }

        if (builder.requestConfig != null && httpConfig == null) {
            httpConfig = new HttpConfig();
            httpConfig.certificates = builder.requestConfig.certificates;
            httpConfig.bksFile = builder.requestConfig.bksFile;
            httpConfig.authorityPassword = builder.requestConfig.authorityPassword;
        }

        if(httpConfig == null) {
            httpConfig = new HttpConfig();
        }

        final HttpRequest httpRequest = new HttpRequest(builder.requestMethod, builder.url, builder.params, builder.what);

        if (builder.httpCallback != null) {
            builder.httpCallback.onRequestStart(builder.what);
            if (builder.requestConfig != null) {
                builder.requestConfig.setHttpCallback(builder.httpCallback);
            }
        }

        ExSingleThread.getInstance().execute(new OperationTask() {
            @Override
            public Object execute() {

                httpRequest.setRequestConfig(builder.requestConfig);

                HttpResponse httpResponse = cacheRequest(httpRequest);
                if (httpResponse != null) return httpResponse;

                HttpActuator httpActuator = httpRequest.needRebuild() ? newHttpActuator() : getHttpActuator();
                httpResponse = httpActuator.performRequest(httpRequest);

                httpResponse = cacheResponse(httpResponse, httpRequest);

                return httpResponse;
            }
        }, new TaskCallback() {
            @Override
            public void callback(Object callback) {
                try {
                    if (builder.httpCallback != null) {
                        if (callback != null) {
                            HttpResponse httpResponse = (HttpResponse) callback;
                            int code = httpResponse.getCode();
                            switch (code) {
                                case HttpStatus.STATUS_OK:
                                    builder.httpCallback.onSucceed(builder.what, new String(httpResponse.getResponseBody(), UTF_8));
                                    break;
                                case HttpStatus.STATUS_NO_CONTENT:
                                    builder.httpCallback.onSucceed(builder.what, null);
                                    break;
                                case HttpStatus.STATUS_PARTIAL_CONTENT:
                                    break;
                                case HttpStatus.STATUS_NOT_MODIFIED:
                                case HttpStatus.STATUS_CACHE:
                                    builder.httpCallback.onSucceed(builder.what, new String(httpResponse.getResponseBody(), UTF_8));
                                    break;
                                case HttpStatus.STATUS_BAD_REQUEST:
                                case HttpStatus.STATUS_UNAUTHORIZED:
                                case HttpStatus.STATUS_FORBIDDEN:
                                case HttpStatus.STATUS_NOT_FOUND:
                                case HttpStatus.STATUS_REQUEST_TIMEOUT:
                                case HttpStatus.STATUS_INTERNAL_SERVER_ERROR:
                                case HttpStatus.STATUS_SERVICE_UNAVAILABLE:
                                case HttpStatus.STATUS_NO_NET:
                                    builder.httpCallback.onFailed(builder.what, code, "", new String(httpResponse.getResponseBody(), UTF_8));
                                    break;
                                case HttpStatus.STATUS_CANCEL:
                                    break;
                                case HttpStatus.STATUS_NO:
                                    builder.httpCallback.onFailed(builder.what, code, "", new String(httpResponse.getResponseBody(), UTF_8));
                                    break;
                            }
                            removeRequest(httpResponse.url());
                        } else {
                            builder.httpCallback.onFailed(builder.what, HttpStatus.STATUS_NO, "", null);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (builder.httpCallback != null) builder.httpCallback.onResponseFinish(builder.what);
                }
            }
        });

        return httpRequest;
    }

    private void removeRequest(String url) {
        HttpTrack.getInstance().removeRequest(url);
    }

    public static void cancelByUrl(String url) {
        LinkedList<HttpRequest> httpRequestList = HttpTrack.getInstance().getHttpRequestList();
        if(ExCommonUtil.isEmpty(httpRequestList))return;
        int size = httpRequestList.size();
        for (int i=0 ; i<size; i++) {
            HttpRequest httpRequest = httpRequestList.get(i);
            if (url.equals(httpRequest.url())) {
                httpRequest.cancel();
                HttpTrack.getInstance().removeRequest(httpRequest);
                size = httpRequestList.size();
                i--;
            }
        }
    }

    public static void cancelByTag(Object tag) {
        LinkedList<HttpRequest> httpRequestList = HttpTrack.getInstance().getHttpRequestList();
        if(ExCommonUtil.isEmpty(httpRequestList))return;
        int size = httpRequestList.size();
        for (int i=0 ; i<size; i++) {
            HttpRequest httpRequest = httpRequestList.get(i);
            if (tag.equals(httpRequest.getTag())) {
                httpRequest.cancel();
                HttpTrack.getInstance().removeRequest(httpRequest);
                size = httpRequestList.size();
                i--;
            }
        }
    }

    private static HttpResponse cacheRequest(HttpRequest httpRequest) {
        HttpResponse httpResponse = null;
        RequestConfig requestConfig = httpRequest.getRequestConfig();
        if (requestConfig != null) {
            HttpCache httpCache = requestConfig.getHttpCache();
            if (httpCache != null) {
                HttpCallback httpCallback = requestConfig.getHttpCallback();
                if (httpCache.triggerCacheStrategy(CacheMode.CACHE_NO_NET) && !ExDeviceUtil.isNetworkConnect()) {
                    if (httpCallback instanceof BitmapCallback) {
                        byte[] cache = httpCache.getImgCache(httpCache.getKey());
                        if (!ExCommonUtil.isEmpty(cache)) {
                            return new HttpResponse(httpRequest.url(), HttpStatus.STATUS_CACHE, cache, null, httpRequest.what);
                        }
                    } else {
                        String cacheResult = httpCache.getCache(httpCache.getKey());
                        if (!ExCommonUtil.isEmpty(cacheResult)) {
                            return new HttpResponse(httpRequest.url(), HttpStatus.STATUS_CACHE, cacheResult.getBytes(), null, httpRequest.what);
                        }
                    }
                }
                if (httpCache.triggerCacheStrategy(CacheMode.CACHE_LOAD_FIRST)) {
                    if (httpCallback instanceof BitmapCallback) {
                        byte[] cache = httpCache.getImgCache(httpCache.getKey());
                        if (!ExCommonUtil.isEmpty(cache)) {
                            httpResponse = new HttpResponse(httpRequest.url(), HttpStatus.STATUS_NOT_MODIFIED, cache, null, httpRequest.what);
                        }
                    } else {
                        String cacheResult = httpCache.getCache(httpCache.getKey());
                        if (!ExCommonUtil.isEmpty(cacheResult)) {
                            httpResponse = new HttpResponse(httpRequest.url(), HttpStatus.STATUS_NOT_MODIFIED, cacheResult.getBytes(), null, httpRequest.what);
                        }
                    }
                }
            }
        }
        return httpResponse;
    }

    private static HttpResponse cacheResponse(HttpResponse httpResponse, HttpRequest httpRequest) {
        RequestConfig requestConfig = httpRequest.getRequestConfig();
        if (requestConfig != null) {
            HttpCache httpCache = requestConfig.getHttpCache();
            if (httpCache != null) {
                HttpCallback httpCallback = requestConfig.getHttpCallback();
                if (httpResponse != null) {
                    if (httpResponse.getCode() == HttpStatus.STATUS_REQUEST_TIMEOUT && httpCache.triggerCacheStrategy(CacheMode.CACHE_TIME_OUT)) {
                        if (httpCallback instanceof BitmapCallback) {
                            return new HttpResponse(httpRequest.url(), HttpStatus.STATUS_CACHE, httpCache.getImgCache(httpCache.getKey()), null, httpRequest.what);
                        } else {
                            return new HttpResponse(httpRequest.url(), HttpStatus.STATUS_CACHE, httpCache.getCache(httpCache.getKey()).getBytes(), null, httpRequest.what);
                        }
                    }
                    if (httpResponse.getCode() == HttpStatus.STATUS_NOT_MODIFIED && httpCache.triggerCacheStrategy(CacheMode.CACHE_NO_CHANGE) && ExCommonUtil.isEmpty(httpResponse.getResponseBody())) {
                        if (httpCallback instanceof BitmapCallback) {
                            httpResponse = new HttpResponse(httpRequest.url(), HttpStatus.STATUS_NOT_MODIFIED, httpCache.getImgCache(httpCache.getKey()), null, httpRequest.what);
                        } else {
                            httpResponse = new HttpResponse(httpRequest.url(), HttpStatus.STATUS_NOT_MODIFIED, httpCache.getCache(httpCache.getKey()).getBytes(), null, httpRequest.what);
                        }
                    }
                    if (httpCache.hasCacheStrategy() && !ExCommonUtil.isEmpty(httpResponse.getResponseBody())) {
                        if (httpCallback instanceof BitmapCallback) {
                            httpCache.saveCache(httpCache.getKey(), httpResponse.getResponseBody());
                        } else {
                            httpCache.saveCache(httpCache.getKey(), new String(httpResponse.getResponseBody(), UTF_8));
                        }
                    }
                } else {
                    if (httpCache.triggerCacheStrategy(CacheMode.CACHE_NO_DATA)) {
                        if (httpCallback instanceof BitmapCallback) {
                            httpResponse = new HttpResponse(httpRequest.url(), HttpStatus.STATUS_CACHE, httpCache.getImgCache(httpCache.getKey()), null, httpRequest.what);
                        } else {
                            httpResponse = new HttpResponse(httpRequest.url(), HttpStatus.STATUS_CACHE, httpCache.getCache(httpCache.getKey()).getBytes(), null, httpRequest.what);
                        }
                    }
                }
            }
        }
        return httpResponse;
    }
}
