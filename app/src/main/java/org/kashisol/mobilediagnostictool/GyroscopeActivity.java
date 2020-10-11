package org.kashisol.mobilediagnostictool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class GyroscopeActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private TextView x_value_field, y_value_field, z_value_field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscope);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        x_value_field = findViewById(R.id.x_value_field);
        y_value_field = findViewById(R.id.y_value_field);
        z_value_field = findViewById(R.id.z_value_field);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            System.out.println("()()()() LEN LEN : " + sensorEvent.values.length);
            for (float value: sensorEvent.values) {
                System.out.println("()()()() " + value);
            }
            x_value_field.setText("" + sensorEvent.values[0]);
            y_value_field.setText("" + sensorEvent.values[1]);
            z_value_field.setText("" + sensorEvent.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor proxSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (proxSensor != null) {
            System.out.println("()()()() MAX VALUE : " + proxSensor.getMaximumRange());
            sensorManager.registerListener((SensorEventListener) this, proxSensor,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}