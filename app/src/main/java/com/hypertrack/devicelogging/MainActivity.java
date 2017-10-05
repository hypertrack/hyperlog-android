package com.hypertrack.devicelogging;

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

import com.android.volley.VolleyError;
import com.hypertrack.devicelogger.db.SmartLog;
import com.hypertrack.devicelogger.db.SmartLogCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    EditText editText;
    ListView listView;
    List<String> logsList = new ArrayList<>();
    ArrayAdapter listAdapter;
    int batchNumber = 1;
    int count = 0;
    String[] logs = new String[]{"Download Library","Library Downloaded","Initialize Library", "Library Initialized","Log Message", "Message Logged",
            "Create Log File","Log File Created","Push Logs to Server", "Logs Pushed to Server", "Logs Deleted", "Library Downloaded", "Library Initialized", "Message Logged",
            "Log File Created", "Logs Pushed to Server", "Logs Deleted"};
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.logText);
        listView = (ListView) findViewById(R.id.listView);
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, logsList);
        listView.setAdapter(listAdapter);
    }

    public void showLogs(View view) {
        logsList.clear();
        logsList.addAll(SmartLog.getDeviceLogsAsStringList(false));
        listAdapter.notifyDataSetChanged();
        batchNumber = 1;
    }

    public void addLog(View view) {
        if (!TextUtils.isEmpty(editText.getText().toString())) {
            SmartLog.i(TAG, editText.getText().toString());
            editText.getText().clear();
            editText.setText(logs[count++]);
            showLogs(view);
            showToast("Log Added");
        }
    }

    public void getFile(View view) {
        File file = SmartLog.getDeviceLogsInFile(this,false);
        if (file != null && file.exists())
            showToast("File Created at: " + file.getAbsolutePath());
    }

    public void deleteLogs(View view) {
        showToast("Logs deleted");
        SmartLog.deleteLogs();
        logsList.clear();
        listAdapter.notifyDataSetChanged();

    }

    public void nextLog(View view) {
        logsList.clear();
        logsList.addAll(SmartLog.getDeviceLogsAsStringList(false, ++batchNumber));
        listAdapter.notifyDataSetChanged();
    }

    public void pushLog(View view) {

        SmartLog.pushLogs(this, false, new SmartLogCallback() {
            @Override
            public void onSuccess(@NonNull String response) {
                showToast("Log Pushed");
                Log.d(TAG, "onSuccess: " + response);
                logsList.clear();
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(@NonNull VolleyError errorResponse) {
                showToast("Log Push Error");
                Log.d(TAG, "onError: ");
                errorResponse.printStackTrace();
            }
        });
    }
    private void showToast(String message){
        if(toast != null)
            toast.cancel();
        toast = Toast.makeText(this,message,Toast.LENGTH_SHORT);
        toast.show();
    }
}
