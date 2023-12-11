package com.example.alarmify;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        quoteTextView = findViewById(R.id.quoteTextView);
        authorTextView = findViewById(R.id.authorTextView);
        openMainActivityButton = findViewById(R.id.openMainActivityButton);
        stepsSwitch = findViewById(R.id.stepsSwitch);
        jumpingSwitch = findViewById(R.id.jumpingSwitch);

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

    private void setStepsAlarm() {

    }

    private void setJumpingAlarm() {
        // Implement logic to set an alarm for jumping using AlarmManager
        // ...
    }

    private void cancelStepsAlarm() {
        // Implement logic to cancel the steps alarm using AlarmManager
        // ...
    }

    private void cancelJumpingAlarm() {
        // Implement logic to cancel the jumping alarm using AlarmManager
        // ...
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
}