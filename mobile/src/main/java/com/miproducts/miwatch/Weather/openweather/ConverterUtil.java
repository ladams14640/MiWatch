package com.miproducts.miwatch.Weather.openweather;import android.util.Log;/** * Created by Larry on 6/3/2015. * Taken from com.swarmnyc.watchface's tutorial */public class ConverterUtil{    public static final int    FAHRENHEIT        = 0;    public static final int    TIME_UNIT_12      = 0;    public static final int    TIME_UNIT_24      = 1;    public static double convertKelvinToCelsiusconvertKelvinToCelsius(double kelvin){        return kelvin - 273.15;    }    // converts to celsius    public static int convertFahrenheitToCelsius( int fahrenheit )    {        return ( ( fahrenheit - 32 ) * 5 / 9 );    }    // converts to fahrenheit    public static int convertCelsiusToFahrenheit( int celsius )    {        return ( ( celsius * 9 ) / 5 ) + 32;    }    public static String convertToMonth( int month )    {        switch ( month )        {            case 0:                return "January ";            case 1:                return "February ";            case 2:                return "March ";            case 3:                return "April ";            case 4:                return "May ";            case 5:                return "JUN";            case 6:                return "JUL";            case 7:                return "AUG";            case 8:                return "SEP";            case 9:                return "OCT";            case 10:                return "NOV";            default:                return "DEC";        }    }    public static String convertToMonthMin( int month ){    switch ( month )    {        case 0:            return "JAN ";        case 1:            return "FEB";        case 2:            return "MAR";        case 3:            return "APR";        case 4:            return "MAY";        case 5:            return "JUN";        case 6:            return "JUL";        case 7:            return "AUG";        case 8:            return "SEP";        case 9:            return "OCT";        case 10:            return "NOB";        default:            return "DEC";    }}    public static int convertMonthToDigit( String month )    {        month = month.toUpperCase();        if(month.equals("JAN")){            return 0;        }        else if(month.equals("FEB")){            return 1;        }        else if(month.equals("MAR")){            return 2;        }        else if(month.equals("APR")){            return 3;        }        else if(month.equals("MAY")){            return 4;        }        else if(month.equals("JUN")){            return 5;        }        else if(month.equals("JUL")){            return 6;        }        else if(month.equals("AUG")){            return 7;        }        else if(month.equals("SEP")){            return 8;        }        else if(month.equals("OCT")){            return 9;        }        else if(month.equals("NOV")){            return 10;        }        else if(month.equals("DEC")){            return 11;        }        else{            Log.d("ConvertUtil", "convertMonthToDigit = wasnt passed proper stuff");            return 0;        }    }    public static String getDaySuffix( int monthDay )    {        switch ( monthDay )        {            case 1:                return "st";            case 2:                return "nd";            case 3:                return "rd";            default:                return "th";        }    }    public static int convertHour( int hour, int timeUnit )    {        if ( timeUnit == TIME_UNIT_12 )        {            int result = hour % 12;            return ( result == 0 ) ? 12 : result;        }        else        {            return hour;        }    }    public static String normalizeTime(String militaryTime){        String[] timeBrokenUp = militaryTime.split(":");        // 1 = HRS        // 2 = MINS        // 3 = SECONDS        // we only want hrs and mins and have hrs in normal time        String HR = timeBrokenUp[0];        String MIN = timeBrokenUp[1];        String AMorPM = "AM";        int valueOfHR = Integer.valueOf(HR);        // so we are deaing with military time        if(Integer.valueOf(HR) > 12){            valueOfHR = valueOfHR - 12;            AMorPM = "PM";        }        HR = String.valueOf(valueOfHR);        militaryTime = HR + ":" + MIN + " " + AMorPM;        return militaryTime;    }    public static String normalizeHour(int hour){        if(hour == 0){            hour = 12;        }        return Integer.toString(hour);    }    public static String normalizeMinute(int minute){        if(minute < 10){            return "0"+Integer.toString(minute);        }        return Integer.toString(minute);    }}