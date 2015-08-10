package com.miproducts.miwatch;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;
import com.miproducts.miwatch.Container.WeatherLocation;
import com.miproducts.miwatch.Weather.openweather.JSONWeatherTask;
import com.miproducts.miwatch.utilities.CSVReader;
import com.miproducts.miwatch.utilities.SettingsManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by larry on 8/10/15.
 */
public class AddWeatherLocation extends Activity{

    private static final String TAG = "AddWeatherLocation";
    /* Link to the View objects */
    private ImageButton ibBack;
    private ImageButton ibCurrentLocation;
    private EditText etSearchPlaces;
    private SettingsManager mSettingsManager;
    private GoogleApiClient mGoogleApiClient;
    List<String[]> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_weather_location);

        mSettingsManager = new SettingsManager(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();

        initViewObjects();
        parseCityAndStateCSV();
    }

    private void parseCityAndStateCSV() {
        String next[] = {};
        list = new ArrayList<String[]>();

        try {
            CSVReader reader = new CSVReader(new InputStreamReader(getAssets().open("citiesstateszipcodes.csv")));
            while(true) {
                next = reader.readNext();
                if(next != null) {

                    list.add(next);
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < list.size(); i++){
            Log.d(TAG, list.get(i)[2].toString());
        }
    }

    /* Initialize View Objects */
    private void initViewObjects() {
        ibBack = (ImageButton) findViewById(R.id.ibBack);
        ibCurrentLocation = (ImageButton) findViewById(R.id.ibCurrentLocation);
        etSearchPlaces = (EditText) findViewById(R.id.etSearchPlaces);
        attachClickHandlers();
    }
    /* Attach Handlers to View Objects */
    private void attachClickHandlers() {

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // end This Activity
                finish();
            }
        });

        ibCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 1. current geolocatio to get weather stuff.
                // TODO 2. add it to our db.
                //TODO 3. when done go back to last activity.

                WeatherLocation weatherLocation = new WeatherLocation();
                weatherLocation.setZipcode(etSearchPlaces.getText().toString());
                JSONWeatherTask task = new JSONWeatherTask(getApplicationContext(), mSettingsManager, mGoogleApiClient);
                task.execute();
            }
        });

        etSearchPlaces.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.d(TAG, "onKey");
                //TODO this is probably too much work and will kill app!
                // dont want to fetch it if we are doing something else here.
                if (event.getAction() != KeyEvent.KEYCODE_DEL || event.getAction() != KeyEvent.KEYCODE_ENTER) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i)[2].contains(etSearchPlaces.getText().toString())){
                            etSearchPlaces.setText(list.get(i)[2].toString() + ", " + list.get(i)[1]);
                        }
                    }
                }

                return false;
            }
        });

    }


}
