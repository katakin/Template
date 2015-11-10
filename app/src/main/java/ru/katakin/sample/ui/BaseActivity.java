package ru.katakin.sample.ui;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import ru.katakin.sample.Constants;
import ru.katakin.sample.preferences.AppPreferences;

public class BaseActivity extends AppCompatActivity {

    protected EventBus bus;
    protected int versionCode;
    protected AppPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bus = EventBus.getDefault();

        versionCode = getAppVersionCode();
        pref = AppPreferences.getInstance(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bus.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        bus.unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public int getAppVersionCode() {
        try {
            PackageInfo packageInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(Constants.LOG_TAG, "Could not get package name: " + e.toString());
            return -1;
        }
    }

    @Subscribe
    public void onEvent(Object object) {

    }
}
