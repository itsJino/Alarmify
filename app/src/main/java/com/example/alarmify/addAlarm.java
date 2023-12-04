package com.example.alarmify;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class addAlarm extends AppCompatActivity {

    private EditText editAlarmTitle;
    private TimePicker timePicker;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        editAlarmTitle = findViewById(R.id.editAlarmTitle);
        timePicker = findViewById(R.id.timePicker);

        Button okButton = findViewById(R.id.okButton);

        createNotificationChannel();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editAlarmTitle.getText().toString();
                int hour, minute;

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                } else {
                    hour = 12;
                    minute = 30;
                }

                if (!title.isEmpty()) {
                    Alarm newAlarm = new Alarm(title, String.format("%02d:%02d", hour, minute), "Default Mission");
                    com.example.alarmify.MainActivity.alarms.add(newAlarm);

                    Toast.makeText(addAlarm.this, "Alarm added Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(addAlarm.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(addAlarm.this, "Please enter a title", Toast.LENGTH_SHORT).show();
                }

                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                calendar.set(Calendar.MINUTE, timePicker.getMinute());
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(addAlarm.this, AlarmReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(addAlarm.this,0, intent, 0);
                Toast.makeText(addAlarm.this, "Alarm Set", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "akchannel";
            String desc = "Channel for Alarm Manager";
            int imp = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = null;
            channel = new NotificationChannel("androidknowledge", name, imp);
            channel.setDescription(desc);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }
}
