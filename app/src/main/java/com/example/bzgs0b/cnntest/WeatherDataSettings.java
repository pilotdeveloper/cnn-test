package com.example.bzgs0b.cnntest;

/**
 * Created by bzgs0b on 10/10/16.
 */

public class WeatherDataSettings {

    private String API_KEY = "0b33e4ca4f2d1892503cf0482706b350";
    private String API_SERVER = "http://api.openweathermap.org/data/2.5/";
    private String[] API_ENDPOINTS = {"weather","forecast/daily"};
    private int DaysToGet = 14;
    //http://api.openweathermap.org/data/2.5/weather?
    //q=Atlanta,ga&units=imperial

    //get Weather Strings
    //getCurrentWeather
    //getForecastedWeather

    public String getForecastURL(){
        return getForecastURL("Atlanta,ga", "imperial");
    }
    public String getForecastURL(String city){
        String units = "imperial";
        return getForecastURL(city, units);
    }
    public String getForecastURL(String city, String units){
        String result = API_SERVER + API_ENDPOINTS[1] + "?q=" + city + "&units=" + units + "&appid=" +API_KEY+"&cnt=" + DaysToGet;
//would be better to build the query String using androids builder... this was done for time
        //add error handling for unsupported units
        return result;

    }

    public String getApiURL() {
        String location = "Atlanta,ga";
        String units = "imperial";
        return getApiURL(location, units);
    }

    public String getApiURL(String city){

        return getApiURL(city, "imperial");
    }

    public String getApiURL(String city, String units){
        String result = API_SERVER + API_ENDPOINTS[0] + "?q=" + city + "&units=" + units + "&apikey=" +API_KEY;
//would be better to build the query String using androids builder... this was done for time
        //add error handling for unsupported units
        return result;
    }

}
