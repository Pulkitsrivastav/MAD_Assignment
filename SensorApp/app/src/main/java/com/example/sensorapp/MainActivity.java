package com.example.sensorapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer, light, proximity;

    private TextView accText, lightText, proxText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accText = findViewById(R.id.accelerometerText);
        lightText = findViewById(R.id.lightText);
        proxText = findViewById(R.id.proximityText);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null) {
            if (accelerometer != null)
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);

            if (light != null)
                sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_UI);

            if (proximity != null)
                sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            accText.setText(String.format(Locale.getDefault(), "X: %.2f | Y: %.2f | Z: %.2f", x, y, z));
        } else if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float value = event.values[0];
            lightText.setText(String.format(Locale.getDefault(), "%.2f lx", value));
        } else if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            float value = event.values[0];
            proxText.setText(String.format(Locale.getDefault(), "%.2f cm", value));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No implementation needed for this demo
    }
}
