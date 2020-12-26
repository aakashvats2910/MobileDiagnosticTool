package org.kashisol.mobilediagnostictool;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button start_button;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start_button = findViewById(R.id.start_button);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ChooseDiagnosisTypeActivity.class));
            }
        });

//        LocationInfo locationInfo = new LocationInfo(getApplicationContext());

//        String extstoragedir = Environment.getExternalStorageDirectory().toString(); /storage/emulated/0
        String extstoragedir = Environment.getDataDirectory().getParent();
        System.out.println("()()()() " + extstoragedir);
    }
}