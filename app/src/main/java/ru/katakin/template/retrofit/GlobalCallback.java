package ru.katakin.template.retrofit;

import ru.katakin.template.Constants;

public interface GlobalCallback {
    public void onFailure(String error, Constants.SERVICE_MODE mode);
    public void onSuccess(Object object, Constants.SERVICE_MODE mode);
}
