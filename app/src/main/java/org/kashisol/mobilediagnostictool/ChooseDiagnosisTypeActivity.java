package org.kashisol.mobilediagnostictool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseDiagnosisTypeActivity extends AppCompatActivity {

    private Button storage_button, battery_button, network_button, cpu_button, apps_button,
            ram_button, screen_calibration_button, speaker_button, illumination_button
            , motion_button, camera_button, phone_info_button, location_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_diagnosis_type);
        setTitle("Diagnosis Type");

        // Initialize the widgets
        storage_button = findViewById(R.id.storage_button);
        battery_button = findViewById(R.id.battery_button);
        network_button = findViewById(R.id.network_button);
        cpu_button = findViewById(R.id.cpu_button);
        apps_button = findViewById(R.id.apps_button);
        ram_button = findViewById(R.id.ram_button);
        screen_calibration_button = findViewById(R.id.screen_calibration_button);
        speaker_button = findViewById(R.id.speaker_button);
        illumination_button = findViewById(R.id.illumination_button);
        motion_button = findViewById(R.id.motion_button);
        camera_button = findViewById(R.id.camera_button);
        phone_info_button = findViewById(R.id.phone_info_button);
        location_button = findViewById(R.id.location_button);

        // location button on click listener
        location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseDiagnosisTypeActivity.this, LocationActivity.class));
            }
        });

        // phone button on click listener
        phone_info_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseDiagnosisTypeActivity.this, PhoneInfoActivity.class));
            }
        });

        // camera button on click listener
        camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseDiagnosisTypeActivity.this, CameraActivity.class));
            }
        });

        // motion button on click listener
        motion_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseDiagnosisTypeActivity.this, MotionActivity.class));
            }
        });

        // illumination button on click listener
        illumination_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseDiagnosisTypeActivity.this, IlluminationActivity.class));
            }
        });

        // speaker button on click listener
        speaker_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseDiagnosisTypeActivity.this, SpeakerActivity.class));
            }
        });

        // screen calibration button on click listener
        screen_calibration_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseDiagnosisTypeActivity.this, ScreenCalibrationActivity.class));
            }
        });

        // ram button on click listener
        ram_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseDiagnosisTypeActivity.this, RAMActivity.class));
            }
        });

        // apps button on click listener
        apps_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseDiagnosisTypeActivity.this, AllAppsActivity.class));
            }
        });

        // cpu button on click listener
        cpu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseDiagnosisTypeActivity.this, CPUActivity.class));
            }
        });

        // button on click listener.
        network_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseDiagnosisTypeActivity.this, NetworkCheckerActivity.class));
            }
        });

        // battery button on click listener
        battery_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseDiagnosisTypeActivity.this, BatteryActivity.class));
            }
        });

        // setting on click listeners
        storage_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseDiagnosisTypeActivity.this, StorageActivity.class));
            }
        });
    }

}