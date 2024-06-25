package com.example.weatherapp.Model;

import com.google.gson.annotations.SerializedName;
import  java.util.*;
public class HourlyData {
        @SerializedName("hourly")
        private Hourly hourly;

        public Hourly getHourly() {
            return hourly;
        }

        public void setHourly(Hourly hourly) {
            this.hourly = hourly;
        }

        public static class Hourly {
            @SerializedName("temperature_2m")
            private List<Double> temperatureList;

            @SerializedName("weather_code")
            private List<Integer> weatherCodeList;

            public List<Double> getTemperatureList() {
                return temperatureList;
            }

            public void setTemperatureList(List<Double> temperatureList) {
                this.temperatureList = temperatureList;
            }

            public List<Integer> getWeatherCodeList() {
                return weatherCodeList;
            }

            public void setWeatherCodeList(List<Integer> weatherCodeList) {
                this.weatherCodeList = weatherCodeList;
            }
        }
    }
