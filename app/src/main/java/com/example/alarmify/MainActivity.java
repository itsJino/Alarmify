package com.example.alarmify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<Alarm> alarms = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = new TextView(this);
        textView.setText("Alarms");

        ListView listView = findViewById(R.id.listAlarms);

        ImageButton openAlarmsView = findViewById(R.id.openAlarmsView);
        ImageButton openReminderView = findViewById(R.id.openReminderView);
        Button addAlarm = findViewById(R.id.addAlarm);

        initializeAlarms(); // Method to initialize alarms ArrayList

        AlarmList alarmList = new AlarmList(this, alarms);
        listView.setAdapter(alarmList);

        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, addAlarm.class));
            }
        });

        openAlarmsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // startActivity(new Intent(MainActivity.this, MainActivity.class)); // This might be an incorrect intent
                // Perhaps you intended to go to a different activity here
            }
        });

        openReminderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ReminderActivity.class));
            }
        });
    }

    private void initializeAlarms() {
        // Populate the alarms ArrayList
        alarms.add(new Alarm("Work", "10:50 AM", "Maths Question"));
        alarms.add(new Alarm("College", "13:40 PM", "Code Question"));
        alarms.add(new Alarm("Leave for Work", "08:20 AM", "QR Code"));
        // Add more alarms as needed
    }
}
