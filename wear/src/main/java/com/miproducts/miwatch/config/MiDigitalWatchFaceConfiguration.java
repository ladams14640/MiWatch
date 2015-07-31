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

/**
 * Class the user sees when he clicks on the configuration clock on his WatchFace Choice Screen.
 * This will setup his color for his watchface.
 * Created by ladam_000 on 7/19/2015.
 */
public class MiDigitalWatchFaceConfiguration extends Activity{
    private static final String TAG = "DigitalWatchFaceConfig";
    private WatchFaceSurfaceViewConfig svView;
    private GoogleApiClient mGoogleApiClient;


    public MiDigitalWatchFaceConfiguration() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.digital_config_activity);
        svView = new WatchFaceSurfaceViewConfig(getApplicationContext());
        svView = (WatchFaceSurfaceViewConfig) findViewById(R.id.surfaceView);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        if (Log.isLoggable(TAG, Log.DEBUG)) {
                            Log.d(TAG, "onConnected: " + connectionHint);
                        }
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                        if (Log.isLoggable(TAG, Log.DEBUG)) {
                            Log.d(TAG, "onConnectionSuspended: " + cause);
                        }
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        if (Log.isLoggable(TAG, Log.DEBUG)) {
                            Log.d(TAG, "onConnectionFailed: " + result);
                        }
                    }
                })
                .addApi(Wearable.API)
                .build();
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



    private void updateConfigHudRemove(){
        DataMap configKeysToOverwrite = new DataMap();
        configKeysToOverwrite.putString(MiDigitalUtil.KEY_HUD_ACTION, MiDigitalUtil.HUD_REMOVE);
        MiDigitalUtil.putConfigDataItem(mGoogleApiClient, configKeysToOverwrite);
    }

    // update the main color
    private void updateConfigForMainColor(final int mainColor) {
        DataMap configKeysToOverwrite = new DataMap();
        configKeysToOverwrite.putInt(MiDigitalUtil.KEY_MAIN_COLOR,
                mainColor);
        MiDigitalUtil.putConfigDataItem(mGoogleApiClient, configKeysToOverwrite);
    }

}
