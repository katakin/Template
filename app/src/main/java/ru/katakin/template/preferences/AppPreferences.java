package ru.katakin.template.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import ru.katakin.template.util.Parser;

public class AppPreferences {

    private final String LAST_REG_DATE = "reg_date";
    private final String PROPERTY_APP_VERSION = "app_version";

    private SharedPreferences sharedPreferences;
    private static AppPreferences preferences;

    public static synchronized AppPreferences getInstance(Context context) {
        return preferences != null ? preferences : (preferences = new AppPreferences(context));
    }

    private AppPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences("MainData", Context.MODE_PRIVATE);
    }

    private synchronized void saveStringPreference(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (TextUtils.isEmpty(value)) {
            editor.remove(key);
        } else {
            editor.putString(key, value);
        }
        editor.apply();
    }

    private synchronized void saveIntPreference(String key, Integer value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (value == null) {
            editor.remove(key);
        } else {
            editor.putInt(key, value);
        }
        editor.apply();
    }

    private synchronized void saveBooleanPreference(String key, Boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (value == null) {
            editor.remove(key);
        } else {
            editor.putBoolean(key, value);
        }
        editor.apply();
    }

    public long getRegDate() {
        return Parser.stringToLong(sharedPreferences.getString(LAST_REG_DATE, ""));
    }
    public void setRegDate(long milliseconds) {
        saveStringPreference(LAST_REG_DATE, String.valueOf(milliseconds));
    }

    public int getAppVersion() {
        return sharedPreferences.getInt(PROPERTY_APP_VERSION, -1);
    }
    public void setAppVersion(int appVersion) {
        saveIntPreference(PROPERTY_APP_VERSION, appVersion);
    }
}
