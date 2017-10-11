package com.hypertrack.devicelogging;

import android.app.Application;
import android.util.Log;

import com.hypertrack.devicelogger.db.HyperLog;

/**
 * Created by Aman on 04/10/17.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        HyperLog.initialize(this,new CustomLog(this));
        HyperLog.setLogLevel(Log.VERBOSE);
        HyperLog.setURL("https://requestb.in/1ggx8zp1");
    }
}
