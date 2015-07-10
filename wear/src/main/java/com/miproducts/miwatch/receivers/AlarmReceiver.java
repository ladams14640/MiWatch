package com.miproducts.miwatch.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.miproducts.miwatch.utilities.TimerFormat;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ladam_000 on 6/20/2015.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmReceiver", "onReceiver Called");
        Long timeLeft = intent.getLongExtra("MiWatch", 0);
        Log.d("Alarm WatchFace", "yeah baby: " + timeLeft);

        Calendar cal;
        cal = Calendar.getInstance();
        Calendar calTimer = Calendar.getInstance();
        calTimer.setTime(new Date(timeLeft));
        long alarmTime =  calTimer.getTimeInMillis() - cal.getTimeInMillis();
        alarmTime = Math.abs(alarmTime);
        Log.d("AlarmReceiver", "TEST: "  + TimerFormat.getTimeString(alarmTime));
    }
}
