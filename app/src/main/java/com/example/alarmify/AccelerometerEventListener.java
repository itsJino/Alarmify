package com.example.alarmify;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class AccelerometerEventListener implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private StepCounter stepCounter;
    private StepCountListener stepCountListener;

    public AccelerometerEventListener(SensorManager sensorManager, StepCountListener listener) {
        this.sensorManager = sensorManager;
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        stepCounter = new StepCounter();
        this.stepCountListener = listener;
    }

    public void register() {
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregister() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] values = event.values;
            int steps = stepCounter.countSteps(values);
            // Notify the listener about step count changes
            if (stepCountListener != null) {
                stepCountListener.onStepCount(steps);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle accuracy changes if needed
    }
}

