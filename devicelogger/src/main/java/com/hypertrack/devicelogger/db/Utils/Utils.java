package com.hypertrack.devicelogger.db.Utils;

import android.content.Context;
import android.provider.Settings;

import com.hypertrack.devicelogger.db.SmartLog;

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
            String dirPath = context.getExternalFilesDir(null).getAbsolutePath() + "/logsfile";

            File filePath = new File(dirPath);
            if (!filePath.exists())
                filePath.mkdirs();

            File logFile = new File(filePath, fileName);
            FileWriter writer = new FileWriter(logFile);
            BufferedWriter bufferedWriter = new BufferedWriter(writer, 4 * (int) MEGA);
            write(data, bufferedWriter);
            return logFile;
        } catch (IOException e) {
            SmartLog.exception(TAG, e);
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

    public static byte[] getByteData(List<String> records) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String record : records) {
            stringBuilder.append(record + "\n");
        }
        return stringBuilder.toString().getBytes();
    }
}
