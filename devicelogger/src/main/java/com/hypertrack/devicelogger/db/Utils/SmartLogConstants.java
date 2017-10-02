package com.hypertrack.devicelogger.db.Utils;

import com.firebase.jobdispatcher.Constraint;

/**
 * Created by Aman on 02/10/17.
 */

public class SmartLogConstants {
    public static final String POST_DEVICE_LOG_TAG = "com.smartLog:PostDeviceLog";
    public static final int POST_DEVICE_LOG_JOB = 23;

    /**
     * Only run the job when the device is currently charging.
     */
    public static final int DEVICE_CHARGING = Constraint.DEVICE_CHARGING;
    /**
     * Only run the job when an unmetered network is available.
     */
    public static final int ON_UNMETERED_NETWORK = Constraint.ON_UNMETERED_NETWORK;

    /**
     * Only run the job when a network connection is available. If both this and {@link
     * #ON_UNMETERED_NETWORK} is provided, {@link #ON_UNMETERED_NETWORK} will take precedence.
     */
    public static final int ON_ANY_NETWORK = Constraint.ON_ANY_NETWORK;

    /**
     * Only run the job when the device is idle. This is ignored for devices that don't expose the
     * concept of an idle state.
     */
    public static final int DEVICE_IDLE = Constraint.DEVICE_IDLE;
}
