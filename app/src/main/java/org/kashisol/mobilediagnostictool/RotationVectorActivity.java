package org.kashisol.mobilediagnostictool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import org.kashisol.mobilediagnostictool.database.DBStatic;

public class RotationVectorActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private TextView x_value_field, y_value_field, z_value_field, scalar_value_field;
    private boolean takeData = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotation_vector);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        x_value_field = findViewById(R.id.x_value_field);
        y_value_field = findViewById(R.id.y_value_field);
        z_value_field = findViewById(R.id.z_value_field);
        scalar_value_field = findViewById(R.id.scalar_value_field);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            System.out.println("()()()() LEN LEN : " + sensorEvent.values.length);
            for (float value: sensorEvent.values) {
                System.out.println("()()()() " + value);
            }
            x_value_field.setText("" + sensorEvent.values[0]);
            y_value_field.setText("" + sensorEvent.values[1]);
            z_value_field.setText("" + sensorEvent.values[2]);
            scalar_value_field.setText("" + sensorEvent.values[3]);

            if (takeData) {
                takeData = false;
                String extra = "X: " + sensorEvent.values[0]
                        + "\nY: " + sensorEvent.values[1]
                        + "\nZ: " + sensorEvent.values[2]
                        + "\nScalar Value: " + sensorEvent.values[3];
                DBStatic.insert("Rotation-Vector Sensor Test",extra, getApplicationContext());
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor proxSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
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