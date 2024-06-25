package com.example.weatherapp.Model;
import com.google.gson.annotations.SerializedName;
import java.util.*;
public class PreceptionData {
    @SerializedName("daily")
    private DailyData dailyData;

    public DailyData getDailyData() {
        return dailyData;
    }

    public static class DailyData {
        @SerializedName("precipitation_probability_max")
        private List<Integer> precipitationProbabilityMax;

        public List<Integer> getPrecipitationProbabilityMax() {
            return precipitationProbabilityMax;
        }
    }
}