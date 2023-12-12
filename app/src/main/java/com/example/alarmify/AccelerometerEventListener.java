package com.example.alarmify;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class AccelerometerEventListener implements SensorEventListener {

    private SensorManager sensorManager; // Manages sensor functions
    private Sensor accelerometerSensor; // Represents the accelerometer sensor
    private StepCounter stepCounter; // Responsible for counting steps using accelerometer data
    private StepCountListener stepCountListener; // Listener to notify about step count changes

    public AccelerometerEventListener(SensorManager sensorManager, StepCountListener listener) {
        this.sensorManager = sensorManager;
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        stepCounter = new StepCounter();
        this.stepCountListener = listener;
    }

    // Method to register the sensor listener
    public void register() {
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    // Method to unregister the sensor listener
    public void unregister() {
        sensorManager.unregisterListener(this);
    }

    // Called when the sensor value changes
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) { // Check if the changed sensor is the accelerometer
            float[] values = event.values; // Get the accelerometer values
            int steps = stepCounter.countSteps(values); // Count the steps using the StepCounter
            // Notify the listener about step count changes
            if (stepCountListener != null) {
                stepCountListener.onStepCount(steps); // Pass the step count to the listener
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle accuracy changes if needed
    }
}

