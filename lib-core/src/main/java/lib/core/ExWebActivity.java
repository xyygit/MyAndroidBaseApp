package lib.core;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;

import lib.core.bean.TitleBar;
import lib.core.utils.ExDeviceUtil;
import lib.core.webview.ExWebView;
import lib_core.R;

/**
 * 上传图片android4.4不兼容
 */
public abstract class ExWebActivity extends ExActivity {

    private FrameLayout webViewFrame;
    protected ExWebView webView;
    public ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> mUploadMessageForAndroid5;

    public final static int FILECHOOSER_RESULTCODE = 1;
    public final static int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2;

    private TitleBar titleBar;

    @Override
    protected void exProcessOnCreateBefore(Bundle savedInstanceState) {

    }

    @Override
    protected boolean exInterceptOnCreate(Bundle savedInstanceState) {
        return false;
    }

    @Override
    protected void exInitToolbar(TitleBar titleBar) {
        super.exInitToolbar(titleBar);
        this.titleBar = titleBar;
    }

    @Override
    protected int exInitLayout() {
        return R.layout.app_web;
    }

    @Override
    protected void exInitView() {
        webViewFrame = (FrameLayout) findViewById(R.id.webViewFrame);
        webView = new ExWebView(this);
        webViewFrame.addView(webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setSupportZoom(true);
        webSettings.setAppCacheEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setUseWideViewPort(true);  //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        if (ExDeviceUtil.getInstance().getAndroidSDKVersion() < 19) {
            webSettings.setLoadsImagesAutomatically(false);
            webView.removeJavascriptInterface("searchBoxJavaBridge_");
            webView.removeJavascriptInterface("accessibility");
            webView.removeJavascriptInterface("accessibilityTraversal");
        } else {
            webSettings.setLoadsImagesAutomatically(true);
        }
        webView.setWebChromeClient(new ExWebChromeClient());
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

    protected abstract void initWebView(WebView webView);


    protected abstract void initWebViewData(WebView webView);

    @Override
    protected void applicationBackground() {

    }

    @Override
    protected boolean exInterceptInit() {
        return false;
    }

    @Override
    protected void onDestroy() {
        try {
            if (webView != null) {
                ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
                if (decorView != null) {
                    decorView.removeAllViews();
                }
                webView.setVisibility(View.GONE);
                webView.clearCache(true);
                webView.removeAllViews();
                try {
                    ((ViewGroup) webView.getParent()).removeView(webView);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    webView.destroy();
                    webView = null;
                }
                if (webViewFrame != null) {
                    webViewFrame.removeAllViews();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            super.onDestroy();
        }
    }

    public class ExWebChromeClient extends WebChromeClient {

        //扩展浏览器上传文件
        //3.0++版本
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            openFileChooserImpl(uploadMsg);
        }

        //3.0--版本
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooserImpl(uploadMsg);
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            openFileChooserImpl(uploadMsg);
        }

        // For Android > 5.0
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg, FileChooserParams fileChooserParams) {
            openFileChooserImplForAndroid5(uploadMsg);
            return true;
        }

        private void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
        }

        private void openFileChooserImplForAndroid5(ValueCallback<Uri[]> uploadMsg) {
            mUploadMessageForAndroid5 = uploadMsg;
            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
            contentSelectionIntent.setType("image/*");

            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");

            startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressUpdate(newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            titleBar.setTitle(title);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;

        } else if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
            if (null == mUploadMessageForAndroid5)
                return;
            Uri result = (intent == null || resultCode != RESULT_OK) ? null : intent.getData();
            if (result != null) {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
            } else {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
            }
            mUploadMessageForAndroid5 = null;
        }
    }

    public void back() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }
}
