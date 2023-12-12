package com.example.alarmify;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class ReminderActivity extends AppCompatActivity {
    private TextView quoteTextView, authorTextView;
    private ImageButton openMainActivityButton;
    private List<Quote> quotes;

    private Switch stepsSwitch;
    private Switch jumpingSwitch;

    private static final String QUOTE_PREF = "quote_of_the_day";
    private static final String AUTHOR_PREF = "author_of_the_day";

    private SharedPreferences sharedPreferences;

    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent stepsPendingIntent;
    private PendingIntent jumpingPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        quoteTextView = findViewById(R.id.quoteTextView);
        authorTextView = findViewById(R.id.authorTextView);
        openMainActivityButton = findViewById(R.id.openMainActivityButton);
        stepsSwitch = findViewById(R.id.stepsSwitch);
        jumpingSwitch = findViewById(R.id.jumpingSwitch);



        createNotificationChannel();

        quotes = new ArrayList<>();
        quotes = readQuotesFromCSV();

        Quote randomQuote = getRandomQuote();
        if (randomQuote != null) {
            quoteTextView.setText(randomQuote.getQuoteText());
            authorTextView.setText((randomQuote.getAuthor()));
        } else {
            quoteTextView.setText("No quotes available"); // Handle case when quoteList is empty or null
        }

        openMainActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReminderActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        stepsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // If the steps switch is turned on, set an alarm for steps
                setStepsAlarm();
            } else {
                // If the steps switch is turned off, cancel the steps alarm
                cancelStepsAlarm();
            }
        });

        jumpingSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // If the jumping switch is turned on, set an alarm for jumping
                setJumpingAlarm();
            } else {
                // If the jumping switch is turned off, cancel the jumping alarm
                cancelJumpingAlarm();
            }
        });
    }

    @SuppressLint("ScheduleExactAlarm")
    private void setStepsAlarm() {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent stepsIntent = new Intent(ReminderActivity.this, ReminderReceiver.class);
        stepsPendingIntent = PendingIntent.getBroadcast(ReminderActivity.this, 0, stepsIntent, PendingIntent.FLAG_IMMUTABLE);

        long intervalMillis = 30 * 60 * 1000; // 30 minutes in milliseconds
        long currentTime = System.currentTimeMillis();
        long alarmTime = currentTime + intervalMillis;


        // Set a repeating alarm that triggers every 30 minutes
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, intervalMillis, stepsPendingIntent);

        // Calculate the time when the alarm is set for
        long alarmSetForTime = alarmTime - currentTime;
        int hours = (int) (alarmSetForTime / (1000 * 60 * 60));
        int minutes = (int) ((alarmSetForTime / (1000 * 60)) % 60);

        String toastMessage;
        if (hours > 0) {
            toastMessage = "Reminder set for " + hours + " hours and " + minutes + " minutes from now";
        } else {
            toastMessage = "Reminder set for " + minutes + " minutes from now";
        }

        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }

    private void setJumpingAlarm() {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent jumpingIntent = new Intent(ReminderActivity.this, ReminderReceiver.class);
        jumpingPendingIntent = PendingIntent.getBroadcast(ReminderActivity.this, 0, jumpingIntent, PendingIntent.FLAG_IMMUTABLE);

        long intervalMillis = 30 * 60 * 1000; // 30 minutes in milliseconds
        long currentTime = System.currentTimeMillis();
        long alarmTime = currentTime + intervalMillis;


        // Set a repeating alarm that triggers every 30 minutes
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, intervalMillis, jumpingPendingIntent);

        // Calculate the time when the alarm is set for
        long alarmSetForTime = alarmTime - currentTime;
        int hours = (int) (alarmSetForTime / (1000 * 60 * 60));
        int minutes = (int) ((alarmSetForTime / (1000 * 60)) % 60);

        String toastMessage;
        if (hours > 0) {
            toastMessage = "Reminder set for " + hours + " hours and " + minutes + " minutes from now";
        } else {
            toastMessage = "Reminder set for " + minutes + " minutes from now";
        }

        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }

    private void cancelStepsAlarm() {
        alarmManager.cancel(stepsPendingIntent);
    }

    private void cancelJumpingAlarm() {
        alarmManager.cancel(jumpingPendingIntent);
    }

    private List<Quote> readQuotesFromCSV() {
        try {
            InputStream is = getAssets().open("quotes.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1); // Split the line by comma
                if (parts.length >= 2) {
                    String author = parts[0];
                    String quote = parts[1];
                    Quote newQuote = new Quote(author, quote);
                    quotes.add(newQuote);
                }
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return quotes;
    }

    private Quote getRandomQuote() {
        if (quotes != null && !quotes.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(quotes.size()); // Generate a random index

            return quotes.get(randomIndex); // Return the Quote object at the random index
        } else {
            return null; // Handle case when quoteList is empty or null
        }
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "alarmifychannelReminder";
            String desc = "Channel for Alarm Manager";
            int imp = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("alarmifyReminder", name, imp);
            channel.setDescription(desc);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}