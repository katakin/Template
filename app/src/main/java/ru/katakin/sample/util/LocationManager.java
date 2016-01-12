package ru.katakin.sample.util;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import de.greenrobot.event.EventBus;

public class LocationManager implements
        LocationListener {

    private static LocationManager instance;

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;

    public static LocationManager getInstance(Context applicationContext) {
        return instance != null ? instance : (instance = new LocationManager(applicationContext));
    }

    private LocationManager(Context applicationContext) {
        createLocationRequest();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(applicationContext)
                    .addApi(LocationServices.API)
                    .build();
        }
        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting())
            mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        EventBus.getDefault().postSticky(location);
        stopLocationUpdates();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setSmallestDisplacement(10);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    public void startLocationUpdates() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            if (!mGoogleApiClient.isConnecting())
                mGoogleApiClient.connect();
        }
    }

    private void stopLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }
}
