package com.app.metown.AppConstants;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class DateUtil {

    public static final long SECOND_IN_MILLIS = 1000;
    public static final long MINUTE_IN_MILLIS = SECOND_IN_MILLIS * 60;
    public static final long HOUR_IN_MILLIS = MINUTE_IN_MILLIS * 60;
    public static final long DAY_IN_MILLIS = HOUR_IN_MILLIS * 24;
    public static final long YEAR_IN_MILLIS = DAY_IN_MILLIS * 365;

    private static final long MILLIS_VALUE = 1000;
    private static final String STRING_TODAY = "Today";
    private static final String STRING_YESTERDAY = "Yesterday";

    private static final SimpleDateFormat FULL_DATE_FORMAT;
    private static final SimpleDateFormat DATE_TIME_FORMAT;
    private static final SimpleDateFormat FULL_DATETIME_FORMAT;
    private static final SimpleDateFormat SIMPLE_TIME_FORMAT;
    private static final SimpleDateFormat DAY_FORMAT;
    private static final SimpleDateFormat DAY_get;
    private static final SimpleDateFormat DAYNAME_get;
    private static final SimpleDateFormat DAY_getdate;
    private static final SimpleDateFormat DAY_mydate;
    private static final SimpleDateFormat USA_FORMAT;
    private static final SimpleDateFormat TIME_HHMMSS;
    private static final SimpleDateFormat TIME_AMPM;
    private static final SimpleDateFormat GET_DAY_MONTH;
    private static final SimpleDateFormat DayFormat;
    private static final SimpleDateFormat DateTime;
    private static final SimpleDateFormat NewDateTime;
    private static final SimpleDateFormat fullDate;
    private static final SimpleDateFormat New_FORMAT;
    private static final SimpleDateFormat Only_month_german;
    private static final SimpleDateFormat Only_month_cortion;
    private static final SimpleDateFormat Only_month_English;
    private static final SimpleDateFormat NewFULL_DATE_FORMAT;
    private static final SimpleDateFormat NEW_FULL_DATE_FORMAT;

    static {
        FULL_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        NewFULL_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        NEW_FULL_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.ENGLISH);
        DATE_TIME_FORMAT = new SimpleDateFormat("MMMM dd, yyyy hh:mm a", Locale.ENGLISH);
        DayFormat = new SimpleDateFormat("EEE, MMM dd", Locale.ENGLISH);
        SIMPLE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        TIME_HHMMSS = new SimpleDateFormat("hh:mm:ss", Locale.getDefault());
        TIME_AMPM = new SimpleDateFormat("HH:mm a", Locale.getDefault());
        FULL_DATETIME_FORMAT = new SimpleDateFormat("EEEE d MMM , yyyy", Locale.getDefault());
        DAY_FORMAT = new SimpleDateFormat("yyyy-MMMM-dd", Locale.getDefault());
        DAY_get = new SimpleDateFormat("dd", Locale.getDefault());
        GET_DAY_MONTH = new SimpleDateFormat("dd MMM", Locale.getDefault());
        DAYNAME_get = new SimpleDateFormat("EEEE", Locale.getDefault());
        DAY_getdate = new SimpleDateFormat("MMM, yyyy", Locale.getDefault());
        DAY_mydate = new SimpleDateFormat("MMM-d-yyyy", Locale.getDefault());
        USA_FORMAT = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        DateTime = new SimpleDateFormat("MMM dd,yyyy", Locale.ENGLISH);
        NewDateTime = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
        fullDate = new SimpleDateFormat("EEEE,MMMM dd,yyyy", Locale.getDefault());
        New_FORMAT = new SimpleDateFormat("yyyy-MM-d", Locale.getDefault());
        Only_month_german = new SimpleDateFormat("MMMM", Locale.forLanguageTag("de"));
        Only_month_cortion = new SimpleDateFormat("MMMM", Locale.forLanguageTag("hr"));
        Only_month_English = new SimpleDateFormat("MMMM", Locale.forLanguageTag("en"));
    }

    public static String getTodayTime(String dateTime) {
        String newFormat = null;
        Date date = null;
        try {
            date = NewFULL_DATE_FORMAT.parse(dateTime);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        newFormat = TIME_AMPM.format(date);
        // Log.e("### newFormat", "" + newFormat);
        return newFormat;
    }

    public static String getDayMonth(String dateTime) {
        String newFormat = null;
        Date date = null;
        try {
            date = NewFULL_DATE_FORMAT.parse(dateTime);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        newFormat = GET_DAY_MONTH.format(date);
        // Log.e("### newFormat", "" + newFormat);
        return newFormat;
    }
}