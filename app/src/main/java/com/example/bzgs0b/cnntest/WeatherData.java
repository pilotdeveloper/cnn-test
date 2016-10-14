package com.example.bzgs0b.cnntest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;

import java.net.HttpURLConnection;
import java.net.URL;


public class WeatherData extends AppCompatActivity {
    private static final String TAG = WeatherData.class.getSimpleName();
    JSONObject currentWeather = null;
    JSONObject forecastWeather = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_weather_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        new GetWeatherData().execute();

    }

    @Override
    public void onResume(){
        super.onResume();

        setContentView(R.layout.activity_weather_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        new GetWeatherData().execute();
    }

    private class GetWeatherData extends AsyncTask<String, Void, String[]> {
        public String[] weatherReturn = new String[2];
        @Override
        protected String[] doInBackground(String... urls) {


            InputStream is = null;
            InputStream is2 = null;

            int len = 500;
            WeatherDataSettings settings = new WeatherDataSettings(getBaseContext());

            try {

                weatherReturn[0] = getJSONData(settings.getApiURL());
                weatherReturn[1] = getJSONData(settings.getForecastURL());

                Log.d(TAG, "The response is: " + weatherReturn[1]);
            } catch (Exception e) {
                Log.v("WTF", "WTF");
            }
            return weatherReturn;
        }

        @Override
        protected void onPostExecute(String[] result) {
            updateGlobal(result[0], result[1]);
        }
    }
    //convert input stream to string
    public String readIt(InputStream stream) throws IOException {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(stream));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
    //grabs the actual data
    private String getJSONData(String urlString){
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(TAG, "The response is: " + response);
            return readIt(conn.getInputStream());
        }
    catch (Exception e){}
        return "{\"error\" : \"wtf\"}";
    }
    //updates global variables
    private void updateGlobal(String c, String f){
        try {
            currentWeather = new JSONObject(c);
            forecastWeather = new JSONObject(f);
            processWeather();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processWeather() {
        Log.v(TAG, currentWeather.toString());
        Log.v(TAG, forecastWeather.toString());

        String highVal = null, lowVal = null;
        String currentWeatherString = null;
        String currentWeatherIcon = null;
        try {
            //this is a huge mess. Normally, I'd make a full class (or use a third party library)
            //to contain these JSON objects; however, given that I only need a few data points
            //combined with the fact this will likely never see the daylight again, I'm just
            //directly calling the JSON objects.  I'm well aware this is perhaps the WORST
            //implementation possible, but I had to find a way to cut time >.>
            JSONObject mainWeather = currentWeather.getJSONObject("main");
            highVal = getTemperature(mainWeather.getDouble("temp_max"));
            lowVal = getTemperature(mainWeather.getDouble("temp_min"));
            JSONObject weather = (currentWeather.getJSONArray("weather").getJSONObject(0));
            currentWeatherString = weather.getString("main");
            currentWeatherIcon = weather.getString("icon");
        } catch (JSONException e) {
        }

        //I am setting up the "current" weather seperately since this is a completely different view
        //and template from the other days
        LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.content_weather_weekview_today, null, false);
        TextView text = (TextView) layout.findViewById(R.id.tempHigh);
        text.setText(highVal);
        text = (TextView) layout.findViewById(R.id.tempLow);
        text.setText(lowVal);
        text = (TextView) layout.findViewById(R.id.weatherStatus);
        text.setText(currentWeatherString);

        //at least I did get the images based off the data (in a switch)
        ImageView imagePic = (ImageView) layout.findViewById(R.id.imageView);
        getImageView(imagePic, currentWeatherIcon);

        LinearLayout linear = (LinearLayout) findViewById(R.id.main_container);
        //each item should have it's own listener so I can pass my poorly designed JSON Object above
        //to give the user a proper experience
        setOnClickListener(layout, "currentWeather");
        //tag is for later
        linear.removeAllViews();
        linear.addView(layout);



        try {
            //Weekly Forecast Parsing... Same thing, I know it's terrible
            JSONArray listData = forecastWeather.getJSONArray("list");
            for (int i = 0; i < listData.length()-1; i++) {
                JSONObject tempList = listData.getJSONObject(i+1);
                String dateFormat = "MM/dd/yyyy";
                //used to get the temps below
                JSONObject temperatures = tempList.getJSONObject("temp");
                //used to get the "main" which is weather type and images
                JSONObject weather = (tempList.getJSONArray("weather").getJSONObject(0));

                //Set the temps to degrees F or C based on stored preferences
                    highVal = getTemperature(temperatures.getDouble("max"));
                    lowVal = getTemperature(temperatures.getDouble("min"));

                currentWeatherIcon = weather.getString("icon");

                String wData = weather.getString("main");
                //new layout from a template
                layout = (RelativeLayout) inflater.inflate(R.layout.content_weather_weekview_day, null, false);
                text = (TextView) layout.findViewById(R.id.tempHigh);
                text.setText(highVal);

                text = (TextView) layout.findViewById(R.id.tempLow);
                text.setText(lowVal);

                text = (TextView) layout.findViewById(R.id.date);
                if (i == 0)
                    text.setText("Today");
                else if (i == 1)
                    text.setText("Tomorrow");
                else
                    text.setText(DateFormat.format(dateFormat, Long.parseLong(tempList.getString("dt") + "000")).toString());

                text = (TextView) layout.findViewById(R.id.weatherStatus);
                text.setText(wData);
                imagePic = (ImageView) layout.findViewById(R.id.imageView);
                getImageView(imagePic, currentWeatherIcon);

                setOnClickListener(layout, "dateTimeStamp:" + tempList.getString("dt"));

                linear.addView(layout);


            }

        } catch (Exception e) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weather_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        if (id == R.id.action_refresh)
        {
            setContentView(R.layout.activity_weather_data);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            new GetWeatherData().execute();
        }
        return super.onOptionsItemSelected(item);
    }

    public String getTemperature(double temp){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        //did they change it to F mode?
        boolean bAppUpdates = sharedPrefs.getBoolean("pref_celc",true);
        if (bAppUpdates) {
            return ( Math.round((temp - 32.0) * (5.0 / 9.0))) + "°C";
        }
        else{
            return Math.round(temp) + "°F";
        }
    }

    View.OnClickListener layoutlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Context context = getApplicationContext();
            CharSequence text = "Hello!  You clicked a very specific object that is identified by: " + v.getTag() + " \r\nNormally, this would have been implemented, but I ran out of time.";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            Log.v(TAG, v.getTag() + " ");
        }
    };


    public void setOnClickListener(RelativeLayout layout, String dt)
    {

        layout.setOnClickListener(layoutlistener);
        layout.setTag(dt);

        Log.v(TAG, layout.getTag() + " ");

    }

    public void getImageView(ImageView img, String state) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean iconMode = sharedPrefs.getBoolean("pref_icon",true);
        if (iconMode == true) {

            switch (state) {
                case "01d":
                    img.setImageResource(R.drawable.art_clear);
                    break;
                case "02d":
                    img.setImageResource(R.drawable.art_light_clouds);

                    break;
                case "03d":
                    img.setImageResource(R.drawable.art_clouds);

                    break;
                case "04d":
                    img.setImageResource(R.drawable.art_clouds);

                    break;
                case "09d":
                    img.setImageResource(R.drawable.art_light_rain);

                    break;
                case "10d":
                    img.setImageResource(R.drawable.art_rain);

                    break;
                case "11d":
                    img.setImageResource(R.drawable.art_storm);

                    break;
                case "13d":
                    img.setImageResource(R.drawable.art_snow);

                    break;
                case "50d":
                    img.setImageResource(R.drawable.art_fog);

                    break;
                default:
                    img.setImageResource(R.drawable.art_clear);
                    break;
            }
        }else {
            switch (state) {
                case "01d":
                    img.setImageResource(R.drawable.ic_clear);
                    break;
                case "02d":
                    img.setImageResource(R.drawable.ic_light_clouds);

                    break;
                case "03d":
                    img.setImageResource(R.drawable.ic_cloudy);

                    break;
                case "04d":
                    img.setImageResource(R.drawable.ic_cloudy);

                    break;
                case "09d":
                    img.setImageResource(R.drawable.ic_light_rain);

                    break;
                case "10d":
                    img.setImageResource(R.drawable.ic_rain);

                    break;
                case "11d":
                    img.setImageResource(R.drawable.ic_storm);

                    break;
                case "13d":
                    img.setImageResource(R.drawable.ic_snow);

                    break;
                case "50d":
                    img.setImageResource(R.drawable.ic_fog);

                    break;
                default:
                    img.setImageResource(R.drawable.ic_clear);
                    break;
            }
        }
    }
}
