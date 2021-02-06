
/*
The MIT License (MIT)

Copyright (c) 2015-2017 HyperTrack (http://hypertrack.com)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.hypertrack.hyperlog.utils;

import android.text.TextUtils;

import com.hypertrack.hyperlog.HyperLog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Aman on 04/10/17.
 */

public class HLDateTimeUtility {
    private static final String TAG = HLDateTimeUtility.class.getSimpleName();
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
            HyperLog.e("HYPERLOG", "Exception while getCurrentTime: " + e);
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
            HyperLog.e("HYPERLOG", "Exception while getFormattedTime: " + e);
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
            HyperLog.e("HYPERLOG", "Exception while getFormattedDate: " + e);
        }

        return new Date();
    }
}
