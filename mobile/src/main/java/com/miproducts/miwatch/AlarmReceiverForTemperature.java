package com.miproducts.miwatch;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.miproducts.miwatch.Weather.openweather.ConverterUtil;
import com.miproducts.miwatch.Weather.openweather.WeatherHttpClient;
import com.miproducts.miwatch.utilities.Consts;

import java.util.Calendar;

/**
 * We will fetch degrees and post them every 30 minutes, or we will not. if the user hits back on the
 * Companion Activity then we will stop fetching temperature every 30. If the user hits Done then we will
 * begin to fetch weather every 30 minutes. So you can start weather fetching for every 30 by hitting done
 * in the companion app and you can turn it off by backing out of the app (press BACK not HOME!!)
 * that way we can control when we fetch weather and not.
 * Created by ladam_000 on 7/19/2015.
 */
public class AlarmReceiverForTemperature extends BroadcastReceiver{

    private GoogleApiClient mGoogleApi;
    private Context mContext;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmReceiverForT", "Cycle through");

        mContext = context;
        mGoogleApi = new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();

        // if so then we repeat
        boolean repeat = intent.getBooleanExtra(Consts.KEY_ALARM_REPEAT, false);

        setRepeatAlarm(repeat);

        getTemp();

    }

    private void setRepeatAlarm(boolean repeat) {
            // lets setup the alarm to run and post the degrees
            Intent intent = new Intent(mContext, AlarmReceiverForTemperature.class);

            intent.putExtra(Consts.KEY_ALARM_REPEAT, repeat);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);

            Calendar instance = Calendar.getInstance();
            instance.add(Calendar.SECOND, 30);

            alarmManager.set(AlarmManager.RTC_WAKEUP, instance.getTimeInMillis(), pendingIntent);
            if(!repeat){
                Log.d("received Alarm", "cancel temperature alarm fetch");
                alarmManager.cancel(pendingIntent);
            }else {
                Log.d("received Alarm", "keep temperature alarm fetch");

            }
            //Toast.makeText(mContext, "received Alarm", Toast.LENGTH_LONG).show();
           // Log.i("DISPLAY ALL", "ALARM SET UP");

    }

    private void getTemp() {
        JSONWeatherTask task = new JSONWeatherTask();
        task.execute();
    }


    int tempInFah;


    public class JSONWeatherTask extends AsyncTask<String,Void,String> {
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
            Toast.makeText(mContext, "Fahrenheit = " + tempInFah, Toast.LENGTH_SHORT).show();


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

            // TODO atm this sends the temperature results to the MainCompanionActivity - we want to
            //TODO change this so we can do it regardless if application on phone is on.
            tempInFah = ConverterUtil.convertCelsiusToFahrenheit(tempInCels);
            Log.d(TAG, "Fahrenheit = " + tempInFah);

            /**/
            // WE ARE DOING 2 THINGS:
            //  1. sending to datalayer
            //  2. broadcasting to CompanionConfigActivity
            // send out to the dataLayer
            DataMap dataMap = new DataMap();
            // going to continue using the broadcast KEY, it is unique after in DataApi.
            dataMap.putInt(Consts.KEY_BROADCAST_DEGREE, tempInFah);
            // send off to wearable - listener over there will be listening.
            new SendToDataLayerThread(Consts.PHONE_TO_WEARABLE_PATH, dataMap).start();


            // send this to the MainCompanionActivity to send it off to wearable. -
            Intent sendDegreesIntent = new Intent(Consts.BROADCAST_DEGREE);
            sendDegreesIntent.putExtra(Consts.KEY_BROADCAST_DEGREE, tempInFah);
            mContext.sendBroadcast(sendDegreesIntent);




            return "temp_nothing";
        }

    }



    class SendToDataLayerThread extends Thread {
        String path;
        DataMap dataMap;

        // Constructor for sending data objects to the data layer
        SendToDataLayerThread(String p, DataMap data) {
            path = p;
            dataMap = data;

        }

        public void run() {
            if (mGoogleApi != null) {
                Log.d("AlarmReceiverForT", "mGoogle != null!");

                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApi).await();
                for (Node node : nodes.getNodes()) {

                    // Construct a DataRequest and send over the data layer
                    PutDataMapRequest putDMR = PutDataMapRequest.create(path);
                    putDMR.getDataMap().putAll(dataMap);
                    PutDataRequest request = putDMR.asPutDataRequest();
                    DataApi.DataItemResult result = Wearable.DataApi.putDataItem(mGoogleApi, request).await();
                    if (result.getStatus().isSuccess()) {
                        Log.d("AlarmReceiverForT", "DataMap: " + dataMap + " sent to: " + node.getDisplayName());
                    } else {
                        // Log an error
                        Log.d("AlarmReceiverForT", "ERROR: failed to send DataMap");
                    }
                }
            }else {
                Log.d("AlarmReceiverForT", "mGoogle == null!");
            }
        }
    }

}
