package ru.katakin.template.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import ru.katakin.template.Constants;
import ru.katakin.template.preferences.AppPreferences;

//import com.google.android.gms.gcm.GcmPubSub;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
//    private static final String[] TOPICS = {"global"};
    private AppPreferences pref;

    public RegistrationIntentService() {
        super(TAG);
        pref = AppPreferences.getInstance(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String regid = instanceID.getToken(Constants.SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            sendRegistartionToServer(regid);

//            subscribeTopics(regid);
        } catch (IOException e) {
            pref.setRegDate(0);
        }

        Intent registrationComplete = new Intent(Constants.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

//    private void subscribeTopics(String regid) throws IOException {
//        GcmPubSub pubSub = GcmPubSub.getInstance(this);
//        for (String topic : TOPICS) {
//            pubSub.subscribe(regid, "/topics/" + topic, null);
//        }
//    }

    private void sendRegistartionToServer(String regid) {
        pref.setRegDate(System.currentTimeMillis());
        //TODO send regid
    }
}
