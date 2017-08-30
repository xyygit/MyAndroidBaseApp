package lib.core.utils;

import android.widget.Toast;

public class ExToastUtil {

    private ExToastUtil() {
        throw new AssertionError();
    }

    /**
     * @param text
     * @param duration
     * @return void
     * @method show
     * @author lightning
     */
    public static final void show(CharSequence text, int duration) {
        Toast.makeText(ExAppUtil.getApplicationContext(), text, duration).show();
    }

    /**
     * @param resId
     * @param duration
     * @return void
     * @method show
     * @author lightning
     */
    public static final void show(int resId, int duration) {
        Toast.makeText(ExAppUtil.getApplicationContext(), resId, duration).show();
    }

    /**
     * @param text
     * @return void
     * @method showLong
     * @author lightning
     */
    public static final void showLong(CharSequence text) {
        show(text, Toast.LENGTH_LONG);
    }

    /**
     * @param resId
     * @return void
     * @method showLong
     * @author lightning
     */
    public static final void showLong(int resId) {
        show(resId, Toast.LENGTH_LONG);
    }

    /**
     * @param text
     * @return void
     * @method showShort
     * @author lightning
     */
    public static final void showShort(CharSequence text) {
        show(text, Toast.LENGTH_SHORT);
    }

    /**
     * @param resId
     * @return void
     * @method showShort
     * @author lightning
     */
    public static final void showShort(int resId) {
        show(resId, Toast.LENGTH_SHORT);
    }

}
