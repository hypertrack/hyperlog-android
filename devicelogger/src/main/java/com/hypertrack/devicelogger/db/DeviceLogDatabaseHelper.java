
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
package com.hypertrack.devicelogger.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

/**
 * Created by Aman on 20/09/17.
 */
class DeviceLogDatabaseHelper extends SQLiteOpenHelper implements DeviceLogDataSource {

    private static final String TAG = DeviceLogDatabaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "com.hypertrack.common.device_logs.db";
    private static final int DATABASE_VERSION = 2;

    private static DeviceLogDatabaseHelper deviceLogDatabaseHelper;
    private SQLiteDatabase database;

    private DeviceLogDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.initializeDatabase();
    }

    private void initializeDatabase() {
        if (database == null)
            database = this.getWritableDatabase();
    }

    static DeviceLogDatabaseHelper getInstance(Context context) {
        if (deviceLogDatabaseHelper == null) {
            synchronized (DeviceLogDatabaseHelper.class) {
                if (deviceLogDatabaseHelper == null)
                    deviceLogDatabaseHelper = new DeviceLogDatabaseHelper(context);
            }
        }
        return deviceLogDatabaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DeviceLogTable.onCreate(db);
        SmartLog.i(TAG, "DeviceLogDatabaseHelper onCreate called.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        DeviceLogTable.onUpgrade(db, oldVersion, newVersion);
        SmartLog.i(TAG, "DeviceLogDatabaseHelper onUpgrade called.");
    }

    @Override
    public long getDeviceLogCount() {
        // Initialize SQLiteDatabase if null
        initializeDatabase();

        return DeviceLogTable.getCount(database);
    }

    @Override
    public void addDeviceLog(String deviceLog) {
        // Initialize SQLiteDatabase if null
        initializeDatabase();

        DeviceLogTable.addDeviceLog(database, deviceLog);
    }

    @Override
    public void deleteDeviceLog(List<DeviceLog> deviceLogList) {
        // Initialize SQLiteDatabase if null
        initializeDatabase();

        DeviceLogTable.deleteDeviceLog(database, deviceLogList);
    }

    @Override
    public void deleteAllDeviceLogs() {
        // Initialize SQLiteDatabase if null
        initializeDatabase();

        DeviceLogTable.deleteAllDeviceLogs(database);
    }

    @Override
    public List<DeviceLog> getDeviceLogs(int batch) {
        // Initialize SQLiteDatabase if null
        initializeDatabase();
        List<DeviceLog> deviceLogList = null;

        try {
            deviceLogList = DeviceLogTable.getDeviceLogs(database,batch);
        } catch (OutOfMemoryError | Exception e) {
            e.printStackTrace();
        }

        return deviceLogList;
    }

    @Override
    public int getDeviceLogBatchCount() {
        initializeDatabase();

        return DeviceLogTable.getDeviceLogBatchCount(database);
    }

    @Override
    public void clearOldLogs(int expiryTime) {
        initializeDatabase();

        DeviceLogTable.clearOldLogs(database,expiryTime);
    }
}
