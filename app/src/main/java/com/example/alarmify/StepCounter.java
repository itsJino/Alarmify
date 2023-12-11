package com.example.alarmify;

public class StepCounter {
    private static final int STEP_THRESHOLD = 10;
    private static final int STEP_DELAY_NS = 250000000; // 0.25 seconds

    private boolean isPeak = false;
    private long lastStepTimestamp = 0;
    private int cumulativeSteps = 0; // Maintain the cumulative step count

    public int countSteps(float[] values) {
        float maxValue = 0;
        for (float value : values) {
            if (Math.abs(value) > maxValue) {
                maxValue = Math.abs(value);
            }
        }
        if (maxValue > STEP_THRESHOLD) {
            long currentTime = System.nanoTime();
            if (!isPeak) {
                isPeak = true;
                lastStepTimestamp = currentTime;
            } else {
                if (currentTime - lastStepTimestamp > STEP_DELAY_NS) {
                    isPeak = false;
                    cumulativeSteps++; // Increment the cumulative step count
                }
            }
        } else {
            isPeak = false;
        }
        return cumulativeSteps; // Return the cumulative step count
    }
}
