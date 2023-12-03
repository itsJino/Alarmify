package com.example.alarmify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class addAlarm extends AppCompatActivity {

    private EditText editAlarmTitle;
    private TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        editAlarmTitle = findViewById(R.id.editAlarmTitle);
        timePicker = findViewById(R.id.timePicker);

        Button okButton = findViewById(R.id.okButton);

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
                    minute = timePicker.getCurrentMinute();
                }

                if (!title.isEmpty()) {
                    // Create a new Alarm object using the input values
                    Alarm newAlarm = new Alarm(title, String.format("%02d:%02d", hour, minute), "Default Mission");

                    // TODO: Save the new alarm to your alarm list or perform required actions.
                    // For example, you could add it to the existing list of alarms or save it to a database.

                    // Provide feedback or navigate to the main activity
                    Toast.makeText(addAlarm.this, "New alarm created", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(addAlarm.this, MainActivity.class));
                } else {
                    // Prompt the user to enter a title
                    Toast.makeText(addAlarm.this, "Please enter a title", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
