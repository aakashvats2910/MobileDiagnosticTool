package org.kashisol.mobilediagnostictool;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.kashisol.mobilediagnostictool.database.DBStatic;
import org.kashisol.mobilediagnostictool.util.Connectivity;

import java.util.Timer;
import java.util.TimerTask;

import static android.telephony.TelephonyManager.NETWORK_TYPE_EDGE;
import static android.telephony.TelephonyManager.NETWORK_TYPE_GPRS;
import static android.telephony.TelephonyManager.NETWORK_TYPE_GSM;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSDPA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_LTE;
import static android.telephony.TelephonyManager.NETWORK_TYPE_UMTS;
import static android.telephony.TelephonyManager.NETWORK_TYPE_UNKNOWN;

public class NetworkCheckerActivity extends AppCompatActivity {

    private Button check_internet_speed_button, open_bluetooth_settings_button;
    private TextView aeroplane_mode_field, connection_type_field, provider_name_field, roaming_field, network_type_field, wifi_connected_field, bluetooth_status_field;
    private BroadcastReceiver mReceiver;
    private boolean takeData = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_checker);

        // initialize widgets
        check_internet_speed_button = findViewById(R.id.check_internet_speed_button);
        aeroplane_mode_field = findViewById(R.id.aeroplane_mode_field);
        connection_type_field = findViewById(R.id.connection_type_field);
        provider_name_field = findViewById(R.id.provider_name_field);
        roaming_field = findViewById(R.id.roaming_field);
        network_type_field = findViewById(R.id.network_type_field);
        wifi_connected_field = findViewById(R.id.wifi_connected_field);
        bluetooth_status_field = findViewById(R.id.bluetooth_status_field);
        open_bluetooth_settings_button = findViewById(R.id.open_bluetooth_settings_button);

        DBStatic.insert("Network Test", "All things checked", getApplicationContext());

        // setting on click listener on bluetooth button to open the bluetooth settings.
        open_bluetooth_settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(Intent.ACTION_MAIN, null);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                ComponentName cn = new ComponentName("com.android.settings",
                        "com.android.settings.bluetooth.BluetoothSettings");
                intent.setComponent(cn);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        isAirplaneModeOnByBroadcast();
        getSignalStatus();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(mReceiver, filter);

        check_internet_speed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://fast.com"));
                startActivity(intent);
            }
        });

//        connection_type_field.setText("CONNECTION TYPE : " + Connectivity.getNetworkInfo(getApplicationContext()));

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Run here to update values
                        isAirplaneModeOn();
                        updateValues();
                        checkWifi();
                        getBluetoothStatus();
                    }
                });
            }
        }, 0, 500);
    }

    private void isAirplaneModeOn() {
        boolean aeroplaneMode = Settings.System.getInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        aeroplane_mode_field.setText("AEROPLANE MODE : " + aeroplaneMode);
    }

    private void isAirplaneModeOnByBroadcast() {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.AIRPLANE_MODE");

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };

        registerReceiver(broadcastReceiver, intentFilter);

//        BroadcastReceiver receiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                System.out.println("()()()() STATE CHANGED");
//                Toast.makeText(getApplicationContext(), "Aeroplane mode State changed",Toast.LENGTH_SHORT).show();
//            }
//        };
//
//        getApplicationContext().registerReceiver(receiver, intentFilter);
    }


    private void checkWifi() {
        wifi_connected_field.setText("CONNECTED : " + Connectivity.isConnectedWifi(getApplicationContext()));
    }


    //    @RequiresApi(api = Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getSignalStatus() {
        TelephonyManager mTelephonyManager;
        MyPhoneStateListener mPhoneStatelistener;
        mPhoneStatelistener = new MyPhoneStateListener();
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mTelephonyManager.listen(mPhoneStatelistener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        // get the signal type:
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        switch (mTelephonyManager.getDataNetworkType()) {
            case NETWORK_TYPE_EDGE:
                network_type_field.setText("Edge");
                System.out.println("()()()() edge");
                break;
            case NETWORK_TYPE_GSM:
                network_type_field.setText("GSM");
                System.out.println("()()()() gsm");
                break;
            case NETWORK_TYPE_GPRS:
                network_type_field.setText("GPRS");
                System.out.println("()()()() gprs");
                break;
            case NETWORK_TYPE_UMTS:
                network_type_field.setText("UMTS");
                System.out.println("()()()() umts");
                break;
            case NETWORK_TYPE_HSDPA:
                network_type_field.setText("HSDPA");
                System.out.println("()()()() hsdpa");
                break;
            case NETWORK_TYPE_LTE:
                network_type_field.setText("LTE");
                System.out.println("()()()() lte");
                break;
            case NETWORK_TYPE_UNKNOWN:
                network_type_field.setText("Unknown");
                System.out.println("()()()() unknown");
                break;
            default:
                network_type_field.setText("New type of Network");
                System.out.println("()()()() new type");
                break;
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                break;
        }
    }

    private void updateValues() {
        String[] values = Connectivity.getNetworkInfoSeparated(getApplicationContext());
        connection_type_field.setText("CONNECTED : " + values[3] + "-" + values[1]);
        provider_name_field.setText("PROVIDER : " + values[2]);
        roaming_field.setText("ROAMING : " + values[4]);
//        network_type_field.setText("NETWORK TYPE : " + values[5]);
    }

    class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            int signalStrengthInt1 = signalStrength.getGsmSignalStrength();
            int signalStrengthInt2 = (2 * signalStrengthInt1) - 113;
            System.out.println("()()()() SIGNAL_STR 1 : " + signalStrengthInt1);
            System.out.println("()()()() SIGNAL_STR 2 : " + signalStrengthInt2);
        }
    }

    private void getBluetoothStatus() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            bluetooth_status_field.setText("STATUS : Does not support Bluetooth");
        } else if (!mBluetoothAdapter.isEnabled()) {
            // Bluetooth is not enabled :)
            bluetooth_status_field.setText("STATUS : DISABLED");
        } else {
            // Bluetooth is enabled
            bluetooth_status_field.setText("STATUS : ENABLED");
        }
    }

}