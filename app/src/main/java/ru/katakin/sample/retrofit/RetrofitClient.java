package ru.katakin.sample.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import ru.katakin.sample.Constants;

public class RetrofitClient {
    private API service;

    public RetrofitClient() {

        OkHttpClient okHttpClient = new okhttp3.OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .connectTimeout(Constants.API_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(Constants.API_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(Constants.API_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVER_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(API.class);
    }

    public API getService() {
        return service;
    }


}