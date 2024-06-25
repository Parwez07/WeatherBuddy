package com.example.weatherapp.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.Adapter.HourlyAdapter;
import com.example.weatherapp.Model.HourlyData;
import com.example.weatherapp.Model.OpenWeatherMap;
import com.example.weatherapp.Model.PreceptionData;
import com.example.weatherapp.Model.pair;
import com.example.weatherapp.R;
import com.example.weatherapp.retrofit.RetrofitWeatherClient;
import com.example.weatherapp.retrofit.WeatherAPI;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView cityName, weatherCondition, temp, humidity, maxTemp, minTemp, percipitation, wind, tvDate, nextDay;
    ImageView imgStatus;
    Button btnSearch;
    EditText etSearch;

    RecyclerView rvDaily;
    HourlyAdapter hourlyAdapter ;

    LocationManager locationManager;
    LocationListener locationListener;

    double lat, lon;

    List<pair> hourlyDataList;
    WeatherAPI weatherAPI = RetrofitWeatherClient.getClient().create(WeatherAPI.class);
    WeatherAPI preceptionData = RetrofitWeatherClient.getPreceptionData().create(WeatherAPI.class);

    WeatherAPI hourlyData = RetrofitWeatherClient.getGetHourlyData().create(WeatherAPI.class);

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        cityName = findViewById(R.id.tvCityName);
        weatherCondition = findViewById(R.id.tvStatus);
        temp = findViewById(R.id.tvTemp);
        humidity = findViewById(R.id.tvHumidPercent);
        maxTemp = findViewById(R.id.tvMax);
        minTemp = findViewById(R.id.tvMin);
        percipitation = findViewById(R.id.tvRainChance);
        wind = findViewById(R.id.tvWindSpeed);
        tvDate = findViewById(R.id.tvDate);
        nextDay = findViewById(R.id.tvNext);
        btnSearch = findViewById(R.id.btnSearch);
        imgStatus = findViewById(R.id.img);
        etSearch = findViewById(R.id.etSearch);
        rvDaily = findViewById(R.id.rvDaily);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = location -> {

            lat = location.getLatitude();
            lon = location.getLongitude();

            Call<OpenWeatherMap> call = weatherAPI.getWeatherWithLocation(lat, lon);
            Call<PreceptionData> preceptionDataCall = preceptionData.getPreceptionData(lat,lon);
            Call<HourlyData> hourlyDataCall = hourlyData.getHourlyData(lat,lon);


            getWeatherData(call);
            getPreceptionData(preceptionDataCall);
            getHourlyData(hourlyDataCall);

            Log.d("lat ", lat + " " + " lon " + lon);
        };

        if (!hasLocationPermission(this)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 50, locationListener);
        }

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String cityName = etSearch.getText().toString().trim();
                Call<OpenWeatherMap> call = weatherAPI.getWeatherWithCityName(cityName);
                etSearch.setText("");
                getWeatherData(call);
            }
        });
    }

    public void getWeatherData(Call<OpenWeatherMap> call) {

        call.enqueue(new Callback<OpenWeatherMap>() {
            @Override
            public void onResponse(Call<OpenWeatherMap> call, Response<OpenWeatherMap> response) {

                if(response.isSuccessful()){
                    cityName.setText(response.body().getName() + " , " + response.body().getSys().getCountry());
                    weatherCondition.setText(response.body().getWeather().get(0).getDescription());

                    long mil = response.body().getDt();
                    String time = time(mil);
                    tvDate.setText(time);
                    temp.setText(response.body().getMain().getTemp() + " °C");
                    maxTemp.setText(response.body().getMain().getTempMax()+" °C");
                    minTemp.setText(response.body().getMain().getTempMin()+" °C");
                    wind.setText(response.body().getWind().getSpeed()+" m/s");
                    humidity.setText(response.body().getMain().getHumidity()+" %");
                    String icon = response.body().getWeather().get(0).getIcon();

                    Picasso.get().load("https://openweathermap.org/img/wn/"+icon+"@2x.png")
                            .placeholder(R.drawable.cloudy_sunny)
                            .into(imgStatus);
                    Log.d("response",icon);
                }
                else{
                    Toast.makeText(MainActivity.this, "City Name is not correct ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OpenWeatherMap> call, Throwable throwable) {

            }
        });
    }

    public void getPreceptionData(Call<PreceptionData>call){

        call.enqueue(new Callback<PreceptionData>() {
            @Override
            public void onResponse(Call<PreceptionData> call, Response<PreceptionData> response) {
                if(response.isSuccessful()){
                    PreceptionData data = response.body();
                    if(data !=null){
                        List<Integer> preceptionList = data.getDailyData().getPrecipitationProbabilityMax();
                        percipitation.setText(preceptionList.get(0)+" %");
                        Log.d("Prec", preceptionList.get(0)+"");
                    }
                    Log.d("Prec", "main me hai ");
                }
                else{
                    Toast.makeText(MainActivity.this, "preceptions not loaded", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PreceptionData> call, Throwable throwable) {

            }
        });
    }

    public void getHourlyData(Call<HourlyData>call){
        call.enqueue(new Callback<HourlyData>() {
            @Override
            public void onResponse(Call<HourlyData> call, Response<HourlyData> response) {
                if(response.isSuccessful()){
                    HourlyData data = response.body();
                    if(data !=null){
                        hourlyDataList = getList(data);
                        hourlyAdapter = new HourlyAdapter(getApplicationContext(),hourlyDataList);
                        rvDaily.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
                        rvDaily.setAdapter(hourlyAdapter);
                        Log.d(" hourly ", "inside getourlyData "+hourlyDataList.size());
                    }
                }
            }

            @Override
            public void onFailure(Call<HourlyData> call, Throwable throwable) {
                Toast.makeText(MainActivity.this,"Data not Loaded ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public List<pair> getList(HourlyData data){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH a", Locale.getDefault());
        int currHour = calendar.get(calendar.HOUR_OF_DAY);
        List<pair> list = new ArrayList<>();
        String time = sdf.format(calendar.getTime());
        list.add(new pair(time,data.getHourly().getTemperatureList().get(currHour),data.getHourly().getWeatherCodeList().get(currHour)));
        for(int i = currHour;i<currHour+24;i++){
            calendar.add(Calendar.HOUR_OF_DAY,1);
            time= sdf.format(calendar.getTime());
            double temp = data.getHourly().getTemperatureList().get(i+1);
            int wc = data.getHourly().getWeatherCodeList().get(i+1);
            list.add(new pair(time,temp,wc));
        }
        Log.d("hourly", currHour+" iside getlist");
        return list;
    }
    public String time(long mil) {
        Date date = new Date(mil);
        SimpleDateFormat format = new SimpleDateFormat("dd MMM ");
        SimpleDateFormat format1 = new SimpleDateFormat("hh : mm a");
        String d = format.format(date.getTime());
        String time = format1.format(date.getTime());
        Log.d("time22", "date " + d + " time " + time);
        time = d + " | " + time;
        return time;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1 && permissions.length > 0 && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 50, locationListener);
        }
    }

    public static boolean hasLocationPermission(Context context) {
        // Check if the ACCESS_FINE_LOCATION or ACCESS_COARSE_LOCATION permission has been granted
        return ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}