package com.miproducts.miwatch.config;

import android.graphics.Color;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.miproducts.miwatch.utilities.Consts;
import com.miproducts.miwatch.utilities.DigitalWatchFaceUtil;

/**
 * MiDigitalUtil is a class we are using to send data from MiDigitalWatchFaceConfiguration
 * to MiDigitalWatchFace.
 * Created by ladam_000 on 7/30/2015.
 */
public class MiDigitalUtil {
    private static final String TAG = "MiDigitalUtil";

    /**
     * The path for the {@link DataItem} containing {@link com.miproducts.miwatch.MiDigitalWatchFace} configuration.
     */
    public static final String PATH_WITH_FEATURE = "/wearable_to_wearable";
    /**
     * The {@link DataMap} key for {@link com.miproducts.miwatch.MiDigitalWatchFace} main color that can be changed.
     */
    public static final String KEY_MAIN_COLOR = "MAIN_COLOR";
    /**
     * The {@link DataMap} key for {@link com.miproducts.miwatch.MiDigitalWatchFace} default color value to KEY_MAIN_COLOR.
     */
    public static final String DEFAULT_COLOR = "digital_time_blue";

    // let MiDIgitalWatchface know that we do not need the hud up, because it will block touches.
    //TODO make sure we reset when we are back tho.
    /**
     * The {@link DataMap} key for {@link com.miproducts.miwatch.MiDigitalWatchFace} removal of hud.
     */
    public static final String KEY_HUD_ACTION = "HUD_ACTION";
    /**
     * The {@link DataMap} key for {@link com.miproducts.miwatch.MiDigitalWatchFace} standard to go with KEY_HUD_ACTION;
     */
    public static final String HUD_REMOVE = "HUD_REMOVE";


    private static void log(String s){
        Log.d(TAG, s);
    }



    /**
     * Overwrites the current config {@link DataItem}'s {@link DataMap} with {@code newConfig}.
     * If the config DataItem doesn't exist, it's created.
     */
    public static void putConfigDataItem(GoogleApiClient googleApiClient, DataMap newConfig) {
        PutDataMapRequest putDMR = PutDataMapRequest.create(PATH_WITH_FEATURE);
        log("putConfigDataItem");
        DataMap configToPut = putDMR.getDataMap();
        configToPut.putAll(newConfig);
        //new SendToDataLayerThread(PATH_WITH_FEATURE, configToPut, googleApiClient).start();

        Wearable.DataApi.putDataItem(googleApiClient, putDMR.asPutDataRequest())
                .setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                    @Override
                    public void onResult(DataApi.DataItemResult dataItemResult) {
                        if (Log.isLoggable(TAG, Log.DEBUG)) {
                            Log.d(TAG, "putDataItem result status: " + dataItemResult.getStatus());
                        }
                    }
                });
    }

    static class SendToDataLayerThread extends Thread {
        String path;
        DataMap dataMap;
        String testmsg = "degree";
        private GoogleApiClient mGoogleApiClient;

        // Constructor for sending data objects to the data layer
        SendToDataLayerThread(String p, DataMap data, GoogleApiClient mGoogleApiClient) {
            log("thread kickOff");
            path = p;
            dataMap = data;
            this.mGoogleApiClient = mGoogleApiClient;
        }

        public void run() {
            // Construct a DataRequest and send over the data layer
            PutDataMapRequest putDMR = PutDataMapRequest.create(path);
            putDMR.getDataMap().putAll(dataMap);
            PutDataRequest request = putDMR.asPutDataRequest();
            Wearable.DataApi.putDataItem(mGoogleApiClient, request);
        }
    }

}
