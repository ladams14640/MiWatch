package com.miproducts.miwatch.utilities;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.wearable.provider.WearableCalendarContract;
import android.support.wearable.watchface.WatchFaceService;
import android.text.format.DateUtils;
import android.util.Log;

import com.miproducts.miwatch.MiDigitalWatchFace;
import com.miproducts.miwatch.container.Event;
import com.miproducts.miwatch.hud.HudView;
import com.miproducts.miwatch.mods.EventMod;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by larry on 6/30/15.
 */
public class LoadMeetingTask extends AsyncTask<Void, Void, Integer> {
    final static String TAG = "LoadMeetingsTask";



    Boolean isNewEventRequired = true;
    Context mContext;
    HudView mHudView;
    // keep track of cursor count from the previous run through of Calendar events
    int cursorCount = 9999;
    List<Event> mEvents = new ArrayList<Event>();
    int mLastEventIndex = 0;
    EventMod mEventMod;
    private boolean isRunning = false;
public boolean isRunning(){
    return isRunning;
}
    public LoadMeetingTask(Context context,EventMod mCalendarMod, HudView mHudView){
        mContext = context;
        this.mHudView = mHudView;
        this.mEventMod = mCalendarMod;
    }


    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            log("doInBackground");
            isRunning = true;
            if (isCancelled()) cancel(true);
            long begin = System.currentTimeMillis();
            Uri.Builder builder =
                    WearableCalendarContract.Instances.CONTENT_URI.buildUpon();

            ContentUris.appendId(builder, begin);
            ContentUris.appendId(builder, begin + DateUtils.DAY_IN_MILLIS);

            final Cursor cursor =
                    mContext.getContentResolver()
                            .query(builder.build(),
                                    Consts.PROJECTION, null, null, null);
            if (isCancelled()) cancel(true);
            int numMeetings = cursor.getCount();

            // if cursor has more than we already have cocked
            // or if events is lesser than cursor's size, than add

            // -  I NEED A BETTER CHECK HERE


            String cursorFirstDescription;
            String eventsFirstDescription;
            String cursorLastDescription;
            String eventsLastDescription;
            if (isCancelled()) cancel(true);
            // check and see if the last time we ran through we had a count
            log("cursorCount = " + cursorCount);
            log("cursor.getCount = " + numMeetings);
            if (mEventMod.getEventsSize() != numMeetings) {
                log("cursors dont equate");
                isNewEventRequired = true;
                mLastEventIndex = 0;
                cursorCount = cursor.getCount();
            } else {
                isNewEventRequired = false;
                log("cursors equate");

            }
            if (isCancelled()) cancel(true);
            // we will be making a new event list for display
            if (isNewEventRequired) {
                log("ultimately isNewEventRequired was true");
                mEvents = new ArrayList<Event>();

                while (cursor.moveToNext()) {
                    Log.d("YAEH", "INDEX: " + cursor.getString(Consts.PROJECTION_ID_INDEX));
                    Log.d(TAG, "TITLE: " + cursor.getString(Consts.PROJECTION_TITLE));
                    Log.d(TAG, "DESCRI: " + cursor.getString(Consts.PROJECTION_DESCRIPTION));

                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(cursor.getLong(Consts.PROJECTION_START));
                    // testing
                    Log.d(TAG, "Day of Week: " + cal.get(Calendar.DAY_OF_WEEK));
                    Log.d(TAG, "Hour of day: " + cal.get(Calendar.HOUR_OF_DAY));
                    Log.d(TAG, "Day of Month: " + cal.get(Calendar.DAY_OF_MONTH));
                    log("Month" + cal.get(Calendar.MONTH));

                    Log.d(TAG, "Everything: " + cal.getTime());
                    Log.d(TAG, "endTime:" + cursor.getString(Consts.PROJECTION_END));
                    final String[] calendarStuff = String.valueOf(cal.getTime()).split(" ");
                    // calendar[0] = Wed
                    // calendar[1] = Dec
                    // Calendar[2] == 31
                    // Calendar[3] == military time
                    // Calendar[4] == EST
                    // calendar[5] == 2015
                    if (isCancelled()) cancel(true);
                    // LAST CHECK BEFORE ADDING TO OUR EVENTS
                    // get current time
                    Calendar c = Calendar.getInstance();
                    int currentDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
                    int currentMonth = c.get(Calendar.MONTH);
                    log("current month = " + currentMonth);
                    log("current day = " + currentDayOfMonth);
                    log("dayOfMonth = " + currentDayOfMonth);
                    log("month of year = " + currentMonth);
                    // make sure the cursor date == or greater than current month
                    //  if(ConverterUtil.convertMonthToDigit(calendarStuff[1].toUpperCase()) >= currentMonth){
                    int cursorDayOfMonth = Integer.valueOf(calendarStuff[2]);
                    log("cursor's day " + cursorDayOfMonth);
                    // current date less than or equal to the cursor date add it
                    //if(cursorDayOfMonth >= currentDayOfMonth ){
                    // lets add the cursor's event to our event
                    Event newEvent = new Event();
                    newEvent.dayOfMonth = calendarStuff[2];
                    newEvent.dayOfWeek = calendarStuff[0];
                    newEvent.time = calendarStuff[3];
                    newEvent.title = cursor.getString(Consts.PROJECTION_TITLE);
                    newEvent.desc = cursor.getString(Consts.PROJECTION_DESCRIPTION);
                    newEvent.endTime = cursor.getString(Consts.PROJECTION_END);
                    newEvent.month = calendarStuff[1].toUpperCase();
                    log("saving new Event");


                    mEvents.add(newEvent);
                    // ALL DAY EVENTS WONT WORK HERE - I WONDER WHAT I GET BACK FROM THE CURSOR FOR THAT
                    // PROJECTION
                   /* }else {
                        log("cursor day of month is less than current day of month" + "cursor's day of month = " + cursor.getString(Consts.PROJECTION_TITLE));
                    }
                }else {
                    // skip the date adding
                    log("the cursor date is on a date before this month = " + cursor.getString(Consts.PROJECTION_TITLE));
                }*/


                }

                mHudView.addEvents(mEvents);
            } else {
                log("ultimately isNewEventRequired was false");

            }



            cursor.close();

        }catch(Exception e){
            log("exception "+ e.getMessage().toString());
        }
            return 0;

    }

    @Override
    protected void onPostExecute(Integer result) {
        log("onPostExecute");
        onMeetingsLoaded(result);

    }
    private void onMeetingsLoaded(Integer result) {
            isRunning = false;
        if (isNewEventRequired == true) {
            mEventMod.setEventIndex(0);
            mEventMod.setNextEvent(0);
        }
    }

    private void log(String s){
        Log.d("LoadMeetingTask", s);
    }
}
