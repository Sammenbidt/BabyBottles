package com.egocentric.babybottles.data;

import android.database.Cursor;

import com.egocentric.babybottles.db.DBHelper;
import com.egocentric.babybottles.util.LocalDateTimeConverter;

import java.time.LocalDateTime;
import java.util.Comparator;

public class Bottle {

    public static final int UNASSIGNED_ID = -1;
    public long id = UNASSIGNED_ID;
    public LocalDateTime start;
    public int amount;
    public LocalDateTime end;


    public static final Comparator<Bottle> bottleComparator = new Comparator<Bottle>() {
        @Override
        public int compare(Bottle bottle, Bottle bottle2) {

            return bottle2.start.compareTo(bottle.start);
        }
    };


    public static Bottle fromCursor(Cursor cursor)
    {

        //long id = cursor.getLong(0);
        //int amount = cursor.getInt(1);
        //LocalDateTime start = LocalDateTimeConverter.toDate(cursor.getString(2));
        Bottle bottle = new Bottle();
        bottle.id = cursor.getInt( cursor.getColumnIndex(DBHelper.BOTTLES_ID));
        bottle.amount = cursor.getInt( cursor.getColumnIndex(DBHelper.BOTTLES_AMOUNT));
        bottle.start = LocalDateTimeConverter.toDate(
                cursor.getString( cursor.getColumnIndex(DBHelper.BOTTLES_START))
        );
        bottle.end = LocalDateTimeConverter.toDate(
                cursor.getString( cursor.getColumnIndex(DBHelper.BOTTLES_END))
        );
        return bottle;
    }
}
