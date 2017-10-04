
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

import android.text.TextUtils;

import java.util.List;

/**
 * Created by Aman on 22/09/17.
 */
class DeviceLogList {
    private DeviceLogDataSource mDeviceLogDataSource;

    DeviceLogList(DeviceLogDataSource mDeviceLogDataSource) {
        this.mDeviceLogDataSource = mDeviceLogDataSource;
    }

    void addDeviceLog(String deviceLog) {
        if (TextUtils.isEmpty(deviceLog)) {
            return;
        }

        this.mDeviceLogDataSource.addDeviceLog(deviceLog);
    }

    void clearSavedDeviceLogs() {
        this.mDeviceLogDataSource.deleteAllDeviceLogs();
    }

    List<DeviceLog> getDeviceLogs(int batch) {
        return this.mDeviceLogDataSource.getDeviceLogs(batch);
    }

    void clearDeviceLogs(List<DeviceLog> pushedDeviceLogs) {
        if (pushedDeviceLogs == null || pushedDeviceLogs.isEmpty())
            return;

        this.mDeviceLogDataSource.deleteDeviceLog(pushedDeviceLogs);
    }

    long count() {
        return this.mDeviceLogDataSource.getDeviceLogCount();
    }

    int getDeviceLogBatchCount(){
        return this.mDeviceLogDataSource.getDeviceLogBatchCount();
    }

    void clearOldLogs(int expiryTime) {
        mDeviceLogDataSource.clearOldLogs(expiryTime);
    }
}