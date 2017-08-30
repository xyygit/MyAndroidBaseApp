package lib.core;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import lib.core.utils.ExDeviceUtil;
import lib.core.webview.ExWebView;

public abstract class ExWebFragment extends ExFragment {

    protected ExWebView webView;

    @Override
    protected void exProcessOnCreateBefore(Bundle savedInstanceState) {

    }

    @Override
    protected boolean exInterceptOnCreate(Bundle savedInstanceState) {
        return false;
    }

    @Override
    protected int exInitLayout() {
        return 0;
    }

    @Override
    protected View exInitLayoutView() {
        webView = new ExWebView(getContext());
        return webView;
    }

    @Override
    protected void exInitView(View contentView) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setSupportZoom(true);
        webSettings.setAppCacheEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (ExDeviceUtil.getInstance().getAndroidSDKVersion() < 19) {
            webSettings.setLoadsImagesAutomatically(false);
            webView.removeJavascriptInterface("searchBoxJavaBridge_");
            webView.removeJavascriptInterface("accessibility");
            webView.removeJavascriptInterface("accessibilityTraversal");
        } else {
            webSettings.setLoadsImagesAutomatically(true);
        }
        try {
            initWebView(webView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void exInitData() {
        super.exInitData();
        try {
            initWebViewData(webView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void initWebView(ExWebView webView);


    protected abstract void initWebViewData(WebView webView);

    @Override
    public void onDestroy() {
        try {
            if (webView != null) {
                ViewGroup decorView = (ViewGroup) getActivity().getWindow().getDecorView();
                if (decorView != null) {
                    decorView.removeAllViews();
                }
                webView.setVisibility(View.GONE);
                webView.removeAllViews();
                try {
                    ((ViewGroup) webView.getParent()).removeView(webView);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    webView.destroy();
                    webView = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            super.onDestroy();
        }
    }

}
