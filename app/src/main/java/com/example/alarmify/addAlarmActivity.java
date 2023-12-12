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

    // Extras used for passing data between activities
    public static final String EXTRA_ID = "com.example.alarmify.EXTRA_ID";
    public static final String EXTRA_ALARM_NAME = "com.example.alarmify.EXTRA_ALARM_NAME";
    public static final String EXTRA_ALARM_TIME = "com.example.alarmify.EXTRA_ALARM_TIME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        // Initialize UI elements
        editAlarmName = findViewById(R.id.editAlarmName);
        Button okButton = findViewById(R.id.okButton);
        Button selectTimeBtn = findViewById(R.id.selectTimeBtn);

        createNotificationChannel(); // Create a notification channel

        // Retrieve the Intent used to start this activity
        Intent intent = getIntent();

        // Check if the Intent contains an extra with the key EXTRA_ID
        if(intent.hasExtra(EXTRA_ID)) {
            // If the Intent contains the EXTRA_ID, retrieve the value associated with EXTRA_ALARM_NAME
            // and set the text of the editAlarmName EditText to the retrieved alarm name
            editAlarmName.setText(intent.getStringExtra(EXTRA_ALARM_NAME));
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve alarm name and time from user input
                String alarmName = editAlarmName.getText().toString();
                int hour, minute;

                hour = timePicker.getHour();
                minute = timePicker.getMinute();

                String alarmTime = String.format("%02d:%02d", hour, minute);

                // Validate user input
                if(alarmName.isEmpty() || alarmTime.isEmpty()) {
                    Toast.makeText(addAlarmActivity.this, "Please enter a title", Toast.LENGTH_SHORT).show();
                    return;
                }

                setAlarm(); // Set the alarm using AlarmManager
                saveAlarm(alarmName, alarmTime); // Save alarm data to ROOM Database

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

    // Show time picker dialog
    private void showTimePicker() {
        // Create a MaterialTimePicker dialog builder
        timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H) // Set the time format to 12-hour clock
                .setHour(12) // Set the default hour to 12
                .setMinute(0) // Set the default minute to 0
                .setTitleText("Select Alarm Time") // Set the title for the time picker dialog
                .build();

        // Show the time picker dialog using the FragmentManager and tag
        timePicker.show(getSupportFragmentManager(), "alarmify");

        // Add a listener to the "OK" button click event
        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When the "OK" button is clicked, create a Calendar instance
                calendar = Calendar.getInstance();

                // Set the Calendar instance with the selected hour and minute from the time picker
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                calendar.set(Calendar.MINUTE, timePicker.getMinute());
                calendar.set(Calendar.SECOND, 0); // Set seconds to 0 for precise timing
                calendar.set(Calendar.MILLISECOND, 0); // Set milliseconds to 0
            }
        });
    }

    @SuppressLint({"ScheduleExactAlarm"})
    // Set the alarm using AlarmManager
    private void setAlarm() {
        // Get the AlarmManager system service
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Create an intent to be sent when the alarm triggers
        Intent intent = new Intent(addAlarmActivity.this, AlarmReceiver.class);

        // Create a PendingIntent to be executed when the alarm is triggered
        // PendingIntent.FLAG_IMMUTABLE ensures that the PendingIntent cannot be modified later
        pendingIntent = PendingIntent.getBroadcast(addAlarmActivity.this,0,intent, PendingIntent.FLAG_IMMUTABLE);

        // Set a repeating alarm using RTC_WAKEUP, repeats every day
        // RTC_WAKEUP ensures the device wakes up when the alarm triggers
        // calendar.getTimeInMillis() specifies the time at which the alarm should start
        // AlarmManager.INTERVAL_DAY specifies the interval at which the alarm should repeat (every day)
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent);

        // Display a Toast message indicating that the alarm has been set
        Toast.makeText(addAlarmActivity.this, "Alarm Set", Toast.LENGTH_SHORT).show();
    }

    // Save alarm data to be passed back to the previous activity
    private void saveAlarm(String alarmName, String alarmTime) {
        // Create a new Intent to pass data back to the calling activity
        Intent data = new Intent();

        // Put extra data (alarm name and time) into the Intent
        data.putExtra(EXTRA_ALARM_NAME, alarmName);
        data.putExtra(EXTRA_ALARM_TIME, alarmTime);

        // Check if there's an existing ID passed from the calling activity
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if(id != -1) {
            // If an ID exists, put it as an extra in the Intent
            data.putExtra(EXTRA_ID, id);
        }

        // Set the result of the activity as OK along with the data to be sent back
        setResult(RESULT_OK, data);

        // Display a Toast message indicating that the alarm has been saved to the Room Database
        Toast.makeText(this, "Alarm has been saved to Room Database", Toast.LENGTH_SHORT).show();
    }

    // Reference: The following code is from Android example @https://developer.android.com/develop/ui/views/notifications/build-notification
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
    // Reference Complete
}
