package com.hypertrack.devicelogging;

import android.content.Context;

import com.hypertrack.devicelogger.db.LogFormat;
import com.hypertrack.devicelogger.db.Utils.DateTimeUtility;

class CustomLog extends LogFormat {

    CustomLog(Context context) {
        super(context);
    }

    @Override
    public String getFormattedMessage(int logLevel, String message) {
        return DateTimeUtility.getCurrentTime() + "| " + message;
    }
}