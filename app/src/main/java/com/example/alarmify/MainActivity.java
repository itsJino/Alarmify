package com.example.alarmify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private String name[] = {
            "Work",
            "College",
            "Leave for Work",
            "Work",
            "College",
            "Leave for Work",
            "Work",
            "College",
            "Leave for Work",
    };

    private String alarmTime[] = {
            "10:50 AM",
            "13:40 PM",
            "08:20 AM",
            "09:15 AM",
            "14:30 PM",
            "08:20 AM",
            "09:15 AM",
            "14:30 PM",
            "08:20 AM",
    };

    private String mission[] = {
            "Maths Question",
            "Code Question",
            "QR Code",
            "Maths Question",
            "Code Question",
            "QR Code",
            "Maths Question",
            "Code Question",
            "QR Code",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listAlarms);

        ImageButton openAlarmsView = findViewById(R.id.openAlarmsView);
        ImageButton openReminderView = findViewById(R.id.openReminderView);

        AlarmList alarmList = new AlarmList(this, alarmTime, name, mission);
        listView.setAdapter(alarmList);

        openAlarmsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });

        openReminderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });

    }
}