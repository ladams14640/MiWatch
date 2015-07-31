package com.miproducts.miwatch.config;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;
import com.miproducts.miwatch.MiDigitalWatchFace;
import com.miproducts.miwatch.R;
import com.miproducts.miwatch.utilities.SettingsManager;

/**
 * Class the user sees when he clicks on the configuration clock on his WatchFace Choice Screen.
 * This will setup his color for his watchface.
 * Created by ladam_000 on 7/19/2015.
 */
public class MiDigitalWatchFaceConfiguration extends Activity{
    private static final String TAG = "DigitalWatchFaceConfig";
    private WatchFaceSurfaceViewConfig svView;
    private GoogleApiClient mGoogleApiClient;
    private SettingsManager settingsManager;

    public MiDigitalWatchFaceConfiguration() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.digital_config_activity);
        svView = new WatchFaceSurfaceViewConfig(getApplicationContext());
        svView = (WatchFaceSurfaceViewConfig) findViewById(R.id.surfaceView);

        settingsManager = new SettingsManager(getApplicationContext());




        // we want to remove the hud - it prevents user from switching up watchfaces
    }

    // Handle the surfaceview's thread
    @Override
    protected void onStart() {
        updateConfigHudRemove();
        svView.threadRun(true);
        super.onStart();
    }
    // Handle the surfaceview's thread
    @Override
    protected void onPause() {
        log("Paused");
        svView.threadRun(false);
        //TODO save the color choice here.
        settingsManager.setHudRemove(false);
        super.onPause();
    }



    private void updateConfigHudRemove(){
        log("updateConfigHudRemove");
        settingsManager.setHudRemove(true);

    }

    public void log(String s) {
        Log.d(TAG, s);
    }


}
