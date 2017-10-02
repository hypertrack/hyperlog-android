package com.hypertrack.devicelogger.db.Utils;

import android.text.TextUtils;

import com.hypertrack.devicelogger.db.SmartLog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeUtility {
    private static final String TAG = DateTimeUtility.class.getSimpleName();
    public static final String HT_DATETIME_FORMAT_1 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String HT_DATETIME_FORMAT_2 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String HT_DATETIME_FORMAT_3 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZZZ";
    public static final String HT_DATETIME_FORMAT_4 = "yyyy-MM-dd'T'HH:mm:ssZZZ";
    public static final String HT_TIMEZONE_UTC = "UTC";

    public static String getCurrentTime() {
        String currentTime;
        try {
            // Quoted "Z" to indicate UTC, no timezone offset
            SimpleDateFormat dateFormat = new SimpleDateFormat(HT_DATETIME_FORMAT_1, Locale.US);
            dateFormat.setTimeZone(TimeZone.getTimeZone(HT_TIMEZONE_UTC));
            currentTime = dateFormat.format(new Date());
        } catch (Exception e) {
            SmartLog.e(TAG, "Exception while getCurrentTime: " + e);
            currentTime = "";
        }
        return currentTime != null ? currentTime : "";
    }

    public static String getFormattedTime(Date date) {
        if (date == null)
            return null;

        try {
            // Quoted "Z" to indicate UTC, no timezone offset
            SimpleDateFormat dateFormat = new SimpleDateFormat(HT_DATETIME_FORMAT_1, Locale.US);
            dateFormat.setTimeZone(TimeZone.getTimeZone(HT_TIMEZONE_UTC));
            return dateFormat.format(date);
        } catch (Exception e) {
            SmartLog.e(TAG, "Exception while getFormattedTime: " + e);
        }

        return getCurrentTime();
    }

    public static Date getFormattedDate(String time) {
        if (TextUtils.isEmpty(time))
            return null;

        try {
            DateFormat format = new SimpleDateFormat(HT_DATETIME_FORMAT_1, Locale.US);
            format.setTimeZone(TimeZone.getTimeZone(HT_TIMEZONE_UTC));
            return format.parse(time);
        } catch (Exception e) {
            SmartLog.e(TAG, "Exception while getFormattedDate: " + e);
        }

        return new Date();
    }
}
