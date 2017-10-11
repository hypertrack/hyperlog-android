
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
package com.hypertrack.hyperlog;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.hypertrack.hyperlog.utils.DateTimeUtility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Aman on 20/09/17.
 */

class DeviceLogTable {

    private static final String TAG = DeviceLogTable.class.getSimpleName();
    private static final int DEVICE_LOG_REQUEST_QUERY_LIMIT = 5000;

    private static final String TABLE_NAME = "device_logs";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_DEVICE_LOG = "device_log";

    private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME
            + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_DEVICE_LOG + " TEXT"
            + ");";

    static void onCreate(SQLiteDatabase db) {
        if (db == null) {
            return;
        }

        try {
            db.execSQL(DATABASE_CREATE);
        } catch (Exception e) {
            e.printStackTrace();
            HyperLog.e(TAG, "DeviceLogTable: Exception occurred while onCreate: " + e);
        }
    }

    static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (db == null) {
            return;
        }

        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);

            HyperLog.i(TAG, "DeviceLogTable onUpgrade called. Executing drop_table query to clear old logs.");
        } catch (Exception e) {
            e.printStackTrace();
            HyperLog.e(TAG, "DeviceLogTable: Exception occurred while onUpgrade: " + e);
        }
    }

    static long getCount(SQLiteDatabase db) {
        try {
            if (db == null) {
                return 0;
            }

            return DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
            HyperLog.e(TAG, "DeviceLogTable: Exception occurred while getCount: " + e);
            return 0L;
        }
    }

    static int getDeviceLogBatchCount(SQLiteDatabase db) {
        try {
            if (db == null) {
                return 0;
            }

            return (int) Math.ceil(getCount(db) * 1.0f / DEVICE_LOG_REQUEST_QUERY_LIMIT);

        } catch (Exception e) {
            e.printStackTrace();
            HyperLog.e(TAG, "DeviceLogTable: Exception occurred while getDeviceLogBatchCount: " + e);
            return 0;
        }
    }

    static void addDeviceLog(SQLiteDatabase db, String deviceLog) {
        if (db == null || TextUtils.isEmpty(deviceLog)) {
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DEVICE_LOG, deviceLog);

        try {
            db.insert(TABLE_NAME, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
            HyperLog.e(TAG, "DeviceLogTable: Exception occurred while addDeviceLog: " + e);
        }
    }

    static void deleteDeviceLog(SQLiteDatabase db, List<DeviceLogModel> deviceLogList) {
        if (db == null)
            return;

        StringBuilder builder = new StringBuilder();
        for (DeviceLogModel deviceLog : deviceLogList) {
            if (deviceLog != null && deviceLog.getId() > 0) {
                builder.append(deviceLog.getId())
                        .append(",");
            }
        }

        if (builder.length() == 0) {
            return;
        }

        try {
            String ids = builder.toString();
            ids = ids.substring(0, ids.length() - 1);

            String whereClause = COLUMN_ID +
                    " IN (" +
                    ids +
                    ")";

            db.delete(TABLE_NAME, whereClause, null);
        } catch (Exception e) {
            e.printStackTrace();
            HyperLog.e(TAG, "DeviceLogTable: Exception occurred while deleteDeviceLog: " + e);
        }
    }

    static void deleteAllDeviceLogs(SQLiteDatabase db) {
        if (db == null) {
            return;
        }

        try {
            db.delete(TABLE_NAME, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            HyperLog.e(TAG, "DeviceLogTable: Exception occurred while deleteAllDeviceLogs: " + e);
        }
    }

    static List<DeviceLogModel> getDeviceLogs(SQLiteDatabase db, int batch) {
        if (db == null) {
            return null;
        }

        int count = getDeviceLogBatchCount(db);
        batch--;
        if (count <= 1 || batch < 0) {
            batch = 0;
        }

        ArrayList<DeviceLogModel> deviceLogList = null;

        String limit = String.valueOf(batch * DEVICE_LOG_REQUEST_QUERY_LIMIT) + ", " + String.valueOf(DEVICE_LOG_REQUEST_QUERY_LIMIT);

        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ID, COLUMN_DEVICE_LOG}, null, null,
                null, null, null, limit);

        if (cursor == null || cursor.isClosed()) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                deviceLogList = new ArrayList<>();
                do {
                    if (cursor.isClosed()) {
                        break;
                    }

                    String deviceLogString = cursor.getString(1);
                    if (!TextUtils.isEmpty(deviceLogString)) {
                        DeviceLogModel deviceLog = new DeviceLogModel(deviceLogString);

                        // Get RowId for DeviceLogModel
                        Integer rowId = Integer.valueOf(cursor.getString(0));
                        deviceLog.setId(rowId != null ? rowId : 0);

                        deviceLogList.add(deviceLog);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            HyperLog.e(TAG, "DeviceLogTable: Exception occurred while getDeviceLogs: " + e);
        } finally {
            cursor.close();
        }

        return deviceLogList;
    }

    public static void clearOldLogs(SQLiteDatabase db, int expiryTimeInSeconds) {
        if (db == null) {
            return;
        }

        try {
            Calendar calendar = Calendar.getInstance();
            //Set the calendar time to older time.
            calendar.add(Calendar.SECOND, -expiryTimeInSeconds);

            String date = DateTimeUtility.getFormattedTime(calendar.getTime());

            db.delete(TABLE_NAME, COLUMN_DEVICE_LOG + "<?", new String[]{date});

        } catch (Exception e) {
            e.printStackTrace();
            HyperLog.e(TAG, "DeviceLogTable: Exception occurred while deleteAllDeviceLogs: " + e);
        }
    }
}