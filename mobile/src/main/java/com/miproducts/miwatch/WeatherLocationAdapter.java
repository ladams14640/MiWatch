package com.miproducts.miwatch;

import android.content.Context;
import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.miproducts.miwatch.Container.WeatherLocation;

import java.sql.Time;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    private void addLocation(WeatherLocation newLocation){
        mLocations.add(newLocation);
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
        // we will do logic here to pick the image
        TextView tvDesc = (TextView) rowView.findViewById(R.id.tvDesc);
        TextView tvTime = (TextView) rowView.findViewById(R.id.tvTimeStamp);

        tvZipcode.setText(mLocations.get(position).getZipcode());
        tvCity.setText(mLocations.get(position).getCity());
        tvTemp.setText(String.valueOf(mLocations.get(position).getTemperature()));
        tvDesc.setText(mLocations.get(position).getDesc());
        tvTime.setText(TimeUnit.MILLISECONDS.toHours(mLocations.get(position).getTime_stamp()) + " : " + TimeUnit.MILLISECONDS.toMinutes(mLocations.get(position).getTime_stamp()));
        return rowView;
    }
}
