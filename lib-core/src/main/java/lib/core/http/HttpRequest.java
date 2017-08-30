package lib.core.http;

import lib.core.http.definition.HttpCall;

public class HttpRequest {

    private String url;
    private Object body;
    private RequestHeader requestHeader;
    private RequestConfig requestConfig;
    private HttpCall httpCall;
    private Object tag;

    public int requestMethod;
    public int what;

    public HttpRequest(int requestMethod, String url, Object body, int what) {
        this.requestMethod = requestMethod;
        this.url = url;
        this.what = what;
        this.body = body;
    }

    public String url() {
        return url;
    }

    public int getRequestMethod() {
        return requestMethod;
    }

    public Object getRequestBody() {
        return body;
    }

    public boolean needRebuild() {
        if (requestConfig != null) {
            return requestConfig.isNeedGzipRequest() || requestConfig.isNeedProgress() || requestConfig.isNeedRefresh();
        } else {
            return false;
        }
    }

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }

    public RequestConfig getRequestConfig() {
        return requestConfig;
    }

    public void setRequestConfig(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
    }

    public void setHttpCall(HttpCall httpCall) {
        this.httpCall = httpCall;
    }

    public void cancel() {
        if (httpCall != null && !httpCall.isCanceled()) httpCall.cancel();
    }

    public boolean isCanceled() {
        return httpCall != null ? httpCall.isCanceled() : false;
    }

    public boolean isExecuted() {
        return httpCall != null ? httpCall.isExecuted() : false;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }
}
