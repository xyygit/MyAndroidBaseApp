package lib.core.http;

import android.graphics.Bitmap;
import lib.core.http.definition.HttpCallback;

public class BitmapCallback implements HttpCallback<Bitmap> {

    @Override
    public void onRequestStart(int what) {

    }

    @Override
    public void onSucceed(int what, Bitmap result) {

    }

    @Override
    public void onFailed(int what, int responseCode, String errorMsg, Bitmap cacheResult) {

    }

    @Override
    public void onResponseFinish(int what) {

    }

    @Override
    public void onProgress(long progress, long length, boolean done) {

    }
}
