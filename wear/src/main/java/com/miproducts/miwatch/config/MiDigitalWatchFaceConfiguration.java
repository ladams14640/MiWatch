package com.miproducts.miwatch.config;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;
import com.miproducts.miwatch.R;

/**
 * Created by ladam_000 on 7/19/2015.
 */
public class MiDigitalWatchFaceConfiguration extends Activity{
    private static final String TAG = "DigitalWatchFaceConfig";
    private WatchFaceSurfaceViewConfig svView;
    public MiDigitalWatchFaceConfiguration() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.digital_config_activity);
        svView = new WatchFaceSurfaceViewConfig(getApplicationContext());
        svView = (WatchFaceSurfaceViewConfig) findViewById(R.id.surfaceView);
    }

    // Handle the surfaceview's thread
    @Override
    protected void onStart() {
        svView.threadRun(true);
        super.onStart();
    }
    // Handle the surfaceview's thread
    @Override
    protected void onPause() {
        svView.threadRun(false);
        super.onPause();
    }

}
