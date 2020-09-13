package org.kashisol.mobilediagnostictool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PhoneInfoActivity extends AppCompatActivity {

    public final int IMEI_RQST = 2;
    private TextView imei_one_field, imei_two_field, android_version_field, build_os_field,
            device_info_field, model_no_field, kernel_info_field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_info);

        // Initialize the field and the widgets
        imei_one_field = findViewById(R.id.imei_one_field);
        imei_two_field = findViewById(R.id.imei_two_field);
        android_version_field = findViewById(R.id.android_version_field);
        build_os_field = findViewById(R.id.build_os_field);
        device_info_field = findViewById(R.id.device_info_field);
        model_no_field = findViewById(R.id.model_no_field);
        kernel_info_field = findViewById(R.id.kernel_info_field);

        // Business logic for simple code.
        android_version_field.setText("VERSION : " + Build.VERSION.RELEASE);
        build_os_field.setText("BUILD OS : " + Build.VERSION.BASE_OS);
        device_info_field.setText("DEVICE : " + Build.MANUFACTURER);
        model_no_field.setText("MODEL NO. : " + Build.MODEL);
        kernel_info_field.setText("KERNE INFO : " + readKernelVersion());

        System.out.println("()()()() BUILD DEVICE : " + Build.DEVICE);
        System.out.println("()()()() MODL " + Build.MODEL);

        if (ActivityCompat.checkSelfPermission(PhoneInfoActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PhoneInfoActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, IMEI_RQST);
        } else getImeiDetails();

    }

    private void getImeiDetails() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            imei_one_field.setText("IMEI 1 : " + telephonyManager.getImei(0));
            imei_two_field.setText("IMEI 2 : " + telephonyManager.getImei(1));
        } else {
            System.out.println("()()()() " + telephonyManager.getDeviceId());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == IMEI_RQST && grantResults[0] == RESULT_OK) {
            getImeiDetails();
        }
    }

    public static String readKernelVersion() {
        try {
            Process p = Runtime.getRuntime().exec("uname -a");
            InputStream is = null;
            if (p.waitFor() == 0) {
                is = p.getInputStream();
            } else {
                is = p.getErrorStream();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is),
                    100);
            String line = br.readLine();
            br.close();
            return line;
        } catch (Exception ex) {
            return "ERROR: " + ex.getMessage();
        }
    }

}