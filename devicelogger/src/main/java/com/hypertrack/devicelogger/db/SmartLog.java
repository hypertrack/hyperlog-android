package com.hypertrack.devicelogger.db;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hypertrack.devicelogger.BuildConfig;
import com.hypertrack.devicelogger.db.Utils.DateTimeUtility;
import com.hypertrack.devicelogger.db.Utils.Utils;
import com.hypertrack.devicelogger.db.Utils.VolleyUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aman on 04/02/16.
 */
public class SmartLog {

    private static final String TAG = "SmartLog";
    private static int logLevel = Log.WARN;
    private static Context mContext;
    private static DeviceLogList mDeviceLogList;
    private static String URL;

    /**
     * Call this method to initialize SmartLog.
     */
    public static void initialize(Context context) {
        mContext = context.getApplicationContext();

        if (mDeviceLogList == null) {
            synchronized (SmartLog.class) {
                if (mDeviceLogList == null) {
                    DeviceLogDataSource logDataSource = DeviceLogDatabaseHelper.getInstance(context);
                    mDeviceLogList = new DeviceLogList(logDataSource);
                }
            }
        }
    }

    private static boolean isInitialize() {
        if (mContext == null) {
            SmartLog.e(TAG, "Smart Log isn't initialized.");
            return false;
        }

        if (mDeviceLogList == null)
            initialize(mContext);

        return true;
    }

    /**
     * Call this method to set a valid end point URL where logs need to be pushed.
     *
     * @param url URL of the endpoint
     */
    public static void setURL(String url) {
        URL = url;
    }

    /**
     * Sets the level of logging to display, where each level includes all those below it. The default
     * level is LOG_LEVEL_NONE. Please ensure this is set to Log#ERROR
     * or LOG_LEVEL_NONE before deploying your app to ensure no sensitive information is
     * logged. The levels are:
     * <ul>
     * <li>{@link Log#ASSERT}</li>
     * <li>{@link Log#VERBOSE}</li>
     * <li>{@link Log#DEBUG}</li>
     * <li>{@link Log#INFO}</li>
     * <li>{@link Log#WARN}</li>
     * <li>{@link Log#ERROR}</li>
     * </ul>
     *
     * @param logLevel The level of logcat logging that Parse should do.
     */
    public static void setLogLevel(int logLevel) {
        SmartLog.logLevel = logLevel;
    }

    public static void v(String tag, String message, Throwable tr) {
        if (Log.VERBOSE >= logLevel) {
            Log.v(tag, message + '\n' + Log.getStackTraceString(tr));
        }
    }

    public static void v(String tag, String message) {
        if (Log.VERBOSE >= logLevel) {
            Log.v(tag, message);
        }
    }

    public static void d(String tag, String message, Throwable tr) {
        if (Log.DEBUG >= logLevel) {
            Log.d(tag, message + '\n' + Log.getStackTraceString(tr));
        }
    }

    public static void d(String tag, String message) {
        if (Log.DEBUG >= logLevel) {
            Log.d(tag, message);
        }
    }

    public static void i(String tag, String message, Throwable tr) {
        if (Log.INFO >= logLevel) {
            Log.i(tag, message + '\n' + Log.getStackTraceString(tr));
        }

        r(getFormattedMessage(Log.INFO, message));
    }

    /**
     * Info log level that will store into DB.
     */
    public static void i(String tag, String message) {
        i(tag, message, null);
    }

    public static void w(String tag, String message, Throwable tr) {
        if (Log.WARN >= logLevel) {
            Log.w(tag, message + '\n' + Log.getStackTraceString(tr));
        }

        r(getFormattedMessage(Log.WARN, message));
    }

    public static void w(String tag, String message) {
        w(tag, message, null);
    }

    public static void e(String tag, String message, Throwable tr) {
        if (Log.ERROR >= logLevel) {
            Log.e(tag, message + '\n' + Log.getStackTraceString(tr));
        }

        r(getFormattedMessage(Log.ERROR, message));
    }

    public static void e(String tag, String message) {
        e(tag, message, null);
    }

    public static void exception(String tag, String message, Throwable tr) {
        if (Log.ERROR >= logLevel) {
            Log.e(tag, "**********************************************");
            Log.e(tag, "EXCEPTION: " + getMethodName() + ", " + message + '\n' + Log.getStackTraceString(tr));
            Log.e(tag, "**********************************************");
        }

        r(getFormattedMessage(Log.ERROR, "EXCEPTION: " + getMethodName() + ", " + message));
    }

    public static void exception(String tag, String message) {
        exception(tag, message, null);
    }

    public static void exception(String tag, Exception e) {
        if (e == null)
            return;

        exception(tag, e.getMessage(), null);
    }

    public static void a(String message) {
        r(getFormattedMessage(Log.ASSERT, message));
    }

    private static String getMethodName() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[1];//coz 0th will be getStackTrace so 1st
        return e.getMethodName();
    }

    private static String getFormattedMessage(int logLevel, String message) {
        return getLogPrefix() + getLogLevelName(logLevel) + message;
    }

    private static String getLogPrefix() {
        String timeStamp = DateTimeUtility.getCurrentTime();
        String senderName = BuildConfig.VERSION_NAME;
        String osVersion = "Android-" + Build.VERSION.RELEASE;

        String deviceUUID = "";

        if (mContext != null) {
            deviceUUID = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        }

        if (deviceUUID == null) {
            deviceUUID = "DeviceUUID";
        }

        return timeStamp + " " + senderName + " : " + osVersion + " | " + deviceUUID + " | ";
    }

    private static void r(final String message) {

        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!isInitialize())
                            return;

                        mDeviceLogList.addDeviceLog(message);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }).start();
        } catch (OutOfMemoryError | Exception e) {
            e.printStackTrace();
        }
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

    /**
     * Call this method to get a list of stored Device Logs
     *
     * @return List of {@link DeviceLog}
     */
    public static List<DeviceLog> getDeviceLogs() {
        return getDeviceLogs(true);
    }

    private static List<DeviceLog> getDeviceLogs(boolean deleteLogs) {
        if (!isInitialize())
            return null;

        List<DeviceLog> deviceLogs = new ArrayList<>();

        long count;

        do {
            List<DeviceLog> temp = mDeviceLogList.getDeviceLogs();
            deviceLogs.addAll(temp);
            if (deleteLogs)
                mDeviceLogList.clearDeviceLogs(temp);
            count = mDeviceLogList.count();
        } while (count > 5000 && deleteLogs);
        return deviceLogs;
    }

    /**
     * Call this method to get a list of stored Device Logs.
     * Device logs will delete from device after fetching.
     *
     * @return List of {@link String}
     */
    public static List<String> getDeviceLogsAsStringList() {
        return getDeviceLogsAsStringList(true);
    }

    /**
     * Call this method to get a list of stored Device Logs
     *
     * @param deleteLogs If true then delete the logs from the device.
     * @return List of {@link String}
     */
    public static List<String> getDeviceLogsAsStringList(boolean deleteLogs) {
        List<String> logsList = new ArrayList<>();
        if (!isInitialize())
            return logsList;

        if (!hasPendingDeviceLogs()) {
            return logsList;
        }

        List<DeviceLog> deviceLogList = new ArrayList<>();

        long count;

        do {
            List<DeviceLog> temp = mDeviceLogList.getDeviceLogs();
            deviceLogList.addAll(temp);
            if (deleteLogs)
                mDeviceLogList.clearDeviceLogs(temp);
            count = mDeviceLogList.count();
        } while (count > 5000 && deleteLogs);
        return getDeviceLogsAsStringList(deviceLogList);

    }

    /**
     * Method to get a list of stored Device Logs
     *
     * @param deviceLogList List of all device logs
     * @return List of {@link String}
     */
    private static List<String> getDeviceLogsAsStringList(List<DeviceLog> deviceLogList) {
        List<String> logsList = new ArrayList<>();

        if (deviceLogList == null) {
            return logsList;
        }

        for (DeviceLog deviceLog : deviceLogList) {
            logsList.add(deviceLog.getDeviceLog());
        }

        return logsList;
    }

    /**
     * Call this method to get a stored Device Logs as a File object.
     * A text file will create in the app folder containing all logs.
     *
     * @param fileName Name of the file and no need to provide the file extension
     * @return {@link File} object, or {@code null if there is not any logs in device.
     */
    public static File getDeviceLogsInFile(String fileName) {

        if (!isInitialize())
            return null;

        File file = null;
        List<String> stringList = getDeviceLogsAsStringList(true);

        if (TextUtils.isEmpty(fileName)) {
            fileName = DateTimeUtility.getCurrentTime() + ".txt";
        }

        if (stringList != null && !stringList.isEmpty()) {
            file = Utils.writeStringsToFile(mContext, stringList, fileName);
        }
        return file;
    }

    /**
     * Call this method to get a stored Device Logs as a File object.
     * A text file will create in the app folder containing all logs with the current date time as name of the file.
     *
     * @return {@link File} object
     */
    public static File getDeviceLogsInFile() {
        return getDeviceLogsInFile(null);
    }

    /**
     * Call this method to check whether any device logs are available.
     *
     * @return true If device has some pending logs otherwise false.
     */
    public static boolean hasPendingDeviceLogs() {
        if (!isInitialize())
            return false;

        long deviceLogsCount = mDeviceLogList.count();
        return deviceLogsCount > 0L;
    }

    /**
     * Call this method to push logs from device to the server.
     */
    public static void pushLogs() {
        pushLogs(null);
    }

    /**
     * Call this method to push logs from device to the server with custom filename.
     * Logs will get delete from the device once it successfully push to the server.
     *
     * @param fileName Name of the file that you want to receive on your server.
     */
    public static void pushLogs(String fileName) {

        if (!isInitialize())
            return;

        VolleyUtils.cancelPendingRequests(mContext, TAG);

        if (TextUtils.isEmpty(URL)) {
            SmartLog.e(TAG, "URL is missing. Please set the URL to push the logs.");
            return;
        }
        if (!hasPendingDeviceLogs())
            return;

        final List<DeviceLog> deviceLogs = getDeviceLogs(false);

        byte[] bytes = Utils.getByteData(getDeviceLogsAsStringList(deviceLogs));

        if (TextUtils.isEmpty(fileName)) {
            fileName = DateTimeUtility.getCurrentTime() + ".txt";
        }

        HTTPMultiPartPostRequest httpMultiPartPostRequest = new HTTPMultiPartPostRequest<String>(URL, bytes,
                fileName, mContext, String.class, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mDeviceLogList.clearDeviceLogs(deviceLogs);
                SmartLog.i(TAG, "Log has been pushed");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SmartLog.e(TAG, "Error has occured while log pushing.");
            }
        });
        VolleyUtils.addToRequestQueue(mContext, httpMultiPartPostRequest, TAG);
    }

    /**
     * Call this method to delete all logs from device.
     */
    public static void deleteLogs() {
        if (!isInitialize())
            return;

        mDeviceLogList.clearSavedDeviceLogs();
    }

    /**
     * Call this method to schedule log push smartly. SmartLog will push logs from device to server
     * when device is connected to internet.
     */

   /* public static void scheduleLogPush() {
        if (!isInitialize())
            return;
        // Create a new dispatcher using the Google Play driver.
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(mContext));
        Job myJob = dispatcher.newJobBuilder()
                .setService(SmartLogService.class) // the JobService that will be called
                .setTag(SmartLogConstants.POST_DEVICE_LOG_TAG)
                .setLifetime(Lifetime.FOREVER)
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setRecurring(true)
                .addConstraint(SmartLogConstants.ON_ANY_NETWORK)
                .build();

        dispatcher.mustSchedule(myJob);
    }*/

}