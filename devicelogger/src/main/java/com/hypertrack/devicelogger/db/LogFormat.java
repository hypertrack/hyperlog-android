package com.hypertrack.devicelogger.db;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.hypertrack.devicelogger.BuildConfig;
import com.hypertrack.devicelogger.db.Utils.DateTimeUtility;

/**
 * Created by Aman on 10/10/17.
 */
/**
 * This class can be overridden to customise the log message format.
 * <br>
 * An instance of LogFormat needs to be passed to the method
 * {@link HyperLog#setLogFormat(LogFormat)} as parameter.
 * */
public class LogFormat {

    private String deviceUUID;

    public LogFormat(Context context) {
        Context mContext = context.getApplicationContext();
        deviceUUID = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * Implement this method to override the default log message format.
     * @param logLevel The level of logcat logging that Parse should do.
     * @param message Log message that need to be customized.
     * @return Formatted Log Message that will store in database.
     * */
    public String getFormattedMessage(int logLevel, String message) {
        return getLogPrefix() + getLogLevelName(logLevel) + message;
    }

    private String getLogPrefix() {
        String timeStamp = DateTimeUtility.getCurrentTime();
        String senderName = BuildConfig.VERSION_NAME;
        String osVersion = "Android-" + Build.VERSION.RELEASE;

        if (deviceUUID == null) {
            deviceUUID = "DeviceUUID";
        }

        return timeStamp + " | " + senderName + " : " + osVersion + " | " + deviceUUID + " | ";
    }

    private static String getLogLevelName(int messageLogLevel) {

        String logLevelName;
        switch (messageLogLevel) {
            case Log.VERBOSE:
                logLevelName = "VERBOSE";
                break;
            case Log.DEBUG:
                logLevelName = "DEBUG";
                break;
            case Log.INFO:
                logLevelName = "INFO";
                break;
            case Log.WARN:
                logLevelName = "WARN";
                break;
            case Log.ERROR:
                logLevelName = "ERROR";
                break;
            case Log.ASSERT:
                logLevelName = "ASSERT";
                break;
            default:
                logLevelName = "NONE";
        }

        return "[" + logLevelName + "]" + ": ";
    }

}
