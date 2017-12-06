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
package com.hypertrack.hyperlog_demo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.hypertrack.hyperlog.HyperLog;
import com.hypertrack.hyperlog.HLCallback;
import com.hypertrack.hyperlog.error.HLErrorResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Aman on 04/10/17.
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    EditText editText;
    ListView listView;
    List<String> logsList = new ArrayList<>();
    ArrayAdapter listAdapter;
    int batchNumber = 1;
    int count = 0;
    String[] logs = new String[]{"Download Library", "Library Downloaded", "Initialize Library", "Library Initialized", "Log Message", "Message Logged",
            "Create Log File", "Log File Created", "Push Logs to Server", "Logs Pushed to Server", "Logs Deleted", "Library Downloaded", "Library Initialized", "Message Logged",
            "Log File Created", "Logs Pushed to Server", "Logs Deleted"};
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Set Custom Log Message Format.
        HyperLog.setLogFormat(new CustomLogMessageFormat(this));
        editText = (EditText) findViewById(R.id.logText);
        listView = (ListView) findViewById(R.id.listView);
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, logsList);
        listView.setAdapter(listAdapter);
    }

    public void showLogs(View view) {
        logsList.clear();
        logsList.addAll(HyperLog.getDeviceLogsAsStringList(false));
        listAdapter.notifyDataSetChanged();
        batchNumber = 1;
    }

    public void addLog(View view) {
        if (!TextUtils.isEmpty(editText.getText().toString())) {
            HyperLog.i(TAG, editText.getText().toString());
            editText.getText().clear();
            editText.setText(logs[count++]);
            showLogs(view);
            showToast("Log Added");
        }
    }

    public void getFile(View view) {
        File file = HyperLog.getDeviceLogsInFile(this, false);
        if (file != null && file.exists())
            showToast("File Created at: " + file.getAbsolutePath());
    }

    public void deleteLogs(View view) {
        showToast("Logs deleted");
        HyperLog.deleteLogs();
        logsList.clear();
        listAdapter.notifyDataSetChanged();

    }

    public void nextLog(View view) {
        logsList.clear();
        logsList.addAll(HyperLog.getDeviceLogsAsStringList(false, ++batchNumber));
        listAdapter.notifyDataSetChanged();
    }

    public void pushLog(View view) {
        //Extra header to post request
        HashMap<String, String> params = new HashMap<>();
        params.put("timezone", TimeZone.getDefault().getID());

        HyperLog.pushLogs(this, params, true, new HLCallback() {
            @Override
            public void onSuccess(@NonNull Object response) {
                showToast("Log Pushed");
                Log.d(TAG, "onSuccess: " + response);
                logsList.clear();
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(@NonNull HLErrorResponse HLErrorResponse) {
                showToast("Log Push Error");
                Log.e(TAG, "onError: " + HLErrorResponse.getErrorMessage());
            }
        });
    }

    private void showToast(String message) {
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
