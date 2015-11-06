package ru.katakin.sample;

import android.app.Activity;
import android.app.Application;

import java.lang.ref.WeakReference;

public class MyApplication extends Application {

    private WeakReference<Activity> currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();
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
