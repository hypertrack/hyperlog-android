package com.hypertrack.devicelogger.db.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Aman Jain on 26/09/17.
 */
public class CustomGson {

    private static class DateTypeAdapter implements JsonSerializer<Date>,
            JsonDeserializer<Date> {

        private final DateFormat mDateFormat1;
        private final DateFormat mDateFormat2;
        private final DateFormat mDateFormat3;
        private final DateFormat mDateFormat4;

        private DateTypeAdapter() {
            mDateFormat1 = new SimpleDateFormat(DateTimeUtility.HT_DATETIME_FORMAT_1, Locale.US);
            mDateFormat1.setTimeZone(TimeZone.getTimeZone(DateTimeUtility.HT_TIMEZONE_UTC));

            mDateFormat2 = new SimpleDateFormat(DateTimeUtility.HT_DATETIME_FORMAT_2, Locale.US);
            mDateFormat2.setTimeZone(TimeZone.getTimeZone(DateTimeUtility.HT_TIMEZONE_UTC));

            mDateFormat3 = new SimpleDateFormat(DateTimeUtility.HT_DATETIME_FORMAT_3, Locale.US);
            mDateFormat3.setTimeZone(TimeZone.getTimeZone(DateTimeUtility.HT_TIMEZONE_UTC));

            mDateFormat4 = new SimpleDateFormat(DateTimeUtility.HT_DATETIME_FORMAT_4, Locale.US);
            mDateFormat4.setTimeZone(TimeZone.getTimeZone(DateTimeUtility.HT_TIMEZONE_UTC));
        }

        @Override
        public synchronized JsonElement serialize(Date date, Type type,
                                                  JsonSerializationContext jsonSerializationContext) {
            synchronized (mDateFormat1) {
                String dateFormatAsString = mDateFormat1.format(date);
                return new JsonPrimitive(dateFormatAsString);
            }
        }

        @Override
        public synchronized Date deserialize(JsonElement jsonElement, Type type,
                                             JsonDeserializationContext jsonDeserializationContext) {
            try {
                synchronized (mDateFormat1) {
                    return mDateFormat1.parse(jsonElement.getAsString());
                }
            } catch (ParseException e1) {
                try {
                    synchronized (mDateFormat2) {
                        return mDateFormat2.parse(jsonElement.getAsString());
                    }
                } catch (ParseException e2) {
                    try {
                        synchronized (mDateFormat3) {
                            return mDateFormat3.parse(jsonElement.getAsString());
                        }
                    } catch (ParseException e3) {
                        try {
                            synchronized (mDateFormat4) {
                                return mDateFormat4.parse(jsonElement.getAsString());
                            }
                        } catch (Exception e4) {
                            throw new JsonSyntaxException(jsonElement.getAsString(), e4);
                        }
                    }
                }
            }
        }
    }

    public static Gson gson() {
        return new GsonBuilder().registerTypeAdapter(Date.class, new DateTypeAdapter()).create();
    }
}