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

public class IlluminationActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private TextView light_output_field;
    private TextView max_min_field;
    private boolean takeData = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_illumination);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        light_output_field = findViewById(R.id.illumination_output_field);
        max_min_field = findViewById(R.id.max_min_field);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            for (float value: sensorEvent.values) {
                System.out.println("()()()() ILL : " + value);
                if (value != 0) {
                    light_output_field.setText("" + value);
                    if (takeData) {
                        takeData = false;
                        String extra = "Value: " + value;
                        DBStatic.insert("Light Sensor Test",extra, getApplicationContext());
                    }
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (lightSensor != null) {
            max_min_field.setText("Max. value " + lightSensor.getMaximumRange()
                    + "");
            sensorManager.registerListener((SensorEventListener) this, lightSensor,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}