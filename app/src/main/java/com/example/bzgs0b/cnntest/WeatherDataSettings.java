package com.example.bzgs0b.cnntest;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by bzgs0b on 10/10/16.
 */

public class WeatherDataSettings {

    private String API_KEY = "";//"0b33e4ca4f2d1892503cf0482706b350";
    private String API_SERVER = "";//"http://api.openweathermap.org/data/2.5/";
    private String[] API_ENDPOINTS = {"weather","forecast/daily"};
    private int DaysToGet = 9;//14;
    private String LOCATION;
    public WeatherDataSettings(Context context){

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            DaysToGet = sharedPrefs.getInt("pref_days",8);
            API_SERVER = sharedPrefs.getString("pref_apiserver", "http://api.openweathermap.org/data/2.5/");
            API_KEY = sharedPrefs.getString("pref_apikey", "0b33e4ca4f2d1892503cf0482706b350");
            LOCATION = sharedPrefs.getString("perf_location", "Atlanta,ga");
        }
    public String getForecastURL(){

        return getForecastURL(LOCATION, "imperial");
    }
    public String getForecastURL(String city, String units){
        String result = API_SERVER + API_ENDPOINTS[1] + "?q=" + LOCATION + "&units=" + units + "&appid=" +API_KEY+"&cnt=" + DaysToGet;
        return result;

    }

    public String getApiURL() {
        String units = "imperial";
        return getApiURL(LOCATION, units);
    }


    public String getApiURL(String city, String units){
        String result = API_SERVER + API_ENDPOINTS[0] + "?q=" + LOCATION + "&units=" + units + "&apikey=" +API_KEY;
        return result;
    }
}
