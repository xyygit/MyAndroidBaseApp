package lib.core.http;

import lib.core.http.definition.HttpCallback;

public class ResponseCallback<T> implements HttpCallback<T> {

    @Override
    public void onRequestStart(int what) {

    }

    @Override
    public void onSucceed(int what, T result) {

    }

    @Override
    public void onFailed(int what, int responseCode, String errorMsg, T cacheResult) {
        onFailed(what, responseCode, errorMsg);
    }

    public void onFailed(int what, int responseCode, String errorMsg) {

    }

    @Override
    public void onResponseFinish(int what) {

    }

    @Override
    public void onProgress(long progress, long length, boolean done) {

    }

}
