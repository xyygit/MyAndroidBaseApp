package lib.core.bean;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import lib.core.utils.ExCommonUtil;
import lib.core.utils.ExConvertUtil;

/**
 * Created by lightning on 17/2/22.
 */

public class TitleBar extends Toolbar {

    private TextView toolbarTitle;
    private View toolbarShadow;
    private FrameLayout extendFrame;

    public TitleBar(Context context) {
        super(context);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setToolbarTitle(TextView toolbarTitle) {
        this.toolbarTitle = toolbarTitle;
    }

    public void setTitle(CharSequence title) {
        toolbarTitle.setText(title);
    }

    public void setTextColor(int color) {
        toolbarTitle.setTextColor(color);
    }

    public void setTextSize(float textSize) {
        toolbarTitle.setTextSize(textSize);
    }

    public View getToolbarShadow() {
        return toolbarShadow;
    }

    public void setToolbarShadow(View toolbarShadow) {
        this.toolbarShadow = toolbarShadow;
    }

    public void setExtendFrame(FrameLayout extendFrame) {
        this.extendFrame = extendFrame;
    }

    public void addExtendView(Context context, ViewGroup viewGroup) {
        extendFrame.setPadding(0, 0, ExConvertUtil.getInstance().dipToPx(context, 12), 0);
        extendFrame.addView(viewGroup);
        extendFrame.setVisibility(View.VISIBLE);
        toolbarTitle.setVisibility(View.GONE);
    }

    public void setExtendViewVisibility(Boolean visibility) {
        extendFrame.setVisibility(visibility ? View.VISIBLE : View.GONE);
        toolbarTitle.setVisibility(visibility ? View.GONE : View.VISIBLE);
    }

    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        toolbarShadow.setVisibility(visibility);
    }
}
