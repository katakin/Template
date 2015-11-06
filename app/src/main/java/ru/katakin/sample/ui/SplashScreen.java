package ru.katakin.sample.ui;

import android.os.Bundle;

import ru.katakin.sample.R;
import ru.katakin.sample.util.GCM;

public class SplashScreen extends BaseActivity {

    private GCM gcm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        gcm = new GCM(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (google.checkPlayServices(this)) {
            gcm.registerGCM();
        }
    }
}
