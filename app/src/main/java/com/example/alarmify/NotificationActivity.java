package com.example.alarmify;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NotificationActivity extends AppCompatActivity implements StepCountListener{

    private SensorManager sensorManager;
    private AccelerometerEventListener accelerometerEventListener;
    private TextView textViewStepCount;
    private boolean isAlarmActive = false;
    private static final int ALARM_REQUEST_CODE = 123;
    private static final int TOTAL_STEPS = 20; // Define the total steps for completion
    private ProgressBar progressBar;
    private Button closeNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // Initialize UI elements
        textViewStepCount = findViewById(R.id.textViewStepCount);
        progressBar = (ProgressBar)findViewById(R.id.progressBarSteps);
        closeNotification = findViewById(R.id.closeNotification);

        // Set up sensor manager and event listener
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometerEventListener = new AccelerometerEventListener(sensorManager, this);

        // Initialize progress bar
        progressBar.setProgress(0);
        progressBar.setMax(TOTAL_STEPS); // Set the maximum progress for the ProgressBar

        // Button click listener for closing notification
        closeNotification.setOnClickListener(v -> {
            if (progressBar.getProgress() >= TOTAL_STEPS) {
                // Perform close notification action
                finish();
            } else {
                // Show a toast message indicating the remaining steps to close
                Toast.makeText(NotificationActivity.this, "Complete " + TOTAL_STEPS + " steps to close", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Register sensor listener when the activity is resumed
    @Override
    protected void onResume() {
        super.onResume();
        accelerometerEventListener.register();
    }

    // Unregister sensor listener when the activity is paused
    @Override
    protected void onPause() {
        super.onPause();
        accelerometerEventListener.unregister();
    }

    // Method to update the UI with step count
    public void updateUI(int steps) {
        textViewStepCount.setText("Steps: " + steps);
    }

    @Override
    // Listener method to handle step count changes
    public void onStepCount(int steps) {
        // Update the UI with step count
        runOnUiThread(() -> {
            textViewStepCount.setText("Steps: " + steps);
            updateProgressBar(steps);
            updateCloseButtonState(steps);
        });
    }

    // Update progress bar with current step count
    private void updateProgressBar(int steps) {
        if (steps <= TOTAL_STEPS) {
            progressBar.setProgress(steps); // Update the ProgressBar with current steps
        } else {
            progressBar.setProgress(TOTAL_STEPS); // Cap the progress at the total steps
        }
    }

    // Update state of close button based on step count
    private void updateCloseButtonState(int steps) {
        if (steps >= TOTAL_STEPS) {
            closeNotification.setEnabled(true); // Enable the button if steps reached total steps
        } else {
            closeNotification.setEnabled(false); // Disable the button if steps are not yet reached
        }
    }
}
