package ru.katakin.template.retrofit;

import android.content.Context;
import android.location.Location;

import retrofit2.Call;
import ru.katakin.template.Constants;
import ru.katakin.template.model.Data;


public class Manager {

    private static Manager manager;
    private API api;
    private Context context;

    public static synchronized Manager getInstance(Context context){
        if (manager == null){
            manager = new Manager(context);
        }
        return manager;
    }

    private Manager(Context context){
        this.context = context.getApplicationContext();
        api = new RetrofitClient().getService();
    }

    public void getWeatherByName(String city_name, MyCallback<Data> callback) {
        Call<Data> call = api.getWeatherByName(city_name, Constants.API_KEY);
        call.enqueue(callback);
    }

    public void getWeatherByGeo(Location location, MyCallback<Data> callback) {
        String latitude = location.getLatitude() + "";
        String longitude = location.getLongitude() + "";

        Call<Data> call = api.getWeatherByGeo(latitude, longitude, Constants.API_KEY);
        call.enqueue(callback);
    }
}
