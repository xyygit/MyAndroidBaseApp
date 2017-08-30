package lib.core.webview;

import android.content.Context;
import android.view.View;
import android.webkit.WebView;

public class ExWebView extends WebView {

    private ScrollListener scrollListener;

    public ExWebView(Context context) {
        super(context);
    }

    /*@Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == View.GONE) {
            try {
                WebView.class.getMethod("onPause").invoke(this);
            } catch (Exception e) {
            }
            this.pauseTimers();
        } else if (visibility == View.VISIBLE) {
            try {
                WebView.class.getMethod("onResume").invoke(this);
            } catch (Exception e) {
            }
            this.resumeTimers();
        }
    }*/

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(scrollListener != null)scrollListener.onScrollChanged(l, t, oldl, oldt);
    }

    public interface ScrollListener {
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }

    public void setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }
}
