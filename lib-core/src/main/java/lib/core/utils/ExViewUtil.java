package lib.core.utils;

import android.view.View;

public class ExViewUtil {
    private ExViewUtil() {}

    private static class ExViewUtilHolder {
        private final static ExViewUtil evu = new ExViewUtil();
    }

    public static final ExViewUtil getInstance() {
        return ExViewUtilHolder.evu;
    }

    /**
     * 获取焦点
     * @method getFocus
     * @param view
     * @return void
     * @author lightning
     */
    public final void getFocus(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    /**
     * 测量高度
     *
     * @param viewGroup
     * @return int
     * @method measureView
     */
    public final int measureViewHeight(View viewGroup) {
        return measureViewHeight(viewGroup, View.MeasureSpec.UNSPECIFIED);
    }

    /**
     * 测量高度
     *
     * @param viewGroup
     * @return int
     * @method measureView
     */
    public final int measureViewHeight(View viewGroup, int mode) {
        if (viewGroup == null) return 0;
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, mode);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, mode);
        viewGroup.measure(widthMeasureSpec, heightMeasureSpec);
        return viewGroup.getMeasuredHeight();
    }

    /**
     * 测量宽度
     *
     * @param viewGroup
     * @return int
     * @method measureView
     */
    public final int measureViewWidth(View viewGroup) {
        return measureViewWidth(viewGroup, View.MeasureSpec.UNSPECIFIED);
    }

    /**
     * 测量宽度
     *
     * @param viewGroup
     * @return int
     * @method measureView
     */
    public final int measureViewWidth(View viewGroup, int mode) {
        if (viewGroup == null) return 0;
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, mode);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, mode);
        viewGroup.measure(widthMeasureSpec, heightMeasureSpec);
        return viewGroup.getMeasuredWidth();
    }

    /**
     * 获取视图在屏幕的X坐标
     * @method getScreenX
     * @param view
     * @return int
     * @author lightning
     */
    public final int getScreenX(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        return x;
    }

    /**
     * 获取视图在屏幕的Y坐标
     * @method getScreenY
     * @param view
     * @return int
     * @author lightning
     */
    public final int getScreenY(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int y = location[1];
        return y;
    }

}
