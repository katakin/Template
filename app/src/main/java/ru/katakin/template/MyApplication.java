package ru.katakin.template;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import java.lang.ref.WeakReference;

public class MyApplication extends Application {

    private WeakReference<Activity> currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        MultiDex.install(this);
        super.attachBaseContext(base);
    }

    public Activity getCurrentActivity(){
        return (currentActivity != null) ? currentActivity.get() : null;
    }

    public void setCurrentActivity(Activity currentActivity){
        if (currentActivity != null) {
            this.currentActivity = new WeakReference<>(currentActivity);
        } else {
            this.currentActivity = null;
        }
    }
}
