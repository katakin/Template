package ru.katakin.sample.ui;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import ru.katakin.sample.Constants;
import ru.katakin.sample.R;
import ru.katakin.sample.gcm.RegistrationIntentService;

public class SplashScreen extends BaseActivity {

    private Dialog dialog;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                startMainActivity();
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (checkPlayServices()) {
            if ((pref.getRegDate() == null) || ((System.currentTimeMillis() - pref.getRegDate().getTime()) > 7 * 24 * 60 * 60 * 1000)) {
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            } else {
                startMainActivity();
            }
        }
    }

    private void startMainActivity() {
        progressBar.setVisibility(ProgressBar.GONE);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean sentToken = sharedPreferences.getBoolean(Constants.SENT_TOKEN_TO_SERVER, false);
        if (sentToken) {
            startActivity(new Intent().setClass(SplashScreen.this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(SplashScreen.this, "Проверьте связь с сервером", Toast.LENGTH_SHORT).show();
        }
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
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    public boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, Constants.CONNECTION_FAILURE_RESOLUTION_REQUEST);
                dialog.setCancelable(false);
                dialog.show();
            } else {
                Log.i(Constants.LOG_TAG, "This device is not supported.");
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                dialog = new AlertDialog.Builder(this)
                        .setMessage(R.string.error_google_service)
                        .setCancelable(false)
                        .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).create();
                dialog.show();
            }
            return false;
        }
        return true;
    }
}
