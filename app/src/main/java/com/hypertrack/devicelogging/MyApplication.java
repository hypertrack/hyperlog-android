package com.hypertrack.devicelogging;

import android.app.Application;
import android.util.Log;

import com.hypertrack.devicelogger.db.SmartLog;

/**
 * Created by Aman on 04/10/17.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SmartLog.initialize(this);
        SmartLog.setLogLevel(Log.VERBOSE);
        SmartLog.setURL("https://requestb.in/1ggx8zp1");
    }
}
