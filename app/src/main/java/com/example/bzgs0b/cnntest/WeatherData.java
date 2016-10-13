package com.example.bzgs0b.cnntest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;



public class WeatherData extends AppCompatActivity {
    private static final String TAG = WeatherNetworkData.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_data);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

       //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
       //     public void onClick(View view) {
       //         Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
       //                 .setAction("Action", null).show();
       //     }
       // });
        WeatherDataSettings api = new WeatherDataSettings();
        new DownloadWebpageTask().execute();
         //   new Thread(new Runnable() {
         //       public void run() {
                //    URL url = new URL(api.getApiURL());
                //    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //    try {
                //        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                //        readStream(in);
                //    } finally {
                //    urlConnection.disconnect();
                //  }
                //    InputStream inputStream = null;
                //    String result = null;
                //    try {
                //        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                //        StringBuilder sb = new StringBuilder();
                //        String line = null;
                //        while ((line = reader.readLine()) != null)
                //        {
                //            sb.append(line + "\n");
                //        }
                //        result = sb.toString();
                //            try {
                //                updateWeather(new JSONObject(result));
                //                Context context = getApplicationContext();
                //                CharSequence text = "Weather Data Loaded";
                //                int duration = Toast.LENGTH_SHORT;
                //                Toast toast = Toast.makeText(context, text, duration);
                //                toast.show();
                //                }catch(Exception e){
                //                //yeah no
                 //            }
                 //       } catch (Exception e) {
                        // Oops
                 //   }
                 //   finally {
                 //       try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
                 //   }
                    //Bitmap b = loadImageFromNetwork("http://example.com/image.png");
                    //mImageView.setImageBitmap(b);
         //       }
    //}).start();
       // }


        /*

        JSONObject jObject = new JSONObject(result);
To get a specific String

String aJsonString = jObject.getString("StringNAME");
To get a specific boolean

boolean aJsonBoolean = jObject.getBoolean("BOOLEANNAME");
To get a specific integer

int aJsonInteger = jObject.getInt("INTEGERNAME");
To get a specific long

long aJsonLong = jObject.getBoolean("LONGNAME");
To get a specific double

double aJsonDouble = jObject.getDouble("DOUBLENAME");
To get a specific JSONArray:

JSONArray jArray = jObject.getJSONArray("ARRAYNAME");
To get the items from the array

for (int i=0; i < jArray.length(); i++)
{
    try {
        JSONObject oneObject = jArray.getJSONObject(i);
        // Pulling items from the array
        String oneObjectsItem = oneObject.getString("StringNAMEinTHEarray");
        String oneObjectsItem2 = oneObject.getString("anotherStringNAMEINtheARRAY");
    } catch (JSONException e) {
        // Oops
    }
}


http://api.openweathermap.org/data/2.5/weather?
q=Atlanta,ga&units=imperial

http://api.openweathermap.org/data/2.5/weather?
q=Atlanta,ga&units=imperial&

http://api.openweathermap.org/data/2.5/weather?q=Atlanta,ga&units=imperial&apikey=212cc505fc02d9a4eeb5f9dd365db68c
212cc505fc02d9a4eeb5f9dd365db68c


         */


    }



    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {

        public String readIt(InputStream stream, int len) throws IOException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);
        }



        @Override
        protected String doInBackground(String... urls) {


            InputStream is = null;
            // Only display the first 500 characters of the retrieved
            // web page content.
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
                String contentAsString = readIt(is, len);
                return contentAsString;

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
            return "error";
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            JSONObject r = null;
            try {
                r = new JSONObject(result);
            } catch (Exception e) {
                e.printStackTrace();
                Log.v("CNNTEST", "error");

            }
            updateWeather(r);
            //textView.setText(result);
        }
    }


    private void updateWeather(JSONObject weatherTemp){
        JSONObject weatherJSON = weatherTemp;

        Log.v("CNNTEST", weatherJSON.toString());
        //weatherJSON = weatherTemp;

    }
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
}
