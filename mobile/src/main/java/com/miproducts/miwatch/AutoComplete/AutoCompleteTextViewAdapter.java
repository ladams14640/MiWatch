package com.miproducts.miwatch.AutoComplete;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import com.miproducts.miwatch.AddWeatherLocation;
import com.miproducts.miwatch.R;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;
import java.util.logging.LogRecord;
import java.util.zip.Inflater;

/**
 * Created by ladam_000 on 8/10/2015.
 */
public class AutoCompleteTextViewAdapter<T> extends ArrayAdapter<String> {
    // the List of String arrays that we have stored the parsed CSV in.
    private List<String[]> parsedCSV;
    /* Context that this Adapter is being generated from. */
    private AddWeatherLocation mAddWeatherLocation;
    /* Resource to inflate*/
    private int mResource;
    /* The inflator*/
    private LayoutInflater mInflater;

    public AutoCompleteTextViewAdapter(Context context, int resource, List<String[]> objects) {
        super(context, resource);

        parsedCSV = objects;
        mAddWeatherLocation = (AddWeatherLocation) context;
        mResource = resource;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return parsedCSV.size();
    }

    //TODO getItem() ????


    @Override
    public String getItem(int position) {
        return parsedCSV.get(position)[2];
    }







    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = mInflater.inflate(mResource, null);

        TextView tvTown = (TextView) row.findViewById(R.id.tvTown);
        TextView tvState = (TextView)row.findViewById(R.id.tvState);
        TextView tvZipcode = (TextView)row.findViewById(R.id.tvZipCodelv);

        // - zipcode = 0
        // - Town = 2
        // - State = 3
        tvTown.setText(parsedCSV.get(position)[2]);
        tvState.setText(parsedCSV.get(position)[3]);
        tvZipcode.setText(parsedCSV.get(position)[0]);


        return row;
    }


    private class AutoCompleteFilter implements Filter {

        public AutoCompleteFilter(){

        }




        @Override
        public boolean isLoggable(LogRecord record) {
            return false;
        }


    }



}
