package ru.katakin.sample.ui;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import ru.katakin.sample.Constants;
import ru.katakin.sample.R;
import ru.katakin.sample.gcm.RegistrationIntentService;
import ru.katakin.sample.util.Logger;

public class SplashScreen extends BaseActivity {

    private Dialog mDialog;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        TextView version = (TextView) findViewById(R.id.version);
        if (getPackageInfo() != null) {
            pref.setAppVersion(getPackageInfo().versionCode);
            String versionName = "v" + getPackageInfo().versionName;
            version.setText(versionName);
        }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                startMainActivity();
            }
        };
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (checkPlayServices()) {
            if ((pref.getRegDate() == 0) || ((System.currentTimeMillis() - pref.getRegDate()) > 7 * 24 * 60 * 60 * 1000)) {
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            } else {
                startMainActivity();
            }
        }
    }

    private void startMainActivity() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(SplashScreen.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(Constants.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a mDialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                mDialog = googleApiAvailability.getErrorDialog(this, resultCode, Constants.CONNECTION_FAILURE_RESOLUTION_REQUEST);
                mDialog.setCancelable(false);
                mDialog.show();
            } else {
                mDialog = new AlertDialog.Builder(this)
                        .setMessage(R.string.error_google_service)
                        .setCancelable(false)
                        .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).create();
                mDialog.show();
            }
            return false;
        }
        return true;
    }

    private PackageInfo getPackageInfo() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            Logger.d("Could not get package name: " + e.toString());
            return null;
        }
    }

    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            // Hide UI first
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHandler.removeCallbacks(mHideRunnable);
        mHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
