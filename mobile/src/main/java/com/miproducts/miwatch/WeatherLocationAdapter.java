package com.miproducts.miwatch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.miproducts.miwatch.Container.WeatherLocation;

import java.util.List;

/**
 * Adapter for the list view
 * Created by ladam_000 on 8/1/2015.
 */
public class WeatherLocationAdapter extends BaseAdapter{
    private List <WeatherLocation> mLocations;
    private Context mContext;
    private int rowForLocationToInflate;
    private LayoutInflater inflater;

    public WeatherLocationAdapter(List<WeatherLocation> mLocations, Context mContext, int rowForLocationToInflate) {
        this.mLocations = mLocations;
        this.mContext = mContext;
        this.rowForLocationToInflate = rowForLocationToInflate;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    //TODO just built up layout now must tie to it.


    private void addLocation(WeatherLocation newLocation){
        mLocations.add(newLocation);
        //TODO maybe invalidate after adding new item.
    }

    @Override
    public int getCount() {
        return mLocations.size();
    }

    @Override
    public WeatherLocation getItem(int position) {
        return mLocations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //TODO build a viewholder

        View rowView = inflater.inflate(rowForLocationToInflate, parent, false);

        TextView tvZipcode = (TextView) rowView.findViewById(R.id.tvZipCode);
        TextView tvCity = (TextView) rowView.findViewById(R.id.tvCity);
        TextView tvTemp = (TextView) rowView.findViewById(R.id.tvDegree);

        tvZipcode.setText(mLocations.get(position).getZipcode());
        tvCity.setText(mLocations.get(position).getCity());
        tvTemp.setText(String.valueOf(mLocations.get(position).getTemperature()));

        return rowView;
    }
}
