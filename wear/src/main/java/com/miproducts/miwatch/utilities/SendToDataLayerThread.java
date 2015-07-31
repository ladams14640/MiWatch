package com.miproducts.miwatch.utilities;

import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.concurrent.TimeUnit;

/**
 * Created by larry on 7/31/15.
 */
class SendToDataLayerThread extends Thread {
    private final static String TAG = "SendToDataLayerThread";
    String path;
    DataMap dataMap;
    private GoogleApiClient mGoogleApiClient;

    // Constructor for sending data objects to the data layer
    SendToDataLayerThread(String p, DataMap data, GoogleApiClient mGoogleApiClient) {
        path = p;
        dataMap = data;
        this.mGoogleApiClient = mGoogleApiClient;

    }

    public void run() {
        if (mGoogleApiClient != null) {
            log("mGoogle != null!");
            mGoogleApiClient.blockingConnect(5, TimeUnit.SECONDS);

            NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
            for (Node node : nodes.getNodes()) {

                // Construct a DataRequest and send over the data layer
                PutDataMapRequest putDMR = PutDataMapRequest.create(path);
                putDMR.getDataMap().putAll(dataMap);
                PutDataRequest request = putDMR.asPutDataRequest();
                DataApi.DataItemResult result = Wearable.DataApi.putDataItem(mGoogleApiClient, request).await();
                if (result.getStatus().isSuccess()) {
                    Log.d(TAG, "DataMap: " + dataMap + " sent to: " + node.getDisplayName());
                } else {
                    // Log an error
                    Log.d(TAG, "ERROR: failed to send DataMap");
                }
            }
        }else {
            log("mGoogle == null!");
        }
    }
    private void log(String s){Log.d(TAG, s);}
}
