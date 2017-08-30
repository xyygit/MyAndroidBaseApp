package lib.core.http;

import lib.core.http.definition.HttpCallback;

public abstract class RefreshCallback<T> implements HttpCallback<T> {

    @Override
    public void onRequestStart(int what) {

    }

    @Override
    public void onSucceed(int what, T result) {
        refresh(what, result);
    }

    @Override
    public void onFailed(int what, int responseCode, String errorMsg, T cacheResult) {
        refresh(what, cacheResult);
    }

    @Override
    public void onResponseFinish(int what) {

    }

    public abstract void refresh(int what, T result);

    @Override
    public void onProgress(long progress, long length, boolean done) {

    }

}
