package com.miproducts.miwatch.Weather.openweather;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by larry on 7/13/15.
 */
public class JSONWeatherTask extends AsyncTask<String,Void,String>{
    WeatherHttpClient httpClient;

    public JSONWeatherTask(){
        httpClient = new WeatherHttpClient();
        Log.d(TAG, "Made");
    }

    private static final String TAG = "JSONWeatherTask";

    @Override
    protected String doInBackground(String... params) {
        Log.d(TAG, "BackGround");

        String data = sendHttpRequest();
        return data;
    }



    @Override
    protected void onPostExecute(String result) {
        Log.d(TAG, "onPostExecute");
    }
    private String sendHttpRequest() {
        String result = httpClient.getWeatherData("Biddeford");
        int indexInt = result.indexOf("temp");
        int begOfTempValue = indexInt+6;
        int endOfTempValue = result.indexOf(",", begOfTempValue);
        String temperatureInCelsius = result.substring(begOfTempValue, endOfTempValue);
        // temperature in Fahrenheit
        int tempInCels = (int) ConverterUtil.convertKelvinToCelsiusconvertKelvinToCelsius(Double.parseDouble(temperatureInCelsius));
        Log.d(TAG, "Celsius = "+ tempInCels);
        // THIS IS WHAT WE WANT
        int tempInFah = ConverterUtil.convertCelsiusToFahrenheit(tempInCels);
        Log.d(TAG, "Fahrenheit = " + tempInFah);

        return "temp_nothing";
    }

}

