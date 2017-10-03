package com.hypertrack.devicelogger.db;

import android.text.TextUtils;

import java.util.List;

/**
 * Created by Aman on 22/08/16.
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