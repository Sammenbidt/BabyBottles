package com.egocentric.babybottles.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.egocentric.babybottles.R;
import com.egocentric.babybottles.data.Bottle;

import java.util.ArrayList;
import java.util.List;

public class BottlesAdapter extends ArrayAdapter<Bottle> {

    private List<Bottle> data;
    private int layoutResourceId;
    private Context context;
    public BottlesAdapter(Context context, int layoutResourceId, ArrayList<Bottle> data)
    {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;

    }

    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View view = convertView;
        final Bottle bottle = getItem(position);
        if(view == null)
        {
            LayoutInflater inflater = ( (Activity) context).getLayoutInflater();
            view = inflater.inflate(layoutResourceId, parent, false);
        }

        view.setTag(bottle);

        TextView timeView = view.findViewById(R.id.bottle_adapter_time);
        TextView amountView = view.findViewById(R.id.diaper_adapter_time);

        timeView.setText( String.format("%d:%02d", bottle.start.getHour(), bottle.start.getMinute()));
        amountView.setText(String.format("%d", bottle.amount));

        TextView endTimeView = view.findViewById(R.id.bottle_adapter_end_time);
        endTimeView.setText(String.format("%d:%02d", bottle.end.getHour(), bottle.end.getMinute()));

        return view;
    }


}
