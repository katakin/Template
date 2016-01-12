package ru.katakin.sample.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.katakin.sample.model.Data;

public interface API {

    @GET("weather")
    Call<Data> getWeatherByName(@Query("q") String city, @Query("appid") String appid);

    @GET("weather")
    Call<Data> getWeatherByGeo(@Query("lat") String lat, @Query("lon") String lon, @Query("appid") String appid);
}
