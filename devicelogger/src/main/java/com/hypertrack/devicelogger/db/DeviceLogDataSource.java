package com.hypertrack.devicelogger.db;

import java.util.List;

/**
 * Created by Aman on 22/08/16.
 */

interface DeviceLogDataSource {
    long getDeviceLogCount();

    void addDeviceLog(String deviceLog);

    void deleteDeviceLog(List<DeviceLog> deviceLogList);

    void deleteAllDeviceLogs();

    List<DeviceLog> getDeviceLogs(int batch);

    int getDeviceLogBatchCount();

    void clearOldLogs(int expiryTime);
}