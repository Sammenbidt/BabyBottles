package com.egocentric.babybottles.data;

import android.database.Cursor;

import com.egocentric.babybottles.db.DBHelper;
import com.egocentric.babybottles.util.LocalDateTimeConverter;

import java.time.LocalDateTime;
import java.util.Comparator;

public class Diaper {


    public long id = Bottle.UNASSIGNED_ID;
    public LocalDateTime dateTime;
    public String text;


    public static Diaper fromCursor(Cursor cursor)
    {

        //long id = cursor.getLong(0);
        //int amount = cursor.getInt(1);
        //LocalDateTime start = LocalDateTimeConverter.toDate(cursor.getString(2));
        Diaper diaper = new Diaper();
        diaper.id = cursor.getInt( cursor.getColumnIndex(DBHelper.DIAPERS_ID));
        diaper.dateTime = LocalDateTimeConverter.toDate(
                cursor.getString(cursor.getColumnIndex(DBHelper.DIAPERS_DATE_TIME))
        );
        diaper.text = cursor.getString(
                cursor.getColumnIndex(DBHelper.DIAPERS_COMMENTARY)
        );

        return diaper;
    }

    public static Comparator<Diaper> comparator = new Comparator<Diaper>() {
        @Override
        public int compare(Diaper d1, Diaper d2) {
            return d2.dateTime.compareTo(d1.dateTime);
        }
    };

}
