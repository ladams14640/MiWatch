package com.miproducts.miwatch.utilities;

import android.provider.CalendarContract;

/**
 * Created by Larry on 6/3/2015.
 */
public class Consts {


    public static final char DEGREE_SIGN = 0x00B0;

    // Heuristic
    public final static int canvasWidth = 600;
    public final static int canvasHeight = 600;

    // TIME
    public static final int xTimePosition = 365;
    public static final int yTimePosition = 142;

    // EVENT
    public static final int xEventPosition = 57;
    public static final int yEventPosition = 192;

    // DateViews
    public static final int xDatePosition = 100;


    /** For selection purposes */
    public static final int NONE = 0;
    public static final int DIGITAL_TIMER = 1;
    public static final int EVENT = 2;
    public static final int FITNESS = 3;
    public static final int DEGREE = 4;
    public static final int DATE = 5;
    public static final int ALARM_TIMER = 6;


    /**
     * Use these as keys to track position and color of each view to transfer to wearable
     * KEYS for serializing data
     *
     */
    public static final String DIGITAL_TIMER_POS_API = "DIGITAL_TIME"; // x,y array of the digital time
    public static final String DIGITAL_TIMER_COLOR_API = "DIGITAL_TIME_API_COLOR"; // color of time
    public static final String DIGITAL_SIZE_API = "DIGITAL_SIZE_API"; // size of time
    public static final String DIGITAL_VISIBLE_API = "DIGITAL_VISIBLE_API"; // visibility of time

    //TODO finish these 4 constants across the rest of the package Items.
    public static final String EVENT_API = "EVENT_API";
    public static final String EVENT_COLOR_API = "EVENT_API_COLOR";



    public static final String FITNESS_API = "FITNESS_API";
    public static final String FITNESS_API_COLOR = "FITNESS_API_COLOR";

    public static final String DEGREE_API_COLOR = "DEGREE_API_COLOR";
    public static final String DEGREE_API = "DEGREE_API";

    public static final String DATE_API = "DATE_API";
    public static final String DATE_API_COLOR = "DATE_API_COLOR";

    public static final String ALARM_API_COLOR = "ALARM_API_COLOR";
    public static final String ALARM_API = "ALARM_API";



  //  public static final String
  //  public static final String
  //  public static final String
 //   public static final String


}