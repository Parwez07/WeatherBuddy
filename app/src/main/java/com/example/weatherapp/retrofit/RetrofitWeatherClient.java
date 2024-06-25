package com.example.weatherapp.retrofit;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitWeatherClient {

    static String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    static String Preception_URL = "https://api.open-meteo.com/v1/";
    static String Hourly_URL = "https://api.open-meteo.com/v1/";
    private static Retrofit retrofit;
    private static Retrofit getPreceptionData;
    private static Retrofit getHourlyData;

    public static Retrofit getClient() {

        if (retrofit == null) {

            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }

        return retrofit;
    }

    public static Retrofit getPreceptionData() {
        Log.d("Prec", " retro client ke under");
        if (getPreceptionData == null) {
            getPreceptionData = new Retrofit.Builder().baseUrl(Preception_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return getPreceptionData;
    }

    public static Retrofit getGetHourlyData() {
        Log.d("hourly", "inside the retro client ");
        if (getHourlyData == null) {
           getHourlyData = new Retrofit.Builder().baseUrl(Hourly_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return getHourlyData;
    }

}
