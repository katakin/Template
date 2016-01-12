package ru.katakin.sample.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import ru.katakin.sample.Constants;
import ru.katakin.sample.MyApplication;
import ru.katakin.sample.R;
import ru.katakin.sample.preferences.AppPreferences;

public class BaseActivity extends AppCompatActivity {

    private ServerRequestProcessingLayout serverRequestProcessingLayout;
    protected AppPreferences pref;
    protected final Handler mHandler = new Handler();
    protected MyApplication application;

    private Runnable hideProcessingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        application = (MyApplication) getApplication();
        pref = AppPreferences.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        application.setCurrentActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        clearReferences();
    }

    @Override
    protected void onDestroy() {
        clearReferences();
        super.onDestroy();
    }

    private void clearReferences(){
        Activity currActivity = application.getCurrentActivity();
        if (currActivity != null && currActivity.equals(this))
            application.setCurrentActivity(null);
    }

    public void showSimpleInfoDialog(String msg, boolean cancelable, String key) {
        SimpleInfoDialog simpleInfoDialog = SimpleInfoDialog.newInstance(msg, cancelable, key);
        simpleInfoDialog.show(getSupportFragmentManager(), "simpleInfoDialog");
    }

    public void doDismiss(String key) {
        switch (key) {
            case "api_timeout":
                break;
        }
    }

    public synchronized void showServerRequestProcessingLayout(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (serverRequestProcessingLayout == null) {
                    serverRequestProcessingLayout = new ServerRequestProcessingLayout(msg, BaseActivity.this);
                }
                if (!serverRequestProcessingLayout.isVisible() && !isFinishing()) {
                    serverRequestProcessingLayout.showProcessLayout();
                }
                hideProcessingLayout = new Runnable() {
                    @Override
                    public void run() {
                        hideServerRequestProcessingLayout();
                        showSimpleInfoDialog(getString(R.string.api_error), true, "api_timeout");
                    }
                };
                mHandler.postDelayed(hideProcessingLayout, Constants.API_CONNECTION_TIMEOUT);
            }
        });
    }

    public synchronized void hideServerRequestProcessingLayout(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (hideProcessingLayout != null) {
                    mHandler.removeCallbacks(hideProcessingLayout);
                    hideProcessingLayout = null;
                }
                if (serverRequestProcessingLayout != null) {
                    serverRequestProcessingLayout.hideProcessLayout();
                }
            }
        });
    }
}
