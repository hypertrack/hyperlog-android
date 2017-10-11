
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
package com.hypertrack.devicelogger.db.Utils;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

import com.hypertrack.devicelogger.db.DeviceLog;
import com.hypertrack.devicelogger.db.HyperLog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Aman on 20/09/17.
 */

public class Utils {

    private static final double MEGA = (Math.pow(1024, 2));

    public static File writeStringsToFile(Context context, List<String> data, String fileName) {
        try {
            String dirPath = context.getExternalFilesDir(null).getAbsolutePath() + "/LogFiles";

            if (TextUtils.isEmpty(dirPath)) {
                HyperLog.e(TAG, "Error occurred while getting directory");
                return null;
            }

            //Create a directory if doesn't exist.
            File filePath = new File(dirPath);
            if (!filePath.exists()) {
                if (!filePath.mkdirs()) {
                    HyperLog.e(TAG, "Error occurred while creating file.");
                    return null;
                }
            }
            //Create a new file with file name
            File logFile = new File(filePath, fileName);
            FileWriter writer = new FileWriter(logFile,true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer, 4 * (int) MEGA);
            write(data, bufferedWriter);

            return logFile;

        } catch (Exception e) {
            HyperLog.exception(TAG, e);
        }
        return null;
    }

    private static void write(List<String> records, Writer writer) throws IOException {
        for (String record : records) {
            writer.write(record + "\n");
        }
        writer.flush();
        writer.close();
    }

    public static String getDeviceId(Context context) {
        String device_uuid = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return device_uuid != null ? device_uuid : "";
    }

    public static byte[] getByteData(List<DeviceLog> deviceLogs) {
        StringBuilder stringBuilder = new StringBuilder();
        for (DeviceLog deviceLog : deviceLogs) {
            stringBuilder.append(deviceLog.getDeviceLog()).append("\n");
        }
        return stringBuilder.toString().getBytes();
    }
}
