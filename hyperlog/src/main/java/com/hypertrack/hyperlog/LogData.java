package com.hypertrack.hyperlog;

public class LogData {
    final String logLevelName;
    final String tag;
    final String message;
    final String timeStamp;
    final String senderName;
    final String osVersion;
    final String deviceUUID;

    public LogData(
            String logLevelName,
            String tag,
            String message,
            String timeStamp,
            String senderName,
            String osVersion,
            String deviceUUID) {
        this.logLevelName = logLevelName;
        this.tag = tag;
        this.message = message;
        this.timeStamp = timeStamp;
        this.senderName = senderName;
        this.osVersion = osVersion;
        this.deviceUUID = deviceUUID;
    }
}
