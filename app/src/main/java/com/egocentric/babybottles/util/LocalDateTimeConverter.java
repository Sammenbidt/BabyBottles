package com.egocentric.babybottles.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeConverter {

    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static LocalDateTime toDate(String dateString) {
        if (dateString == null) {
            return null;
        } else {
            return LocalDateTime.parse(dateString);
        }
    }

    public static String toDateString(LocalDateTime date) {
        if (date == null) {
            return null;
        } else {
            return date.toString();
        }
    }

    public static LocalTime toTime(String timeString)
    {
        if(timeString == null)
            return null;
        return
                LocalTime.parse(timeString);
    }

    public static String toTimeString(LocalTime time)
    {
        if(time == null)
            return null;
        return time.toString();
    }
}
