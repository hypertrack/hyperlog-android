package com.hypertrack.devicelogging;

import android.content.Context;

import com.hypertrack.devicelogger.db.LogFormat;

class CustomLogMessageFormat extends LogFormat {

    CustomLogMessageFormat(Context context) {
        super(context);
    }

    @Override
    public String getFormattedLogMessage(String logLevelName, String message, String timeStamp,
                                         String senderName, String osVersion, String deviceUUID) {
        String customLogFormat = timeStamp + " : " + logLevelName + " : " + deviceUUID + " : " + message;
        return customLogFormat;
    }
}