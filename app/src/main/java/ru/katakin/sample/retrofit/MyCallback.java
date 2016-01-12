package ru.katakin.sample.retrofit;

import android.content.Context;

import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import ru.katakin.sample.Constants;
import ru.katakin.sample.ui.BaseActivity;

public class MyCallback<T> implements Callback<T> {
    private GlobalCallback callback;
    private BaseActivity baseActivity;
    private Context context;
    private Constants.SERVICE_MODE mode;

    public MyCallback(Context context, GlobalCallback callback, boolean showProgress, Constants.SERVICE_MODE mode) {
        this.context = context;
        this.callback = callback;
        if (context != null && context instanceof BaseActivity) {
            baseActivity = (BaseActivity) context;
        }
        this.mode = mode;
        if (showProgress) {
            startProgress();
        }
    }

    public void startProgress() {
        if (baseActivity != null)
            baseActivity.showServerRequestProcessingLayout(null);
    }

    public void stopProgress() {
        if (baseActivity != null)
            baseActivity.hideServerRequestProcessingLayout();
    }

    @Override
    public void onResponse(Response<T> response) {

        if (response.isSuccess()) {
            T t = response.body();
            callback.onSuccess(t, mode);
        } else {
            int statusCode = response.code();

            // handle request errors yourself
            ResponseBody errorBody = response.errorBody();

            callback.onFailure(errorBody.toString(), mode);
        }

        stopProgress();
    }

    @Override
    public void onFailure(Throwable t) {
        callback.onFailure(t.getMessage(), mode);
        stopProgress();
//        Error e = new Error();
//        if (error.getKind() == RetrofitError.Kind.CONVERSION) {
//            e.setText(context.getString(R.string.conversion_error));
//        } else if (error.getKind() == RetrofitError.Kind.HTTP) {
//            e.setText(context.getString(R.string.http_error));
//        } else if (error.getKind() == RetrofitError.Kind.NETWORK) {
//            e.setText(context.getString(R.string.no_internet));
//        } else if (error.getKind() == RetrofitError.Kind.UNEXPECTED) {
//            e.setText(context.getString(R.string.api_error));
//        }
//        if (callback != null) callback.onFailure(e, mode);
    }

}