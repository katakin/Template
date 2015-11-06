package ru.katakin.sample.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import ru.katakin.sample.Constants;
import ru.katakin.sample.preferences.AppPreferences;

public class GCM {

    private AppPreferences prefs;
    private Context context;
    private int versionCode;
    private static GoogleCloudMessaging gcm;
    private static String regId;
    private Handler handler;

    public GCM(Context context) {
        this.context = context.getApplicationContext();
    }

    public String registerGCM() {
        prefs = AppPreferences.getInstance(context);
        versionCode = getAppVersionCode();

        gcm = GoogleCloudMessaging.getInstance(context);
        regId = getRegistrationId();

        if (TextUtils.isEmpty(regId)) {
            regId = registerInBackground();
        } else {
            if (Constants.DEBUG) Log.d(Constants.LOG_TAG, "Device registered, registration ID = " + regId);
        }

        return regId;
    }


    private String getRegistrationId() {
        String registrationId = prefs.getRegistrationId();
        if (TextUtils.isEmpty(registrationId)) {
            if (Constants.DEBUG) Log.d(Constants.LOG_TAG, "Registration not found.");
            return "";
        }

        int registeredVersion = prefs.getAppVersion();
        if (registeredVersion != versionCode) {
            if (Constants.DEBUG) Log.d(Constants.LOG_TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private void storeRegistrationId(String regId) {
        if (Constants.DEBUG) Log.d(Constants.LOG_TAG, "Saving regId on app version " + versionCode);
        if (!TextUtils.isEmpty(regId)) prefs.setRegistrationId(regId);
        if (versionCode != -1) prefs.setAppVersion(versionCode);
    }

    private String registerInBackground() {
        String new_regId = "";
        new AsyncTask<Object, Void, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                long expTimeoutCount = 0L;
                String msg;
                try {
                    if (gcm == null) gcm = GoogleCloudMessaging.getInstance(context);
                    regId = gcm.register(Constants.SENDER_ID);
                    msg = "Device registered, registration ID = " + regId;
                    storeRegistrationId(regId);
                    expTimeoutCount = 0L;
                } catch (IOException e) {
                    msg = "Error :" + e.getMessage();
                    expTimeoutCount++;
                    if (handler == null) handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            registerInBackground();
                        }
                    }, Constants.INIT_EXP_TIMEOUT * (long)Math.exp(expTimeoutCount - 1));
                }
                return msg;
            }

            @Override
            protected void onPostExecute(Object msg) {
                if (Constants.DEBUG) Log.d(Constants.LOG_TAG, msg.toString());
            }
        }.execute(null, null, null);
        return new_regId;
    }

    private int getAppVersionCode() {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            if (Constants.DEBUG) Log.d(Constants.LOG_TAG, "Could not get package name: " + e.toString());
            return -1;
        }
    }
}
