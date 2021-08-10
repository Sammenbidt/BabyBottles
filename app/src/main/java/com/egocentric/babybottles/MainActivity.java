package com.egocentric.babybottles;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;

import com.egocentric.babybottles.db.DBHelper;
import com.egocentric.babybottles.fragments.DailyFeedingsFragment;
import com.egocentric.babybottles.fragments.DiapersFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.time.LocalDate;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DailyFeedingsFragment.OnFragmentInteractionListener, DiapersFragment.OnFragmentInteractionListener {

    public static final int CREATE_FEEDING_RESULT_CODE = 1;

    private DBHelper dbHelper;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PACKAGE_NAME = getApplicationContext().getPackageName();
        dbHelper = new DBHelper(this);


        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if(prev != null)
                    ft.remove(prev);

                AddFeedingDialog fragment = new AddFeedingDialog();
                // ADD BUNDLE !
                ft.addToBackStack(null);
                //fragment.setArguments(bundle);

                // Add button handlers

                fragment.setListener(MainActivity.this);
                fragment.show(ft, "dialog");



            }
        });
        */


        /*
        addButton = (FloatingActionButton) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              FragmentTransaction ft = getFragmentManager().beginTransaction();

              Fragment prev = getFragmentManager().findFragmentByTag("dialog");
              if( prev != null)
                  ft.remove(prev);

              AddItemDialogFragment fragment = new AddItemDialogFragment();
              Bundle bundle = new Bundle();
              bundle.putInt(AddItemDialogFragment.ITEM_TYPE, currentState.getID());

              //bundle.putByte(AddItemDialogFragment.ITEM_TYPE, AddItemDialogFragment.GAME );
              ft.addToBackStack(null);

              fragment.setArguments(bundle);
              fragment.show(ft, "dialog");
            }
        });
         */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //findViewById(R.id.nav_daily).callOnClick();
        setDayliFeedingsFragment();
        // TODO: Remove ?

    }

    private static final int MY_PERMISSION_CONSTANT = 50;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        /*
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_date) {
            final Calendar c = Calendar.getInstance();
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            int mMonth = c.get(Calendar.MONTH);
            int mYear = c.get(Calendar.YEAR);
            // Show the calendar dialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                    LocalDate date = LocalDate.of(
                            year, month + 1, day
                    );

                }
            }, mYear, mMonth - 1, mDay);

            datePickerDialog.show();
            return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }


    private void setDayliFeedingsFragment()
    {
        Fragment frag = null;
        frag = new DailyFeedingsFragment();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.daily_bottles);

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, frag);
        transaction.commit();
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        android.support.v4.app.Fragment frag = null;
        if (id == R.id.nav_daily) {

            frag = new DailyFeedingsFragment();

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle(R.string.daily_bottles);


            // Handle the camera action
        }else if( id == R.id.nav_diapers)
        {
            frag = new DiapersFragment();
            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle(R.string.diapers);

        }
        /*
        else if(id == R.id.nav_monthly)
        {
            frag = new WeeklyBottlesFragment();

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle(R.string.weekly_bottles);
        }
        */
        /*
        else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        */
        else if( id == R.id.nav_exportDB)
        {
            exportDB();
        }
        if(frag != null)
        {
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, frag);
            transaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static final String TAG = "MainActivity";


    public static String PACKAGE_NAME;
    private void exportDB()
    {
        Log.d(TAG, "ExportDB()");
        Log.d(TAG,"Package: " + PACKAGE_NAME);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this, "Allow storage permission", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions( (Activity) this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_CONSTANT );

        }else {


            String DatabaseName = DBHelper.DATABASE_NAME;
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            FileChannel source = null;
            FileChannel destination = null;
            String currentDBPath = "/data/" + PACKAGE_NAME + "/databases/" + DatabaseName;
            String backupDBPath = "BabyBottles.db";
            File currentDB = new File(data, currentDBPath);
            File backupDB = new File(sd, backupDBPath);
            try {
                source = new FileInputStream(currentDB).getChannel();
                destination = new FileOutputStream(backupDB).getChannel();
                destination.transferFrom(source, 0, source.size());
                source.close();
                destination.close();
                Toast.makeText(this, "Your Database is Exported to " + backupDB.getAbsolutePath(), Toast.LENGTH_LONG).show();
                Log.d(TAG, "Database exported to: " + backupDB.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

    }
}

