package com.example.alarmify;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

public class addAlarmActivity extends AppCompatActivity {
    private EditText editAlarmName;
    private MaterialTimePicker timePicker;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    public static final String EXTRA_ID = "com.example.alarmify.EXTRA_ID";
    public static final String EXTRA_ALARM_NAME = "com.example.alarmify.EXTRA_ALARM_NAME";
    public static final String EXTRA_ALARM_TIME = "com.example.alarmify.EXTRA_ALARM_TIME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        editAlarmName = findViewById(R.id.editAlarmName);
        Button okButton = findViewById(R.id.okButton);
        Button selectTimeBtn = findViewById(R.id.selectTimeBtn);

        createNotificationChannel();

        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_ID)) {
            editAlarmName.setText(intent.getStringExtra(EXTRA_ALARM_NAME));
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String alarmName = editAlarmName.getText().toString();
                int hour, minute;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                } else {
                    hour = 12;
                    minute = 0;
                }

                String alarmTime = String.format("%02d:%02d", hour, minute);

                if(alarmName.isEmpty() || alarmTime.isEmpty()) {
                    Toast.makeText(addAlarmActivity.this, "Please enter a title", Toast.LENGTH_SHORT).show();
                    return;
                }

                setAlarm();
                saveAlarm(alarmName, alarmTime);

                Toast.makeText(addAlarmActivity.this, "Alarm added Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        selectTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });
    }

    private void showTimePicker() {
        timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Alarm Time")
                .build();

        timePicker.show(getSupportFragmentManager(), "alarmify");

        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                calendar.set(Calendar.MINUTE, timePicker.getMinute());
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
            }
        });
    }

    @SuppressLint({"ScheduleExactAlarm"})
    private void setAlarm() {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(addAlarmActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(addAlarmActivity.this,0,intent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Toast.makeText(addAlarmActivity.this, "Alarm Set", Toast.LENGTH_SHORT).show();
    }

    private void saveAlarm(String alarmName, String alarmTime) {
        Intent data = new Intent();

        data.putExtra(EXTRA_ALARM_NAME, alarmName);
        data.putExtra(EXTRA_ALARM_TIME, alarmTime);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if(id != -1) {
            data.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, data);
        Toast.makeText(this, "Alarm has been saved to Room Database", Toast.LENGTH_SHORT).show();
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "alarmifychannel";
            String desc = "Channel for Alarm Manager";
            int imp = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("alarmify", name, imp);
            channel.setDescription(desc);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
