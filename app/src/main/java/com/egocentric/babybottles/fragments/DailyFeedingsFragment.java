package com.egocentric.babybottles.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.egocentric.babybottles.dialogs.AddFeedingDialog;
import com.egocentric.babybottles.R;
import com.egocentric.babybottles.adapters.BottlesAdapter;
import com.egocentric.babybottles.data.Bottle;
import com.egocentric.babybottles.db.DBHelper;
import com.egocentric.babybottles.util.LocalDateTimeConverter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DailyFeedingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DailyFeedingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyFeedingsFragment extends Fragment implements AddFeedingDialog.Callback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private TextView dailyBottlesText;
    private TextView dailyAmountText;
    private TextView dateText;

    private ListView bottleListview;

    private int nBottles = 0;
    private int amount = 0;

    private ArrayList<Bottle> bottles;
    private BottlesAdapter bottlesAdapter;

    private LocalDate date;

    private Animation mLeftAnimation;
    private Animation mRightAnimation;

    public DailyFeedingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DailyFeedingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DailyFeedingsFragment newInstance(String param1, String param2) {
        DailyFeedingsFragment fragment = new DailyFeedingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setHasOptionsMenu(true);


    }

    public static final String TAG = "DailyFeedingsFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_daily_feedings, container, false);

        date = LocalDate.now();

        bottles = new ArrayList<>();
        bottlesAdapter = new BottlesAdapter(this.getContext(), R.layout.bottle_adapter, bottles);

        bottleListview = view.findViewById(R.id.daily_feedings_list);
        bottleListview.setAdapter(bottlesAdapter);

        //registerForContextMenu(bottleListview);


        bottleListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "OnItemLongClick!");
               Bottle bottle = (Bottle) adapterView.getItemAtPosition(i);
                showBottleDialog(bottle);

                return true;
            }
        });


        dateText = view.findViewById(R.id.daily_feedings_date);
        dailyBottlesText = view.findViewById(R.id.daily_feedings_bottles);
        dailyAmountText = view.findViewById(R.id.daily_feedings_amount);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.daily_feedings_add_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottleDialog();
            }
        });
        loadData(date);
        updateViews();
        updateDateView(date);

        ImageButton fwdButton = view.findViewById(R.id.daily_feedings_fwd);
        ImageButton prevButton = view.findViewById(R.id.daily_feedings_prev);

        /*
        fwdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDate(date.plusDays(1));
            }
        });

        */


        mLeftAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_left);
        mRightAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right);


        mLeftAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                //animation.setAnimationListener(null);
                //changeDate(date.plusDays(1));
                updateListData();
                bottleListview.startAnimation(mRightAnimation);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        fwdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set date!
                // We set the date
                date = date.plusDays(1);
                updateDateView(date);
                // TODO: Update the date
                bottleListview.startAnimation(mLeftAnimation);

            }
        });

        prevButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                date = date.minusDays(1);
                updateDateView(date);
                bottleListview.startAnimation(mLeftAnimation);
            }
        });

        /*

        final GestureDetector gesture = new GestureDetector(getActivity() , new GestureDetector.SimpleOnGestureListener(){

            @Override
            public boolean onDown(MotionEvent e)
            {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
            {
                Log.i(TAG, "onFling!");
                final int SWIPE_MIN_DISTANCE = 120;
                final int SWIPE_MAX_OFF_PATH = 250;
                final int SWIPE_THRESHOLD_VELOCITY = 200;
                try {
                    if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                        return false;
                    if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                            && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                        Log.i(TAG, "Right to Left");
                    } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                            && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                        Log.i(TAG, "Left to Right");
                    }
                } catch (Exception e) {
                    // nothing
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }

        });



        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                Log.i(TAG, "onTouch()");

                return gesture.onTouchEvent(motionEvent);
            }
        });

        */
        return view;
    }

    public void changeDate(LocalDate date)
    {
        this.date = date;
        updateDateView(this.date);
        bottleListview.startAnimation(mLeftAnimation);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, view, menuInfo);

    }

    /*
    private void changeDate(LocalDate newDate)
    {
        this.date = newDate;
        updateDateView(this.date);
        loadData(this.date);
        updateViews();

        // TODO: Testing animations
    }
    */

    private void updateListData()
    {
        updateDateView(this.date);
        loadData(this.date);
        updateViews();

    }

    private void loadData(LocalDate date)
    {

        DBHelper dbHelper = new DBHelper(this.getContext());

        bottles.clear();
        bottles.addAll(
            dbHelper.getBottlesForDate(date)
        );
        bottles.sort(Bottle.bottleComparator);
        recalcDaily();
        bottlesAdapter.notifyDataSetChanged();


    }

    private void showBottleDialog()
    {

        AddFeedingDialog dialog = new AddFeedingDialog();

        dialog.setStartDate(date);
        dialog.setTargetFragment(this, 1);
        dialog.show(getFragmentManager(), "dialog");

    }

    private void showBottleDialog(Bottle bottle)
    {
        AddFeedingDialog dialog = new AddFeedingDialog();
        dialog.setStartDate(date);
        dialog.setTargetFragment(this, 1);
        dialog.setBottle(bottle);
        dialog.show(getFragmentManager(), "dialog");
    }

    private void recalcDaily()
    {
        amount = 0;
        nBottles = 0;
        for(Bottle b: bottles)
        {
            amount += b.amount;
            nBottles++;
        }

    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void updateViews()
    {
        dailyAmountText.setText( String.format("%d", amount));
        dailyBottlesText.setText(String.format("%d", nBottles));
    }

    public void updateDateView(LocalDate date)
    {
        if(date == null)
            date = LocalDate.now();


        this.dateText.setText(date.format(LocalDateTimeConverter.dateTimeFormatter));
    }

    @Override
    public void cancel()
    {

    }

    @Override
    public void accept(Bottle bottle)
    {
        Toast.makeText(this.getContext(), "Flaske Tilføjet.", Toast.LENGTH_LONG).show();
        DBHelper dbHelper = DBHelper.getInstance(this.getContext());
        dbHelper.createOrUpdate(bottle);
        // Check if the correct date.
        loadData(date);
        updateViews();
    }

    @Override
    public void delete(Bottle bottle)
    {
        Toast.makeText(this.getContext(),"Flaske Slettet", Toast.LENGTH_LONG).show();
        DBHelper dbHelper = DBHelper.getInstance(this.getContext());
        dbHelper.delete(bottle);
        loadData(date);
        updateViews();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_date) {
            final Calendar c = Calendar.getInstance();
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            int mMonth = c.get(Calendar.MONTH);
            int mYear = c.get(Calendar.YEAR);
            // Show the calendar dialog

            DatePickerDialog datePickerDialog = new DatePickerDialog(this.getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                    LocalDate date = LocalDate.of(
                            year, month+1, day
                    );

                    changeDate(date);
                }
            }, mYear, mMonth, mDay);


            datePickerDialog.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
