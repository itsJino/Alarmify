package com.example.alarmify;

public class StepCounter {
    // Constants for step detection
    private static final int STEP_THRESHOLD = 10; // Threshold for detecting steps
    private static final int STEP_DELAY_NS = 250000000; // Delay between steps (0.25 seconds)

    // Variables for step counting logic
    private boolean isPeak = false; // Flag to indicate a potential step peak
    private long lastStepTimestamp = 0; // Timestamp of the last detected step
    private int cumulativeSteps = 0; // Maintain the cumulative step count

    // Method to count steps based on accelerometer values
    public int countSteps(float[] values) {
        float maxValue = 0;

        // Find the maximum value among accelerometer sensor readings
        for (float value : values) {
            if (Math.abs(value) > maxValue) {
                maxValue = Math.abs(value);
            }
        }

        // Check if the maximum value exceeds the defined threshold
        if (maxValue > STEP_THRESHOLD) {
            long currentTime = System.nanoTime();

            // Check if the step is a potential peak
            if (!isPeak) {
                isPeak = true;
                lastStepTimestamp = currentTime; // Record the timestamp of this potential step
            } else {
                // Check the time elapsed since the last step
                if (currentTime - lastStepTimestamp > STEP_DELAY_NS) {
                    isPeak = false; // Reset the peak flag as the step is confirmed
                    cumulativeSteps++; // Increment the cumulative step count
                }
            }
        } else {
            isPeak = false; // Reset the peak flag if the threshold is not surpassed
        }

        return cumulativeSteps; // Return the cumulative step count
    }
}
