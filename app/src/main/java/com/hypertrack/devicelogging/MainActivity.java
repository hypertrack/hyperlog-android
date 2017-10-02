package com.hypertrack.devicelogging;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.hypertrack.devicelogger.db.SmartLog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    EditText editText;
    ListView listView;
    List<String> logsList = new ArrayList<>();
    ArrayAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SmartLog.initialize(this);
        SmartLog.setLogLevel(Log.VERBOSE);
        editText = (EditText) findViewById(R.id.logText);
        listView = (ListView) findViewById(R.id.listView);
        logsList.add("Test");
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, logsList);
        listView.setAdapter(listAdapter);
    }

    public void showLogs(View view) {
        logsList.clear();
        logsList.addAll(SmartLog.getDeviceLogsAsStringList(false));
            listAdapter.notifyDataSetChanged();
    }

    public void addLog(View view) {
        if (!TextUtils.isEmpty(editText.getText().toString())) {
            SmartLog.i(TAG, editText.getText().toString());
        }
    }

    public void getFile(View view) {
        File file = SmartLog.getDeviceLogsInFile();
        if (file != null && file.exists())
            Toast.makeText(this, file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
    }

    public void deleteLogs(View view) {
        SmartLog.deleteLogs();
    }
}
