package org.kashisol.mobilediagnostictool;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.kashisol.mobilediagnostictool.util.Connectivity;

import java.util.Timer;
import java.util.TimerTask;

public class NetworkCheckerActivity extends AppCompatActivity {

    private Button check_internet_speed_button;
    private TextView aeroplane_mode_field, connection_type_field, provider_name_field
            , roaming_field, network_type_field, wifi_connected_field, bluetooth_status_field;
    private BroadcastReceiver mReceiver;

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

        getSignalStatus();
        addBluetoothFunctionality();

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

        Timer timer = new Timer();;
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
                    }
                });
            }
        },0, 500);
    }

    private void isAirplaneModeOn() {
        boolean aeroplaneMode = Settings.System.getInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        aeroplane_mode_field.setText("AEROPLANE MODE : " + aeroplaneMode);
    }

    private void checkWifi() {
        wifi_connected_field.setText("CONNECTED : " + Connectivity.isConnectedWifi(getApplicationContext()));
    }

    private void getSignalStatus() {
        TelephonyManager mTelephonyManager;
        MyPhoneStateListener mPhoneStatelistener;
        mPhoneStatelistener = new MyPhoneStateListener();
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mTelephonyManager.listen(mPhoneStatelistener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    private void updateValues() {
        String[] values = Connectivity.getNetworkInfoSeparated(getApplicationContext());
        connection_type_field.setText("CONNECTED : " + values[3] + "-" + values[1]);
        provider_name_field.setText("PROVIDER : " + values[2]);
        roaming_field.setText("ROAMING : " + values[4]);
        network_type_field.setText("NETWORK TYPE : " + values[5]);
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

    private void addBluetoothFunctionality() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    bluetooth_status_field.setText("STATUS : DEVICE FOUND");
//           ... //Device found
                }
                else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                    bluetooth_status_field.setText("STATUS : DEVICE CONNECTED");
//           ... //Device is now connected
                }
                else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
           //Done searching
                    bluetooth_status_field.setText("STATUS : SEARCHING FINISHED");
                }
                else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
//           ... //Device is about to disconnect
                    bluetooth_status_field.setText("STATUS : DISCONNECTING");
                }
                else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
//           ... //Device has disconnected
                    bluetooth_status_field.setText("STATUS : DISCONNECTED");
                }
            }
        };
    }

}