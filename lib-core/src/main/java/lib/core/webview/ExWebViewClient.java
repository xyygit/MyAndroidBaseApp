package lib.core.webview;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.io.InputStream;
import java.util.LinkedHashMap;

import lib.core.http.BitmapCallback;
import lib.core.http.CacheMode;
import lib.core.http.HttpImgCache;
import lib.core.http.JHttp;
import lib.core.http.RequestConfig;
import lib.core.http.RequestMethod;
import lib.core.utils.ExCommonUtil;
import lib.core.utils.ExConvertUtil;

public class ExWebViewClient extends WebViewClient {

    private LinkedHashMap<String, String> localRes;

    public void configLocalResFilter(LinkedHashMap<String, String> localRes) {
        this.localRes = localRes;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (!view.getSettings().getLoadsImagesAutomatically()) {
            view.getSettings().setLoadsImagesAutomatically(true);
        }
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {

        if (!ExCommonUtil.isEmpty(url)) {
            WebResourceResponse webResourceResponse = getResourceResponse(url);
            if (!ExCommonUtil.isEmpty(webResourceResponse)) {
                return webResourceResponse;
            }
        }

        return super.shouldInterceptRequest(view, url);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && request != null && request.getUrl() != null) {
            WebResourceResponse webResourceResponse = getResourceResponse(request.getUrl().toString());
            if (!ExCommonUtil.isEmpty(webResourceResponse)) {
                return webResourceResponse;
            }
        }

        return super.shouldInterceptRequest(view, request);
    }

    private WebResourceResponse getResourceResponse(String url) {

        String lowerUrl = url.toLowerCase();
        WebResourceResponse response = null;

        if(ExCommonUtil.isEmpty(localRes)) return response;
        for (String key : localRes.keySet()) {
            if (lowerUrl.endsWith(key)) {
                return getWebResourceResponse(url, localRes.get(key));
            }
        }

        return response;
    }

    private WebResourceResponse getWebResourceResponse(String url, String mime) {
        WebResourceResponse response = null;
        InputStream inputStream = null;

        try {
            RequestConfig requestConfig = new RequestConfig();
            requestConfig.setHttpCallback(new BitmapCallback());
            HttpImgCache httpCache = new HttpImgCache(url, mime);
            httpCache.addCacheStrategy(CacheMode.CACHE_LOAD_FIRST);
            requestConfig.setHttpCache(httpCache);
            JHttp.Builder builder = new JHttp.Builder(url);
            builder.method(RequestMethod.GET);
            builder.setRequestConfig(requestConfig);
            byte[] responseBody = builder.build().requestSyn();
            inputStream = ExConvertUtil.getInstance().byteToInputStream(responseBody);
            if (!ExCommonUtil.isEmpty(inputStream)) {
                response = new WebResourceResponse(mime, "UTF-8", inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        if (error != null && handler != null && (error.getPrimaryError() != 0)) {
            handler.proceed();
        } else {
            handler.cancel();
        }
    }

}
