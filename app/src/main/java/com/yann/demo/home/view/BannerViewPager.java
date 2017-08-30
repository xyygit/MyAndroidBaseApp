package com.yann.demo.home.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * 支持自动轮播和手动滑动.
 */
public class BannerViewPager extends ViewPager {

    private double x, y;
    private final double touch_limit;

    private IStateListener listener;

    public BannerViewPager(Context context) {
        this(context, null);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        touch_limit = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5.f, context
                .getResources().getDisplayMetrics());
    }

    public void setStateListener(IStateListener cb) {
        this.listener = cb;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return MotionEvent.ACTION_DOWN == ev.getAction() || super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (MotionEvent.ACTION_DOWN == ev.getAction()) {
            x = ev.getX();
            y = ev.getY();
            if (listener != null) {
                listener.pause();
            }
        } else if (MotionEvent.ACTION_MOVE != ev.getAction()) {
            if (Double.compare(Math.pow(ev.getX() - x, 2.) + Math.pow(ev.getY() - y, 2.),
                    Math.pow(touch_limit, 2.)) < 0) {
                if (null != listener) {
                    PagerAdapter adapter = getAdapter();
                    if (adapter instanceof IAdapter) {
                        IAdapter iAdapter = (IAdapter) adapter;
                        listener.click(iAdapter.getView(iAdapter.getPosition(getCurrentItem())));
                    }
                }
            } else {
                if (listener != null) {
                    listener.resume();
                }
            }
        }
        return super.onTouchEvent(ev);
    }

    public interface IStateListener {
        void resume();

        void click(View view);

        void pause();
    }

    public interface IAdapter {
        View getView(int pos);

        int getPosition(int pos);
    }
}
