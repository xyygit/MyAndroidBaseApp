package lib.core.utils;

import android.content.Context;
import android.content.SharedPreferences;

import lib.core.security.AES;


public class ExSharePreferencesUtil {

    private ExSharePreferencesUtil() {}

    private static class ExPreferencesUtilHolder {
        private static final ExSharePreferencesUtil epu = new ExSharePreferencesUtil();
    }

    public static final ExSharePreferencesUtil getInstance() {
        return ExPreferencesUtilHolder.epu;
    }

    private static final String PREFERENCE_NAME = ExAppUtil.getInstance().getPackageName();

    /**
     * put string preferences
     *
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public final boolean putStringWithCallback(String key, String value) {
        SharedPreferences settings = ExAppUtil.getApplicationContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    /**
     *
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @param encryption judge need to encrypt
     * @return
     */
    public final boolean putStringWithCallback(String key, String value, boolean encryption) {
        SharedPreferences settings = ExAppUtil.getApplicationContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, encryption ? AES.encrypt(value) : value);
        return editor.commit();
    }

    /**
     * put string preferences
     *
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     */
    public final void putString(String key, String value) {
        SharedPreferences settings = ExAppUtil.getApplicationContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     *
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @param encryption judge need to encrypt
     */
    public final void putString(String key, String value, boolean encryption) {
        SharedPreferences settings = ExAppUtil.getApplicationContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, encryption ? AES.encrypt(value) : value);
        editor.apply();
    }

    /**
     * get string preferences
     *
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or null. Throws ClassCastException if there is a preference with this
     *         name that is not a string
     */
    public final String getString(String key) {
        return getString(key, null);
    }

    /**
     *
     * @param key The name of the preference to retrieve
     * @param encryption judge need to encrypt
     * @return
     */
    public final String getString(String key, boolean encryption) {
        return getString(key, null, encryption);
    }

    /**
     * get string preferences
     *
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     *         this name that is not a string
     */
    public final String getString(String key, String defaultValue) {
        return getString(key, defaultValue, false);
    }

    public final String getString(String key, String defaultValue, boolean encryption) {
        SharedPreferences settings = ExAppUtil.getApplicationContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        String value = settings.getString(key, defaultValue);
        return encryption ? AES.decrypt(value) : value;
    }

    /**
     * put int preferences
     *
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public final boolean putIntWithCallback(String key, int value) {
        SharedPreferences settings = ExAppUtil.getApplicationContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    /**
     * put int preferences
     *
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     */
    public final void putInt(String key, int value) {
        SharedPreferences settings = ExAppUtil.getApplicationContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * get int preferences
     *
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
     *         name that is not a int
     */
    public final int getInt(String key) {
        return getInt(key, -1);
    }

    /**
     * get int preferences
     *
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     *         this name that is not a int
     */
    public final int getInt(String key, int defaultValue) {
        SharedPreferences settings = ExAppUtil.getApplicationContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getInt(key, defaultValue);
    }

    /**
     * put long preferences
     *
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public final boolean putLongCallback(String key, long value) {
        SharedPreferences settings = ExAppUtil.getApplicationContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    /**
     * put long preferences
     *
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     */
    public final void putLong(String key, long value) {
        SharedPreferences settings = ExAppUtil.getApplicationContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    /**
     * get long preferences
     *
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
     *         name that is not a long
     */
    public final long getLong(String key) {
        return getLong(key, -1);
    }

    /**
     * get long preferences
     *
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     *         this name that is not a long
     */
    public final long getLong(String key, long defaultValue) {
        SharedPreferences settings = ExAppUtil.getApplicationContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getLong(key, defaultValue);
    }

    /**
     * put float preferences
     *
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public final boolean putFloatCallback(String key, float value) {
        SharedPreferences settings = ExAppUtil.getApplicationContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    /**
     * put float preferences
     *
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     */
    public final void putFloat(String key, float value) {
        SharedPreferences settings = ExAppUtil.getApplicationContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    /**
     * get float preferences
     *
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
     *         name that is not a float
     */
    public final float getFloat(String key) {
        return getFloat(key, -1);
    }

    /**
     * get float preferences
     *
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     *         this name that is not a float
     */
    public final float getFloat(String key, float defaultValue) {
        SharedPreferences settings = ExAppUtil.getApplicationContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getFloat(key, defaultValue);
    }

    /**
     * put boolean preferences
     *
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public final boolean putBooleanCallback(String key, boolean value) {
        SharedPreferences settings = ExAppUtil.getApplicationContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    /**
     * put boolean preferences
     *
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     */
    public final void putBoolean(String key, boolean value) {
        SharedPreferences settings = ExAppUtil.getApplicationContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * get boolean preferences, default is false
     *
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or false. Throws ClassCastException if there is a preference with this
     *         name that is not a boolean
     */
    public final boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * get boolean preferences
     *
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     *         this name that is not a boolean
     */
    public final boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences settings = ExAppUtil.getApplicationContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getBoolean(key, defaultValue);
    }
}
