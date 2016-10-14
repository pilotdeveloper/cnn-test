package com.example.bzgs0b.cnntest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ScrollingView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;


public class WeatherData extends AppCompatActivity {
    private static final String TAG = WeatherNetworkData.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        WeatherDataSettings api = new WeatherDataSettings();
        new GetWeatherData().execute();

    }



    private class GetWeatherData extends AsyncTask<String, Void, String[]> {

        public String[] weatherReturn = new String[2];

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


        @Override
        protected String[] doInBackground(String... urls) {


            InputStream is = null;
            InputStream is2 = null;

            int len = 500;
            WeatherDataSettings settings = new WeatherDataSettings();

            try {
                URL url = new URL(settings.getApiURL());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                Log.d(TAG, "The response is: " + response);
                is = conn.getInputStream();
                // Convert the InputStream into a string
                weatherReturn[0] = readIt(is);

                URL url2 = new URL(settings.getForecastURL());
                HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
                conn2.setReadTimeout(10000 /* milliseconds */);
                conn2.setConnectTimeout(15000 /* milliseconds */);
                conn2.setRequestMethod("GET");
                conn2.setDoInput(true);
                // Starts the query
                conn2.connect();
                int response2 = conn2.getResponseCode();
                Log.d(TAG, "The response is: " + response2);
                is2 = conn2.getInputStream();



                // Convert the InputStream into a string
                weatherReturn[1] = readIt(is2);
                Log.d(TAG, "The response is: " + weatherReturn[1]);

                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            }
            catch (Exception e) {
                Log.v("WTF","WTF");
            }



            // params comes from the execute() call: params[0] is the url.
//            try {
//                WeatherDataSettings api = new WeatherDataSettings();
//                WeatherNetworkData dataHandler = new WeatherNetworkData();
//                return  dataHandler.WeatherNetworkData();
//                //return "{\"coord\":{\"lon\":-84.39,\"lat\":33.75},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"02d\"}],\"base\":\"stations\",\"main\":{\"temp\":79.94,\"pressure\":1000.07,\"humidity\":35,\"temp_min\":79.94,\"temp_max\":79.94,\"sea_level\":1032.61,\"grnd_level\":1000.07},\"wind\":{\"speed\":4.41,\"deg\":342.507},\"clouds\":{\"all\":8},\"dt\":1476391649,\"sys\":{\"message\":0.168,\"country\":\"US\",\"sunrise\":1476358916,\"sunset\":1476399865},\"id\":4180439,\"name\":\"Atlanta\",\"cod\":200}";
//            } catch (Exception e) {
//                return "Unable to retrieve web page. URL may be invalid.";
//            }
            return weatherReturn;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String[] result) {
            JSONObject currentWeather = null;
            JSONObject forecastWeather = null;
            try {
                currentWeather = new JSONObject(result[0]);
                forecastWeather = new JSONObject(result[1]);

            } catch (Exception e) {
                e.printStackTrace();
                Log.v("CNNTEST", "error");

            }
            updateWeather(currentWeather,forecastWeather);
            //textView.setText(result);
        }
    }


    private void updateWeather(JSONObject currentWeather, JSONObject forecastWeather){

        ///this is only todays weather
        Log.v(TAG, currentWeather.toString());
        Log.v(TAG, forecastWeather.toString());

        String highVal = null, lowVal = null;
        String currentWeatherString = null;
        String currentWeatherIcon = null;
        //today's forcast
                try {
                    JSONObject mainWeather = currentWeather.getJSONObject("main");

                    // tempVal = mainWeather.getString("temp");
                    highVal = Math.round(mainWeather.getDouble("temp_max")  - 32.0 * (5.0/9.0)) + "°";
                    lowVal = Math.round(mainWeather.getDouble("temp_min") - 32.0 * (5.0/9.0)) + "°";


                    JSONObject weather = (currentWeather.getJSONArray("weather").getJSONObject(0));
                    currentWeatherString = weather.getString("main");
                    currentWeatherIcon = weather.getString("icon");
                    //imageView
                    //JSONArray weatherArray = currentWeather.getJSONArray("weather");
                } catch (JSONException e) {
                    // Oops
            }
        LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.content_weather_weekview_today, null, false);
        TextView text = (TextView)layout.findViewById(R.id.tempHigh);
        text.setText(highVal);
        text = (TextView)layout.findViewById(R.id.tempLow);
        text.setText(lowVal);
        text = (TextView) layout.findViewById(R.id.weatherStatus);
        text.setText(currentWeatherString);
        ImageView imagePic = (ImageView)layout.findViewById(R.id.imageView);
        getImageView(imagePic,currentWeatherIcon );
        //imagePic.setImageResource(R.drawable.art_clear);


       LinearLayout linear = (LinearLayout)findViewById(R.id.main_container);
       linear.addView(layout);


            try{
                //weekly forecast
                JSONArray listData =  forecastWeather.getJSONArray("list");
                for (int i=1; i<listData.length(); i++){
                    JSONObject tempList = listData.getJSONObject(i);
                    String dateFormat = "MM/dd/yyyy";

                    JSONObject temperatures = tempList.getJSONObject("temp");
                    JSONObject weather = (tempList.getJSONArray("weather").getJSONObject(0));


                    highVal = Math.round(temperatures.getDouble("max") - 32.0 * (5.0/9.0)) + "°";
                    lowVal =  Math.round(temperatures.getDouble("min") - 32.0 * (5.0/9.0)) + "°";
                    currentWeatherIcon = weather.getString("icon");

                    String wData = weather.getString("main");

                    layout = (RelativeLayout) inflater.inflate(R.layout.content_weather_weekview_day, null, false);



                    text = (TextView) layout.findViewById(R.id.tempHigh);
                    text.setText(highVal);

                    text = (TextView) layout.findViewById(R.id.tempLow);
                    text.setText(lowVal);

                    text = (TextView) layout.findViewById(R.id.date);
                    if (i == 1)
                        text.setText("Tomorrow");
                    else
                        text.setText(DateFormat.format(dateFormat, Long.parseLong(tempList.getString("dt") + "000")).toString());

                    text = (TextView) layout.findViewById(R.id.weatherStatus);
                    text.setText(wData);
                             imagePic = (ImageView)layout.findViewById(R.id.imageView);
                     getImageView(imagePic,currentWeatherIcon );

                    //linear = (LinearLayout) findViewById(R.id.main_container);
                    linear.addView(layout);


                }

            }catch (Exception e){

            }
           // LayoutInflater rinflater = LayoutInflater.from(this);
       }
        //    <include layout="@layout/content_weather_weekview_today" />
        //weatherJSON = weatherTemp;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weather_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getImageView(ImageView img, String state)                {


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



    }
}
