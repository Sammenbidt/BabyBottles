package com.egocentric.babybottles.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.egocentric.babybottles.data.Bottle;
import com.egocentric.babybottles.data.Diaper;
import com.egocentric.babybottles.util.LocalDateTimeConverter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static boolean DEBUG = true;

    private static DBHelper instance;
    private SQLiteDatabase db;
    private static final String TAG = "DBHelper";

    private static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "babybottles";

    //Feedings
    public static final String TABLE_BOTTLES = "bottles";
    public static final String BOTTLES_ID = "bottles_id";
    public static final String BOTTLES_AMOUNT = "bottles_amount";
    public static final String BOTTLES_START = "bottles_start";
    public static final String BOTTLES_END = "bottles_end";


    // Diapers
    public static final String TABLE_DIAPERS = "diapers";
    public static final String DIAPERS_ID = "diapers_id";
    public static final String DIAPERS_DATE_TIME = "diapers_date";
    public static final String DIAPERS_COMMENTARY = "diapers_comment";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    private static final String CREATE_FEEDINGS_TABLE =
            "CREATE TABLE " + TABLE_BOTTLES + " (" +
                    "'" + BOTTLES_ID + "' INTEGER PRIMARY KEY NOT NULL, " +
                    "'" + BOTTLES_AMOUNT + "' INTEGER," +
                    "'" + BOTTLES_START + "' DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    "'" + BOTTLES_END + "' DATETIME" +
                    ");";

    private static final String CREATE_DIAPERS_TABLE =
            "CREATE TABLE " + TABLE_DIAPERS + " (" +
                    "'" + DIAPERS_ID + "' INTEGER PRIMARY KEY NOT NULL, " +
                    "'" + DIAPERS_DATE_TIME + "' DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "'" + DIAPERS_COMMENTARY + "' TEXT" +
                    ");";


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creating Table: \n" + CREATE_FEEDINGS_TABLE);
        Log.d(TAG, CREATE_FEEDINGS_TABLE);
        Log.d(TAG, CREATE_DIAPERS_TABLE);

        db.execSQL(CREATE_FEEDINGS_TABLE);
        db.execSQL(CREATE_DIAPERS_TABLE);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.d(TAG, "Updating table");


        db.execSQL(
                "DROP TABLE IF EXISTS  " + TABLE_BOTTLES
        );
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIAPERS);

        onCreate(db);
    }

    public Bottle updateBottle(Bottle bottle) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(
                BOTTLES_AMOUNT, bottle.amount
        );
        values.put(
                BOTTLES_START, LocalDateTimeConverter.toDateString(bottle.start)
        );
        values.put(
                BOTTLES_END, LocalDateTimeConverter.toDateString(bottle.end)
        );

        db.update(
                TABLE_BOTTLES, values, BOTTLES_ID + " = ?", new String[]{bottle.id + ""}

        );

        return bottle;
    }



    private static String dateToSQLDate(LocalDate date)
    {
        String s = String.format("%d-%02d-%02d", date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        Log.d(TAG, "datTimeToSQLDate: " +date.toString() + " -- " + s);
        return s;
    }
/*
    private static String dateTimeToSQLDate(LocalDateTime start)
    {
        String s = String.format("%d-%02d-%02d", start.getYear(), start.getMonth(), start.getDayOfMonth());
        Log.d(TAG, "datTimeToSQLDate: " +start.toString() + " -- " + s);
        return s;
    }

    private static String dateTimeToSQLDateNextDay(LocalDateTime start)
    {
        LocalDateTime nextDay = start.plusDays(1);
        String s = String.format("%d-%02d-%02d", nextDay.getYear(), nextDay.getMonth(), nextDay.getDayOfMonth());
        Log.d(TAG, "datTimeToSQLDate: " +start.toString() + " -- " + s);
        return s;


    }
    */
/*
    public List<Bottle> getBottlesForDate(LocalDateTime date) {
        LocalDate localDate = date.toLocalDate();
       return getBottlesForDate(localDate);
    }

    */
    public List<Bottle> getBottlesForDate(LocalDate date)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Bottle> bottles = new ArrayList<>();

        String sql = "SELECT * FROM '" + TABLE_BOTTLES +
                "' where " +

                "( " + TABLE_BOTTLES + "." + BOTTLES_START + " >= '" + dateToSQLDate(date) + "' AND " + TABLE_BOTTLES + "." + BOTTLES_START + " <= '" +dateToSQLDate(date.plusDays(1)) + "' ) " +
                    " OR  (" +

                TABLE_BOTTLES + "." + BOTTLES_END   + " >= '" + dateToSQLDate(date) + "' AND " + TABLE_BOTTLES + "." + BOTTLES_END   + " <= '" + dateToSQLDate(date.plusDays(1)) +"' " + " ) " +
                "   ORDER BY " + BOTTLES_START;
        Log.d(TAG, "SQL:" + sql);
        Cursor cursor = db.rawQuery(
                sql,
                null
        );

        Log.d(TAG, "Cursor : " + cursor.getCount());
        if (cursor.getCount() == 0)
            return bottles;


        cursor.moveToFirst();
        do {
            Bottle bottle = Bottle.fromCursor(cursor);
            bottles.add(bottle);
        } while (cursor.moveToNext());
        return bottles;

    }




    public Bottle createOrUpdate(Bottle bottle) {
        if (bottle.id == Bottle.UNASSIGNED_ID) {
            return createBottle(bottle);
        } else {
            //return updateBottle(bottle);
            return updateBottle(bottle);
        }


    }

    public Bottle createBottle(Bottle bottle) {

        Log.d(TAG, "CreateBottle()");
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BOTTLES_AMOUNT, bottle.amount);
        values.put(BOTTLES_START, LocalDateTimeConverter.toDateString(bottle.start));
        values.put(BOTTLES_END, LocalDateTimeConverter.toDateString(bottle.end));
        long id = db.insert(
                TABLE_BOTTLES,
                null,
                values

        );

        bottle.id = id;

        db.close();
        return bottle;
    }

    public static DBHelper getInstance(Context context)
    {
        if(instance == null)
            instance = new DBHelper(context);
        return instance;

    }


    public void delete(Bottle bottle)
    {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(
                TABLE_BOTTLES,
                BOTTLES_ID + " = ?",
                new String[]{bottle.id + ""}
        );

        db.close();
    }


    public Diaper createDiaper(Diaper diaper)
    {
        Log.d(TAG, "CreateBottle()");
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DIAPERS_DATE_TIME, LocalDateTimeConverter.toDateString(diaper.dateTime));
        values.put(DIAPERS_COMMENTARY, diaper.text);
        //values.put(BOTTLES_AMOUNT, diaper.amount);
        //values.put(BOTTLES_START, LocalDateTimeConverter.toDateString(bottle.start));
        //values.put(BOTTLES_END, LocalDateTimeConverter.toTimeString(bottle.end));
        long id = db.insert(
                TABLE_DIAPERS,
                null,
                values

        );

        diaper.id = id;

        db.close();
        return diaper;
    }

    public Diaper updateDiaper(Diaper diaper)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(
                DIAPERS_DATE_TIME, LocalDateTimeConverter.toDateString(diaper.dateTime)
        );
        values.put(
                DIAPERS_COMMENTARY, diaper.text
        );


        db.update(
                TABLE_DIAPERS, values, DIAPERS_ID + " = ?", new String[]{diaper.id + ""}

        );

        return diaper;
    }

    public Diaper createOrUpdate(Diaper diaper)
    {
        if(diaper.id == Bottle.UNASSIGNED_ID)
        {
            return createDiaper(diaper);
        }
        return updateDiaper(diaper);
    }
    public void delete(Diaper diaper)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(
                TABLE_DIAPERS,
                DIAPERS_ID + " = ?",
                new String[]{diaper.id + ""}
        );
        db.close();
    }

    public List<Diaper> getDiapersForDate(LocalDate date)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Diaper> diapers = new ArrayList<>();

        String sql = "SELECT * FROM '" + TABLE_DIAPERS + "' where " + TABLE_DIAPERS + "." + DIAPERS_DATE_TIME + " >= '"+ dateToSQLDate(date) + "' AND " + TABLE_DIAPERS + "." + DIAPERS_DATE_TIME + " <= '" +dateToSQLDate(date.plusDays(1)) + "' ORDER BY " + DIAPERS_DATE_TIME;
        Log.d(TAG, "SQL:" + sql);
        Cursor cursor = db.rawQuery(
                sql,
                null
        );

        Log.d(TAG, "Cursor : " + cursor.getCount());
        if (cursor.getCount() == 0)
            return diapers;


        cursor.moveToFirst();
        do {
            Diaper diaper = Diaper.fromCursor(cursor);
            //Bottle bottle = Bottle.fromCursor(cursor);
            diapers.add(diaper);
        } while (cursor.moveToNext());
        return diapers;

    }
}
