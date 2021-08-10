package com.egocentric.babybottles.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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

import com.egocentric.babybottles.R;
import com.egocentric.babybottles.adapters.DiapersAdapter;
import com.egocentric.babybottles.data.Bottle;
import com.egocentric.babybottles.data.Diaper;
import com.egocentric.babybottles.db.DBHelper;
import com.egocentric.babybottles.dialogs.DiaperDialog;
import com.egocentric.babybottles.util.LocalDateTimeConverter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DiapersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DiapersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiapersFragment extends Fragment implements DiaperDialog.Callback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private TextView dateView;

    private ListView diapersList;
    private ArrayList<Diaper> diapers;
    private DiapersAdapter diapersAdapter;
    private LocalDate date;

    private Animation mLeftAnimation;
    private Animation mRightAnimation;

    private ImageButton fwdBtn;
    private ImageButton prevBtn;
    public DiapersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiapersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiapersFragment newInstance(String param1, String param2) {
        DiapersFragment fragment = new DiapersFragment();
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

    public static final String TAG = "DiapersFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_diapers, container, false);


        date = LocalDate.now();

        fwdBtn = view.findViewById(R.id.diapers_fwd);
        prevBtn = view.findViewById(R.id.diapers_prev);
        diapers = new ArrayList<>();
        diapersAdapter = new DiapersAdapter(this.getContext(), R.layout.diaper_adapter, diapers);
        diapersList = view.findViewById(R.id.diapers_list);
        diapersList.setAdapter(diapersAdapter);
        diapersList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Log.d(TAG, "OnItemLongClick!");
                Diaper diaper = (Diaper) adapterView.getItemAtPosition(i);
                //Bottle bottle = (Bottle) adapterView.getItemAtPosition(i);
                showDiaperDialog(diaper);

                return true;
            }
        });

        dateView = view.findViewById(R.id.diapers_date);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.diapers_add_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDiaperDialog();
            }
        });

        loadData(date);
        updateDateView(date);
        diapersAdapter.notifyDataSetChanged();


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
                //updateListData(date);
                loadData(date);
                diapersAdapter.notifyDataSetChanged();
                diapersList.startAnimation(mRightAnimation);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        fwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set date!
                // We set the date
                date = date.plusDays(1);
                updateDateView(date);
                // TODO: Update the date
                diapersList.startAnimation(mLeftAnimation);

            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                date = date.minusDays(1);
                updateDateView(date);
                diapersList.startAnimation(mLeftAnimation);
            }
        });

        return view;
    }

    private void loadData(LocalDate date)
    {
        DBHelper dbHelper = new DBHelper(this.getContext());

        diapers.clear();
        diapers.addAll(
                dbHelper.getDiapersForDate(date)
        );
        diapers.sort(Diaper.comparator);
    }




    private void updateDateView(LocalDate date)
    {
        dateView.setText( date.format(LocalDateTimeConverter.dateTimeFormatter));
    }

    public void showDiaperDialog()
    {
        showDiaperDialog(null);
    }
    public void showDiaperDialog(Diaper diaper)
    {
        DiaperDialog dialog = new DiaperDialog();
        dialog.setDate(date);
        if(diaper != null)
            dialog.setDiaper(diaper);
        dialog.setTargetFragment(this, 1);
        dialog.show(getFragmentManager(), "dialog");
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

    @Override
    public void cancel() {

    }

    @Override
    public void accept(Diaper diaper) {
        Toast.makeText(this.getContext(), "Ble tilføjet", Toast.LENGTH_LONG).show();
        DBHelper dbHelper = DBHelper.getInstance(this.getContext());
        dbHelper.createOrUpdate(diaper);
        loadData(date);
        diapersAdapter.notifyDataSetChanged();
    }

    @Override
    public void delete(Diaper diaper)
    {

        if(diaper.id != Bottle.UNASSIGNED_ID)
        {
            Toast.makeText(this.getContext(), "Ble slettet", Toast.LENGTH_LONG).show();
            DBHelper dbHelper = DBHelper.getInstance(this.getContext());
            dbHelper.delete(diaper);
            loadData(date);
            diapersAdapter.notifyDataSetChanged();
        }

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
    private void changeDate(LocalDate date)
    {
        this.date = date;

        updateDateView(this.date);
        diapersList.startAnimation(mLeftAnimation);
    }
}
