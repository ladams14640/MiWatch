package com.miproducts.miwatch.Weather.openweather;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by larry on 7/13/15.
 * http://www.javacodegeeks.com/2013/06/android-build-real-weather-app-json-http-and-openweathermap.html
 */
public class WeatherHttpClient {

    private static final String TAG = "WeatherHTTPCLient";
    private static String BASE_URL_ZIP = "http://api.openweathermap.org/data/2.5/weather?zip=";
    private static String BASE_TOWN_STATE = "http://api.openweathermap.org/data/2.5/weather?q=";

   // String test= "http://openweathermap.org/city/4958141";

    public String getWeatherData(int location) {
        Log.d(TAG, "get Temperature for: " + location);
        HttpURLConnection con = null ;
        InputStream is = null;
        String url = BASE_URL_ZIP + location+",us";
        try {
            con = (HttpURLConnection) ( new URL(url)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // Let's read the response
            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while (  (line = br.readLine()) != null )
                buffer.append(line + "\r\n");

            is.close();
            con.disconnect();
            return buffer.toString();
        }
        catch(Throwable t) {
            t.printStackTrace();
            Log.d(TAG, "issue");
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }

        return null;

    }
    public String getWeatherData(String town, String state) {
        Log.d(TAG, "get Temperature for: " + town + ", " + state);
        HttpURLConnection con = null ;
        InputStream is = null;
        String url = BASE_TOWN_STATE + town + "," + state + ",us";
        try {
            con = (HttpURLConnection) ( new URL(url)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // Let's read the response
            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while (  (line = br.readLine()) != null )
                buffer.append(line + "\r\n");

            is.close();
            con.disconnect();
            return buffer.toString();
        }
        catch(Throwable t) {
            t.printStackTrace();
            Log.d(TAG, "issue");
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }

        return null;

    }


}