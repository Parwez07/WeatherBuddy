package com.example.weatherapp.retrofit;

import com.example.weatherapp.Model.HourlyData;
import com.example.weatherapp.Model.OpenWeatherMap;
import com.example.weatherapp.Model.PreceptionData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {

    @GET("weather?appid=d36b61bc43203f6e3dac21dd6d5e86d5&units=metric")
    Call<OpenWeatherMap>getWeatherWithLocation(@Query("lat")double lat,@Query("lon")double lon);

    @GET("forecast?&daily=precipitation_probability_max")
    Call<PreceptionData>getPreceptionData(@Query("latitude")double lat, @Query("longitude")double lon);

    @GET("weather?appid=d36b61bc43203f6e3dac21dd6d5e86d5&units=metric")
    Call<OpenWeatherMap>getWeatherWithCityName(@Query("q")String name);

    @GET("dwd-icon?&hourly=temperature_2m,weather_code&forecast_days=3")
    Call<HourlyData> getHourlyData(@Query("latitude")double lat, @Query("longitude")double lon);
}
