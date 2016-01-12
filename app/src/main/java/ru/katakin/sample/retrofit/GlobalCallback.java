package ru.katakin.sample.retrofit;

import ru.katakin.sample.Constants;

public interface GlobalCallback {
    public void onFailure(String error, Constants.SERVICE_MODE mode);
    public void onSuccess(Object object, Constants.SERVICE_MODE mode);
}
