package com.engrisk.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {
    public static final DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    public static String format(Date date) {
        return formatter.format(date);
    }

    public static Date parseDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate parseLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static boolean isOverlapping(Date start1, Date end1, Date start2, Date end2) {
        return !(start1.after(end2) || end1.before(start2));
    }
}
