package org.kashisol.mobilediagnostictool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.kashisol.mobilediagnostictool.database.DBStatic;

public class CameraActivity extends AppCompatActivity {

    private Button start_camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        DBStatic.insert("Camera Test", "Working", getApplicationContext());

        start_camera = findViewById(R.id.start_camera);
        start_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CameraActivity.this, CameraTwoActivity.class));
            }
        });
    }
}