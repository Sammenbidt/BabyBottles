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
import com.egocentric.babybottles.data.Diaper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DiapersAdapter extends ArrayAdapter<Diaper> {

    private List<Diaper> data;
    private int layoutResourceId;
    private Context context;

    public DiapersAdapter(Context context, int layoutResourceId, ArrayList<Diaper> data)
    {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;

    }

    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View view = convertView;
        //final Bottle bottle = getItem(position);
        final Diaper diaper = getItem(position);
        if(view == null)
        {
            LayoutInflater inflater = ( (Activity) context).getLayoutInflater();
            view = inflater.inflate(layoutResourceId, parent, false);
        }

        view.setTag(diaper);

        TextView timeView = view.findViewById(R.id.diaper_adapter_time);
        TextView commentView = view.findViewById(R.id.diaper_adapter_text);

        if(diaper != null)
        {
            timeView.setText(
                    String.format("%d:%02d", diaper.dateTime.getHour(), diaper.dateTime.getMinute())
            );

            commentView.setText(
                    diaper.text
            );
        }

        /*
        TextView timeView = view.findViewById(R.id.bottle_adapter_time);
        TextView amountView = view.findViewById(R.id.bottle_adapter_amount);

        timeView.setText( String.format("%d:%02d", bottle.start.getHour(), bottle.start.getMinute()));
        amountView.setText(String.format("%d", bottle.amount));

        TextView endTimeView = view.findViewById(R.id.bottle_adapter_end_time);
        endTimeView.setText(String.format("%d:%02d", bottle.end.getHour(), bottle.end.getMinute()));
        */
        return view;
    }


}
