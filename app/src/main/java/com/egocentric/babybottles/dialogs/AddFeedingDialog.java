package com.egocentric.babybottles.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
//import android.app.DialogFragment;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
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
import com.egocentric.babybottles.util.LocalDateTimeConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AddFeedingDialog extends DialogFragment implements View.OnClickListener, View.OnFocusChangeListener{

    private static final String TAG = "AddFeedingDialog";
    private TextView startDateView;
    private TextView startTimeView;
    private TextView feedingAmount;
    private TextView endTimeView;
    private TextView endDateView;

    /*
    private ImageButton feedingDateBtn;
    private ImageButton feedingTimeBtn;
    */

    //private LocalDateTime dateTime;
    private Bottle bottle;

    private LocalDate startDate;
    private LocalTime startTime;

    private LocalDate endDate;
    private LocalTime endTime;

    private Button saveBtn;
    private Button cancelBtn;
    private Button deleteBtn;


    public void setEndTime(LocalTime time)
    {
        this.endTime = time;
    }

    public void setBottle(Bottle bottle)
    {
        this.bottle = bottle;
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.create_feeding, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        startDateView = view.findViewById(R.id.add_feeding_date_start);
        startTimeView = view.findViewById(R.id.add_feeding_time_start);
        feedingAmount = view.findViewById(R.id.add_feeding_amount);
        endTimeView = view.findViewById(R.id.add_bottle_end_time);
        endDateView = view.findViewById(R.id.add_bottle_end_date);
        //feedingTimeBtn = view.findViewById(R.id.add_feeding_time_btn);
        //feedingDateBtn = view.findViewById(R.id.add_feeding_date_btn);

        saveBtn = view.findViewById(R.id.add_feeding_save_btn);
        cancelBtn = view.findViewById(R.id.add_feeding_cancel_btn);
        deleteBtn = view.findViewById(R.id.add_feeding_delete_btn);


        //Bundle bundle = getArguments();

        if(startDate == null) startDate = LocalDate.now();
        endDate = startDate;
        startTime = LocalTime.now();
        endTime =  LocalTime.now();

        startDateView.setOnClickListener(this);
        //startDateView.setOnClickListener(dateClickListener);
        startTimeView.setOnClickListener(this);
        endTimeView.setOnClickListener(this);
        endDateView.setOnClickListener(this);

        startDateView.setOnFocusChangeListener(this);
        startTimeView.setOnFocusChangeListener(this);
        endTimeView.setOnFocusChangeListener(this);
        endDateView.setOnFocusChangeListener(this);
     /*
        startDateView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus)
                    dateClickListener.onClick(view);

            }
        });
        */


        //feedingDateBtn.setOnClickListener( this);
        //feedingTimeBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);

        if(bottle != null)
        {
            this.feedingAmount.setText(bottle.amount + "");
            this.startDate = bottle.start.toLocalDate();
            this.startTime = bottle.start.toLocalTime();

            this.endDate = bottle.end.toLocalDate();
            this.endTime = bottle.end.toLocalTime();
            deleteBtn.setVisibility(View.VISIBLE);
            ((TextView)view.findViewById(R.id.add_bottle_title)).setText("Rediger Flaske");

        }

        updateStartDateText();
        updateStartTimeText();
        updateEndDateText();
        updateEndTimeText();


    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onClick(View view)
    {
        if(view == startDateView)
        {
            int mDay = startDate.getDayOfMonth(); //c.get(Calendar.DAY_OF_MONTH);
            int mMonth = startDate.getMonthValue(); //c.get(Calendar.MONTH);
            int mYear  = startDate.getYear();// c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this.getContext(), new DatePickerDialog.OnDateSetListener(){


                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    startDate = startDate.withYear(year);
                    startDate = startDate.withMonth(month +1);
                    startDate = startDate.withDayOfMonth(day);
                    updateStartDateText();

                }
            }, mYear , mMonth -1 , mDay );

            datePickerDialog.show();
        }else if(view == startTimeView)
        {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this.getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minute)
                {
                    startTime = startTime.withMinute(minute);
                    startTime = startTime.withHour(hour);
                    updateStartTimeText();
                }
            }, startTime.getHour(), startTime.getMinute(), true);
            timePickerDialog.show();
        }else if(view == endTimeView)
        {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this.getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                    endTime = endTime.withHour(hour);
                    endTime = endTime.withMinute(minute);
                    updateEndTimeText();
                }
            }, endTime.getHour(), endTime.getMinute() ,true );
            timePickerDialog.show();

        }else if(view == endDateView)
        {
            int mDay = endDate.getDayOfMonth(); //c.get(Calendar.DAY_OF_MONTH);
            int mMonth = endDate.getMonthValue(); //c.get(Calendar.MONTH);
            int mYear  = endDate.getYear();// c.get(Calendar.YEAR);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this.getContext(), new DatePickerDialog.OnDateSetListener(){


                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    endDate = endDate.withYear(year);
                    endDate = endDate.withMonth(month + 1);
                    endDate = endDate.withDayOfMonth(day);
                    updateEndDateText();

                }
            }, mYear , mMonth -1 , mDay );


            datePickerDialog.show();

        }else if(view == cancelBtn)
        {
            dismiss();
        }else if(view == saveBtn)
        {
            // Check everything is set.
            if(
                    startDateView.getText().length() == 0 || startTimeView.getText().length() == 0 || feedingAmount.getText().length() == 0 || endTimeView.getText().length() == 0 || endDateView.getText().length() == 0
            ){
                // TODO: Show error message
                return;
            }
            // Save the bottle
            if(this.bottle == null)
                bottle = new Bottle();
            bottle.start = LocalDateTime.of(startDate, startTime);
            bottle.amount = Integer.parseInt(feedingAmount.getText().toString());

            bottle.end = LocalDateTime.of(endDate, endTime);
            Callback callback = null;
            try {
                callback = (Callback) getTargetFragment();
            }catch(ClassCastException e)
            {
                Log.d(TAG, "Callback of this class must be implemented by target fragment!" , e);
            }
            if(callback != null)
            {
                callback.accept(bottle);
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

            callback.delete(bottle);

            dismiss();
        }

    }

    @Override
    public void onFocusChange(View view, boolean hasFocus)
    {
        if(hasFocus)
        {
            onClick(view);
        }
    }

    public void setStartDate(LocalDate date) {
        this.startDate = date;
    }

    /*
    private FeedingDialogListener listener = null;
    public void setListener(FeedingDialogListener listener)
    {
        this.listener = listener;
    }

    public interface FeedingDialogListener {
        void onFinishEditDialog(Bottle feeding);
    }
    */

    public interface Callback
    {
        public void cancel();
        public void accept(Bottle bottle);
        public void delete(Bottle bottle);
    }

    private void updateStartDateText()
    {
        startDateView.setText(
                startDate.format(LocalDateTimeConverter.dateTimeFormatter)
        );

    }

    private void updateEndDateText()
    {
        endDateView.setText(
                endDate.format(LocalDateTimeConverter.dateTimeFormatter)
        );

    }

    private void updateStartTimeText()
    {
        startTimeView.setText(
                String.format("%d:%02d", startTime.getHour(), startTime.getMinute())
        );
    }

    private void updateEndTimeText()
    {
        endTimeView.setText(
                String.format("%d:%02d", endTime.getHour(), endTime.getMinute())
        );

    }


}
