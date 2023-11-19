package com.example.alarmify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ReminderActivity extends AppCompatActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = new TextView(this);
        textView.setText("Reminders");

        ListView listView = (ListView) findViewById(R.id.listAlarms);

        ImageButton openAlarmsView = findViewById(R.id.openAlarmsView);
        ImageButton openReminderView = findViewById(R.id.openReminderView);
        Button addAlarm = findViewById(R.id.addAlarm);

        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReminderActivity.this, addAlarm.class));
            }
        });

        openAlarmsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReminderActivity.this, MainActivity.class));
            }
        });

        openReminderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReminderActivity.this, ReminderActivity.class));
            }
        });

    }
}