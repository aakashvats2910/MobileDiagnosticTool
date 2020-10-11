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

public class OrientationActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private TextView x_value_field, y_value_field, z_value_field;
    private TextView orientation_field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orientation);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        x_value_field = findViewById(R.id.x_value_field);
        y_value_field = findViewById(R.id.y_value_field);
        z_value_field = findViewById(R.id.z_value_field);
        orientation_field = findViewById(R.id.orientation_field);

        orientation_field.setText("PORTRAIT");
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION) {
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
        Sensor proxSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
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

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            orientation_field.setText("LANDSCAPE");
        } else {
            orientation_field.setText("PORTRAIT");
        }
    }
}