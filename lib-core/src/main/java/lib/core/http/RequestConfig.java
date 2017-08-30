package lib.core.http;

import lib.core.http.definition.HttpCallback;

/**
 * Created by lightning on 16/7/6.
 */
public class RequestConfig extends HttpConfig {

    private boolean needRefresh;
    private boolean needProgress;
    public boolean needGzipRequest;
    private HttpCallback httpCallback;
    private HttpCache httpCache;

    public boolean isNeedRefresh() {
        return needRefresh;
    }

    public void setNeedRefresh(boolean needRefresh) {
        this.needRefresh = needRefresh;
    }

    public boolean isNeedProgress() {
        return needProgress;
    }

    public void setNeedProgress(boolean needProgress) {
        this.needProgress = needProgress;
    }

    public HttpCallback getHttpCallback() {
        return httpCallback;
    }

    public void setHttpCallback(HttpCallback httpCallback) {
        this.httpCallback = httpCallback;
    }

    public boolean isNeedGzipRequest() {
        return needGzipRequest;
    }

    public void setNeedGzipRequest(boolean needGzipRequest) {
        this.needGzipRequest = needGzipRequest;
    }

    public HttpCache getHttpCache() {
        return httpCache;
    }

    public void setHttpCache(HttpCache httpCache) {
        this.httpCache = httpCache;
    }
}
