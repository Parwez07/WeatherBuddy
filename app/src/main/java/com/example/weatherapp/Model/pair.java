package com.example.weatherapp.Model;

public class pair {
    String time;
    double temp;
    int weatherCode;
    public pair(String time, double temp, int weatherCode){
        this.temp = temp;
        this.time = time;
        this.weatherCode = weatherCode;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public int getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(int weatherCode) {
        this.weatherCode = weatherCode;
    }
}
