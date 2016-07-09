package com.acukanov.hivet.utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static boolean isToday(long timeMilliseconds) {
        Calendar calendar = Calendar.getInstance();
        int todayYear = calendar.get(Calendar.YEAR);
        int todayMonth = calendar.get(Calendar.MONTH);
        int todayDay = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTimeInMillis(timeMilliseconds);
        return calendar.get(Calendar.YEAR) == todayYear &&
                calendar.get(Calendar.MONTH) == todayMonth &&
                calendar.get(Calendar.DAY_OF_MONTH) == todayDay;
    }

    public static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
