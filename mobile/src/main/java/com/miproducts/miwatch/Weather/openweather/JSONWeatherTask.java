package com.miproducts.miwatch.Weather.openweather;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.miproducts.miwatch.Database.WeatherLocationDbHelper;
import com.miproducts.miwatch.MiDigitalWatchFaceCompanionConfigActivity;
import com.miproducts.miwatch.utilities.Consts;
import com.miproducts.miwatch.utilities.SendToDataLayerThread;
import com.miproducts.miwatch.utilities.SettingsManager;
import com.miproducts.miwatch.Container.WeatherLocation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


//TODO check over the work when requested by the watch
//TODO update weather if we do currently have the zipcode in the database.

/**
 * Created by ladam_000 on 8/1/2015.
 */
public class JSONWeatherTask extends AsyncTask<String,Void,String> {
    // max length of a integer to check against
    private static final int MAX_INTEGER_LENGTH = 11;
    // error indicate it is not a proper zipcode
    private static final String ERROR_ZIP = "ZIP_ISSUE";
    // error indicate we are not online
    private static final String ERROR_ONLINE = "ONLINE_ISSUE";
    // error we use if we didnt get a result back from trying to reach out to the weather online.
    private static final int ERROR_URL = -2000;

    private WeatherHttpClient httpClient;
    private SettingsManager mSettingsManager;
    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private WeatherLocation locationToFill;
    private WeatherLocationDbHelper dbHelper;
    private boolean fromActivity = false;
    // keep track if we should change what the last Selected Zipcode is.
    private boolean changeSelected = true;
    // keep track if user is searching by town and state
    private boolean fromTownAndState = false;


    /**
     * From ConfigServiceListener
     * @param mContext
     * @param mSettingsManager
     * @param mGoogleApiClient
     */
    public JSONWeatherTask(Context mContext, SettingsManager mSettingsManager, GoogleApiClient mGoogleApiClient){
        Log.d(TAG, "Made");
        this.mSettingsManager = mSettingsManager;
        this.mContext = mContext;
        this.mGoogleApiClient = mGoogleApiClient;
        this.httpClient = new WeatherHttpClient();
        this.locationToFill = null;
        this.dbHelper = new WeatherLocationDbHelper(mContext);
        // indicate we are from a Service and not an Activity (our app is not present on the screen.)
        this.fromActivity = false;
    }

    /**
     * /**
     * The Weather Location indicates this came from the Activity not the ConfigListenerService and
     * that the Activity will eventually need it's UI updated - because it will have a new item in it's
     * listview.
     * @param mContext
     * @param mSettingsManager
     * @param mGoogleApiClient
     * @param locationToFill - WeatherLocation
     * @param changeSelected - Should we change what is saved as the default zipcode? true if we want to refresh and just clicked on an item in the ListView, false if we just onCreated and want to update all of the weathers.
     */
    public JSONWeatherTask(Context mContext, SettingsManager mSettingsManager,
                           GoogleApiClient mGoogleApiClient, WeatherLocation locationToFill,
                           boolean changeSelected){
        Log.d(TAG, "Made");
        this.mSettingsManager = mSettingsManager;
        this.mContext = mContext;
        this.mGoogleApiClient = mGoogleApiClient;
        this.locationToFill = locationToFill;
        this.dbHelper = new WeatherLocationDbHelper(mContext);
        this.httpClient = new WeatherHttpClient();

        // indicate we are from Activity not Service - our app is showing.
        this.fromActivity = true;

        this.changeSelected = changeSelected;

    }

    /**
     * /**
     * The Weather Location indicates this came fAddWeatherLocation.Activity not the ConfigListenerService and
     * that the Activity will eventually need it's UI updated - because it will have a new item in it's
     * listview.
     * @param mContext
     * @param mSettingsManager
     * @param mGoogleApiClient
     * @param locationToFill - WeatherLocation
     * @param changeSelected - Should we change what is saved as the default zipcode? true if we want to refresh and just clicked on an item in the ListView, false if we just onCreated and want to update all of the weathers.
     * @param fromTownAndState - this is if the user was searching with state and town instead of zipcode
     */
    //TODO really need to polish this boolean, we now have 3 booleans determining how we want to handle the search - not good.!
    public JSONWeatherTask(Context mContext, SettingsManager mSettingsManager,
                           GoogleApiClient mGoogleApiClient, WeatherLocation locationToFill,
                           boolean changeSelected, boolean fromTownAndState){
        Log.d(TAG, "Made");
        this.mSettingsManager = mSettingsManager;
        this.mContext = mContext;
        this.mGoogleApiClient = mGoogleApiClient;
        this.locationToFill = locationToFill;
        this.dbHelper = new WeatherLocationDbHelper(mContext);
        this.httpClient = new WeatherHttpClient();

        // indicate we are from Activity not Service - our app is showing. But this is only generated in addLocation so we are not fromActivity
        this.fromActivity = true;

        this.changeSelected = changeSelected;
        this.fromTownAndState = true;

    }

    private static final String TAG = "JSONWeatherTask";
    //mSettingsManager.saveZipcode(String.valueOf(etZipCode.getText().toString()));
    @Override
    protected String doInBackground(String... params) {
        Log.d(TAG, "BackGround");
        String zipcode = null;

        if(fromActivity){
            if(!fromTownAndState) {
                // fresh zipcode, not one from the preferences.
                zipcode = locationToFill.getZipcode();
            }
            // means we came by other means
            else {

            }
        }
        else {
            //TODO this is getting gross

                zipcode = mSettingsManager.getZipCode();
                locationToFill = new WeatherLocation();
                locationToFill.setZipcode(zipcode);

            }


        // store data
        String returnedData;

        // check if we are even online
        if(!isOnline())
            return ERROR_ONLINE;
        //TODO we need to clean this up
        // if we came from zipcode inputed

            log("not from town");
        if(!fromTownAndState){
            // check to see if this is the user's first time in the app
            if(!zipcode.equals(SettingsManager.NOTHING_SAVED))
            {
                // not a integer if it exceeds MAX_INTER_LENGTH
                if(zipcode.length() >= MAX_INTEGER_LENGTH)
                    return ERROR_ZIP;
                returnedData = String.valueOf(sendHttpRequest(Integer.valueOf(zipcode)));
            }
            else {
                Log.d(TAG, "NONE - must be first time was set so lets grab Biddeford.");
                // lets save biddos since first time
                mSettingsManager.saveZipcode("04005");
                returnedData = String.valueOf(sendHttpRequest(04005));
            }
            // onlt send out the temperature if the result back was not an error
            if(Integer.valueOf(returnedData) != (ERROR_URL)){
                // send to watch
                sendHandledTemperatureToWatch(returnedData);
            }else {
                return ERROR_ZIP;
            }

        }
        else {
         // we came from tow
            log("from town");
            returnedData = String.valueOf(sendHttpRequest(locationToFill));
        }

        return null;
    }



    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onPostExecute(String result) {
        // by default result should return a null - unless there was an issue.
        if(result != null){
            if(result.equals(ERROR_ZIP)){
                Toast.makeText(mContext, "Not proper zipcode!", Toast.LENGTH_LONG).show();

                return;
            }else if(result.equals(ERROR_ONLINE)){
                Toast.makeText(mContext, "Not online!", Toast.LENGTH_LONG).show();

            }

        }
        /* Can't guarantee we will have the MiDigitalWatchFaceCompanionConfigActivity for a Context.
        // If we came from a ConfigListener and we try to update the UI it will become a mess. -
        // todo this we would do something like - send a broadcaster as a callback out,
        // if ConfigActivity responds we can handle - but thats alot for little result and will prob pass
            on that for a bit.
        */
        if(fromActivity){
            // tell
        //TODO turned off because now i am from an activity but not from that activity -- got to fix.n t//o update it's UI
            //((MiDigitalWatchFaceCompanionConfigActivity)mContext).updateUI();
        }
        Log.d(TAG, "onPostExecute");
    }
    //TODO clean up
    private int sendHttpRequest(WeatherLocation locationToFill) {
        // get the JSON GEOLOCATION TEMP DETAILS
        String result = httpClient.getWeatherData(locationToFill.getCity(), locationToFill.getState());

        if(resultIsValid(result)){

            //TODO refactor and grab this with the JSON OBJECT -
            //TODO prob no reason to keep them split now. Because
            int tempInFah = parseTemp(result);

            try{


                //  json result
                JSONObject resultObject = new JSONObject(result);
                JSONArray jsonArrayWeather = resultObject.getJSONArray("weather");

                // description from JSONObject
                JSONObject description = jsonArrayWeather.getJSONObject(0);
                // get Name of town from JSONObject
                String town = resultObject.getString("name");

                //TODO reformat here
                locationToFill.setZipcode();
                locationToFill.setCity(town);
                locationToFill.setTemperature(tempInFah);
                locationToFill.setDesc(description.getString("description").replace("proximity", ""));
                locationToFill.setTime_stamp(System.currentTimeMillis());

                log("weather pulled out " + description.getString("description"));

                //log("name of place with jsonArray = " + town);
            }catch(JSONException e){
                log("issue with json = " + e.getMessage());
            }


            // we want to return more than just a temp
            if(fromActivity){
                // make sure we don't already have a copy of this location - compare zipcodes.
                if(!dbHelper.doesLocationExist(locationToFill))
                    dbHelper.addLocation(locationToFill);// Store into the Database
                    // update the database because we already have this zipcode
                else {dbHelper.updateTemperatureAndTime(locationToFill);}
                // save the new zipcode.
                if(changeSelected)
                    mSettingsManager.saveZipcode(locationToFill.getZipcode());
                try{
                    //  json result
                    JSONObject resultObject = new JSONObject(result);
                    JSONArray jsonArrayWeather = resultObject.getJSONArray("weather");

                    // description from JSONObject
                    JSONObject description = jsonArrayWeather.getJSONObject(0);
                    // get Name of town from JSONObject
                    String town = resultObject.getString("name");

                    log("weather pulled out " + description.getString("description"));
                    log("name of place with jsonArray = " + town);
                }catch(JSONException e){
                    log("issue with json = " + e.getMessage());
                }
            }
            // we caem from Config
            else {
                // send out to the dataLayer
                DataMap dataMap = new DataMap();
                // going to continue using the broadcast KEY, it is unique after in DataApi.
                dataMap.putInt(Consts.KEY_BROADCAST_DEGREE, tempInFah);
                // send off to wearable - listener over there will be listening.
                new SendToDataLayerThread(Consts.PHONE_TO_WEARABLE_PATH, dataMap, mGoogleApiClient).start();

                // update the database because we already have this zipcode
                dbHelper.updateTemperatureAndTime(locationToFill);



                // send this to the MainCompanionActivity to send it off to wearable. -
                Intent sendDegreesIntent = new Intent(Consts.BROADCAST_DEGREE);
                sendDegreesIntent.putExtra(Consts.KEY_BROADCAST_DEGREE, tempInFah);
                mContext.sendBroadcast(sendDegreesIntent);
            }

            return tempInFah;
        }else {
            return ERROR_URL;
        }

    }

    private int sendHttpRequest(int zipcode) {

        // get the JSON GEOLOCATION TEMP DETAILS
        String result = httpClient.getWeatherData(zipcode);

        if(resultIsValid(result)){

            //TODO refactor and grab this with the JSON OBJECT -
            //TODO prob no reason to keep them split now. Because
            int tempInFah = parseTemp(result);

            try{


                //  json result
                JSONObject resultObject = new JSONObject(result);
                JSONArray jsonArrayWeather = resultObject.getJSONArray("weather");

                // description from JSONObject
                JSONObject description = jsonArrayWeather.getJSONObject(0);
                // get Name of town from JSONObject
                String town = resultObject.getString("name");

                //TODO reformat here
                locationToFill.setCity(town);
                locationToFill.setTemperature(tempInFah);
                locationToFill.setDesc(description.getString("description").replace("proximity", ""));
                locationToFill.setTime_stamp(System.currentTimeMillis());

                log("weather pulled out " + description.getString("description"));

                //log("name of place with jsonArray = " + town);
            }catch(JSONException e){
                log("issue with json = " + e.getMessage());
            }


            // we want to return more than just a temp
            if(fromActivity){
                // make sure we don't already have a copy of this location - compare zipcodes.
                if(!dbHelper.doesLocationExist(locationToFill))
                    dbHelper.addLocation(locationToFill);// Store into the Database
                // update the database because we already have this zipcode
                else {dbHelper.updateTemperatureAndTime(locationToFill);}
                // save the new zipcode.
                if(changeSelected)
                    mSettingsManager.saveZipcode(locationToFill.getZipcode());
                try{
                    //  json result
                    JSONObject resultObject = new JSONObject(result);
                    JSONArray jsonArrayWeather = resultObject.getJSONArray("weather");

                    // description from JSONObject
                    JSONObject description = jsonArrayWeather.getJSONObject(0);
                    // get Name of town from JSONObject
                    String town = resultObject.getString("name");

                    log("weather pulled out " + description.getString("description"));
                    log("name of place with jsonArray = " + town);
                }catch(JSONException e){
                    log("issue with json = " + e.getMessage());
                }
            }
            // we caem from Config
            else {
                // send out to the dataLayer
                DataMap dataMap = new DataMap();
                // going to continue using the broadcast KEY, it is unique after in DataApi.
                dataMap.putInt(Consts.KEY_BROADCAST_DEGREE, tempInFah);
                // send off to wearable - listener over there will be listening.
                new SendToDataLayerThread(Consts.PHONE_TO_WEARABLE_PATH, dataMap, mGoogleApiClient).start();

                // update the database because we already have this zipcode
                dbHelper.updateTemperatureAndTime(locationToFill);



                // send this to the MainCompanionActivity to send it off to wearable. -
                Intent sendDegreesIntent = new Intent(Consts.BROADCAST_DEGREE);
                sendDegreesIntent.putExtra(Consts.KEY_BROADCAST_DEGREE, tempInFah);
                mContext.sendBroadcast(sendDegreesIntent);
            }

            return tempInFah;
        }else {
            return ERROR_URL;
        }

    }

    private boolean resultIsValid(String result) {
        if(result == null)
            return false;
        log("check validity " + !result.contains("Not found city"));
        //TODO check validity
        // we will get not found city if this was not a accurate zipcode.
        return !result.contains("Not found city");

    }

    private void log(String y) {
        Log.d(TAG, y);
    }


    private int parseTemp(String result) {
        int indexInt = result.indexOf("temp");
        int begOfTempValue = indexInt+6;
        int endOfTempValue = result.indexOf(",", begOfTempValue);
        // Kelvin version
        String temperatureInCelsius = result.substring(begOfTempValue, endOfTempValue);
        // Celsius version
        int tempInCels = (int) ConverterUtil.convertKelvinToCelsius(Double.parseDouble(temperatureInCelsius));
        // Fernheit
        int tempInFah = ConverterUtil.convertCelsiusToFahrenheit(tempInCels);

        return tempInFah;

    }

    private String parseCity(String result) {

        int indexInt = result.indexOf("name") + 1; // +1 to avoid the "
        int begOfCityValue = indexInt + 6;
        int endOfCityValue = result.indexOf(",", begOfCityValue);
        String city = result.substring(begOfCityValue, endOfCityValue-1); // -1 to avoid "
        return city;
    }


    private void sendHandledTemperatureToWatch(String temp) {
        // send out to the dataLayer
        DataMap dataMap = new DataMap();
        // going to continue using the broadcast KEY, it is unique after in DataApi.
        dataMap.putInt(Consts.KEY_BROADCAST_DEGREE, Integer.valueOf(temp));
        // send off to wearable - listener over there will be listening.
        new SendToDataLayerThread(Consts.PHONE_TO_WEARABLE_PATH, dataMap, mGoogleApiClient).start();
    }

}