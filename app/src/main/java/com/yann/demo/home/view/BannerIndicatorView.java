package com.yann.demo.home.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import com.yann.demo.R;

import java.util.Arrays;

/**
 * ViewPager圆点指示器
 */
public class BannerIndicatorView extends View {

    private final Drawable[] mDrawables = new Drawable[2];

    private float mWidth, mHeight;
    private float mCellSideLength, mCellPadding, mCellLeftPadding, mCellRightPadding;
    private float internal_side_length;
    private float normalSize;
    private float strokeSize;

    private int mGravity;

    private int mCount;
    private boolean[] selects;

    public BannerIndicatorView(Context context) {
        this(context, null);
    }

    public BannerIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BannerIndicatorView);

        mCellSideLength = typedArray.getDimension(R.styleable.BannerIndicatorView_cell_side_length,
                dp2px(context, 28.f));
        mCellPadding = typedArray.getDimension(R.styleable.BannerIndicatorView_cell_padding,
                dp2px(context, 10.f));
        mCellLeftPadding = typedArray.getDimension(R.styleable.BannerIndicatorView_cell_left_padding,
                dp2px(context, 2.f));
        mCellRightPadding = typedArray.getDimension(R.styleable.BannerIndicatorView_cell_right_padding,
                dp2px(context, 2.f));
        strokeSize = typedArray.getDimension(R.styleable.BannerIndicatorView_cell_stroke_size, 0);

        mGravity = typedArray.getInt(R.styleable.BannerIndicatorView_dot_gravity, 2); // 默认居中显示

        Drawable normal = typedArray.getDrawable(R.styleable.BannerIndicatorView_normal);
        Drawable focused = typedArray.getDrawable(R.styleable.BannerIndicatorView_focused);

        mDrawables[0] = null != normal ? normal : context.getResources().getDrawable(R.drawable.shape_home_banner_indicator_dot_normal);
        mDrawables[1] = null != focused ? focused : context.getResources().getDrawable(R.drawable.shape_home_banner_indicator_dot_focus);

        typedArray.recycle();

        internal_side_length = mCellSideLength - 2.f * mCellPadding;
        normalSize = internal_side_length - 2 * strokeSize;
    }

    public void initDotView(int count) {
        mCount = count;
        selects = new boolean[count];

        Arrays.fill(selects, false);
        postInvalidate();
    }

    public void setDotSelected(int index) {
        Arrays.fill(selects, false);

        selects[index] = true;
        postInvalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawDotViews(canvas);
    }

    private void drawDotViews(Canvas canvas) {
        final float left;
        if (mGravity == 1) { // 圆点布局居左显示
            left = mCellLeftPadding;
        } else if (mGravity == 3) { // 圆点布局居右显示
            left = mWidth - mCount * mCellSideLength - mCellRightPadding;
        } else { // 圆点布局居中显示
            left = 0.5f * (mWidth - mCount * mCellSideLength);
        }

        if (selects != null) {
            int b_left, b_top, b_right, b_bottom;

            for (int index = 0, sz = selects.length; index < sz; ++index) {
                boolean selected = selects[index];

                final float top = strokeSize > 0 ?
                        (selected ? 0 : 0.25f * (mHeight - normalSize)) : 0.5f * (mHeight - internal_side_length);

                b_left = (int) (left + index * mCellSideLength + mCellPadding +
                        (strokeSize > 0 ? (selected ? 0 : strokeSize) : 0) + .5f);
                b_top = (int) (top + .5f);
                b_right = (int) (b_left + internal_side_length -
                        (strokeSize > 0 ? (selected ? 0 : strokeSize) : 0) + .5f);
                b_bottom = (int) (b_top + internal_side_length -
                        (strokeSize > 0 ? (selected ? 0 : strokeSize) : 0) + .5f);

                mDrawables[selected ? 1 : 0].setBounds(b_left, b_top, b_right, b_bottom);
                mDrawables[selected ? 1 : 0].draw(canvas);
            }
        }
    }

    private float dp2px(Context context, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources()
                .getDisplayMetrics());
    }

}
