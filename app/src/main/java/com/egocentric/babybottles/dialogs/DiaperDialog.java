package com.egocentric.babybottles.dialogs;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.egocentric.babybottles.R;
import com.egocentric.babybottles.data.Bottle;
import com.egocentric.babybottles.data.Diaper;
import com.egocentric.babybottles.util.LocalDateTimeConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DiaperDialog extends DialogFragment implements View.OnClickListener, View.OnFocusChangeListener {

    private static final String TAG = "DiaperDialog";

    private LocalDate date;
    private LocalTime time;

    private TextView diaperDateView;
    private TextView diaperTimeView;
    private TextView diaperCommentView;
    private TextView titleView;

    private Button saveBtn;
    private Button cancelBtn;
    private Button deleteBtn;

    private Diaper diaper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.diaper_dialog, container, false);
    }
    @Override
    public void onClick(View view)
    {

        if(view == diaperDateView)
        {
            int mDay = date.getDayOfMonth(); //c.get(Calendar.DAY_OF_MONTH);
            int mMonth = date.getMonthValue(); //c.get(Calendar.MONTH);
            int mYear  = date.getYear();// c.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this.getContext(), new DatePickerDialog.OnDateSetListener(){


                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    date = date.withYear(year);
                    date = date.withMonth(month +1);
                    date = date.withDayOfMonth(day);
                    updateDateText(date);
                }
            }, mYear , mMonth -1 , mDay );

            datePickerDialog.show();
        }else if(view == diaperTimeView)
        {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this.getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minute)
                {
                    time = time.withMinute(minute);
                    time = time.withHour(hour);

                    updateTimeText(time);


                }
            }, time.getHour(), time.getMinute(), true);
            timePickerDialog.show();
        }else if(view == cancelBtn)
        {
            dismiss();
        }else if(view == saveBtn)
        {
            // Check everything is set.
            if(
                    diaperDateView.getText().length() == 0 || diaperTimeView.getText().length() == 0
            ){
                // TODO: Show error message
                return;
            }
            // Save the bottle
            if(this.diaper == null)
                diaper = new Diaper();

            diaper.dateTime = LocalDateTime.of(date, time);
            diaper.text = diaperCommentView.getText().toString();


            Callback callback = null;
            try {
                callback = (Callback) getTargetFragment();
            }catch(ClassCastException e)
            {
                Log.d(TAG, "Callback of this class must be implemented by target fragment!" , e);
            }
            if(callback != null)
            {
                callback.accept(diaper);
            }

            dismiss();
        }else if(view == deleteBtn)
        {
            Callback callback = null;
            try {
                callback = (Callback) getTargetFragment();
            }catch(ClassCastException e)
            {
                Log.d(TAG, "Callback of this class must be implemented by target fragment!" , e);
            }

            callback.delete(diaper);

            dismiss();
        }

    }

    @Override
    public void onFocusChange(View view, boolean focus)
    {
        if(focus)onClick(view);

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        titleView = view.findViewById(R.id.diaper_dialog_title);
        diaperDateView = view.findViewById(R.id.diaper_dialog_date);
        diaperTimeView = view.findViewById(R.id.diaper_dialog_time);
        diaperCommentView = view.findViewById(R.id.diaper_dialog_comment);


        saveBtn = view.findViewById(R.id.diaper_dialog_save_btn);
        cancelBtn = view.findViewById(R.id.diaper_dialog_cancel_btn);
        deleteBtn = view.findViewById(R.id.diaper_dialog_delete_btn);


        //Bundle bundle = getArguments();

        if(date == null)
            date = LocalDate.now();
        if(time == null)
            time = LocalTime.now();




        diaperDateView.setOnClickListener(this);
        diaperTimeView.setOnClickListener(this);

        diaperDateView.setOnFocusChangeListener(this);
        diaperTimeView.setOnFocusChangeListener(this);

        cancelBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);

        if(diaper != null)
        {
            this.time = diaper.dateTime.toLocalTime();
            this.date = diaper.dateTime.toLocalDate();
            this.diaperCommentView.setText(diaper.text);


            deleteBtn.setVisibility(View.VISIBLE);
        }
        updateDateText(date);
        updateTimeText(time);

        diaperCommentView.setFocusedByDefault(true);

        if(diaper != null)
        {
            titleView.setText("Rediger Ble");
            deleteBtn.setVisibility(View.VISIBLE);
        }


    }



    private void updateDateText(LocalDate date)
    {
        diaperDateView.setText(
                date.format(LocalDateTimeConverter.dateTimeFormatter)
        );
    }

    private void updateTimeText(LocalTime time)
    {
        diaperTimeView.setText(
                String.format("%d:%02d", time.getHour(), time.getMinute())
        );
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public void setTime(LocalTime time)
    {
        this.time = time;
    }

    public void setDiaper(Diaper diaper)
    {
        this.diaper = diaper;
    }

    public interface Callback
    {
        public void cancel();
        public void accept(Diaper diaper);
        public void delete(Diaper diaper);
    }
}
