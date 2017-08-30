package lib.core.http.definition;

public interface HttpCallback<T> {

     void onRequestStart(int what);

     void onSucceed(int what, T result);

     void onFailed(int what, int responseCode, String errorMsg, T cacheResult);

     void onResponseFinish(int what);

     void onProgress(long progress, long length, boolean done);

}
