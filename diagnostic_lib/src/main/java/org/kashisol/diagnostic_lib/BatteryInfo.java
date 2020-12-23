package org.kashisol.diagnostic_lib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public abstract class BatteryInfo {

    private static Intent batteryStatus;
    private static IntentFilter ifilter;

    private Context mContext;
    private BatteryInfo mThis;
    private int mMilliseconds;
    private Timer mTimer;
    private BroadcastReceiver mReceiver;

    private int lastStatus = -1;

    public BatteryInfo(Context context, int milliseconds) {
        this.mContext = context;
        this.mThis = this;

        mTimer = new Timer();
        mMilliseconds = milliseconds;
        checkBatteryHealth();
        checkChargingStatus();
    }

    public BatteryInfo(Context context) {
        this.mContext = context;
        this.mThis = this;

        mTimer = new Timer();
        mMilliseconds = 200;
        checkBatteryHealth();
        checkChargingStatus();
    }

    public static float getBatteryPercentage(Context context) {

        if (ifilter == null)
            ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        if (batteryStatus == null)
            batteryStatus = context.registerReceiver(null, ifilter);

        // What is battery charge level?
        assert batteryStatus != null;
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        return level * 100 / (float)scale;
    }

    public static boolean isCharging(Context context) {
        if (ifilter == null)
            ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        if (batteryStatus == null)
            batteryStatus = context.registerReceiver(null, ifilter);

        assert batteryStatus != null;
        final int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

        return status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
    }

    public static boolean isChargingOverUSB(Context context) {
        if (ifilter == null)
            ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        if (batteryStatus == null)
            batteryStatus = context.registerReceiver(null, ifilter);

        assert batteryStatus != null;
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        return chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
    }

    public static boolean isChargingOverAC(Context context) {
        if (ifilter == null)
            ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        if (batteryStatus == null)
            batteryStatus = context.registerReceiver(null, ifilter);

        assert batteryStatus != null;
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        return chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
    }

    private void checkBatteryHealth() {

        if (ifilter == null)
            ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        if (batteryStatus == null)
            batteryStatus = mContext.registerReceiver(null, ifilter);

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int deviceHealth = intent.getIntExtra(BatteryManager.EXTRA_HEALTH,0);

                if(deviceHealth == BatteryManager.BATTERY_HEALTH_COLD){
                    mThis.onBatteryHealthChanged(deviceHealth, "BATTERY_HEALTH_COLD");
                }

                if(deviceHealth == BatteryManager.BATTERY_HEALTH_DEAD){
                    mThis.onBatteryHealthChanged(deviceHealth, "BATTERY_HEALTH_DEAD");
                }

                if (deviceHealth == BatteryManager.BATTERY_HEALTH_GOOD){
                    mThis.onBatteryHealthChanged(deviceHealth, "BATTERY_HEALTH_GOOD");
                }

                if(deviceHealth == BatteryManager.BATTERY_HEALTH_OVERHEAT){
                    mThis.onBatteryHealthChanged(deviceHealth, "BATTERY_HEALTH_OVERHEAT");
                }

                if (deviceHealth == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE){
                    mThis.onBatteryHealthChanged(deviceHealth, "BATTERY_HEALTH_OVER_VOLTAGE");
                }

                if (deviceHealth == BatteryManager.BATTERY_HEALTH_UNKNOWN){
                    mThis.onBatteryHealthChanged(deviceHealth, "BATTERY_HEALTH_UNKNOWN");
                }

                if (deviceHealth == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE){
                    mThis.onBatteryHealthChanged(deviceHealth, "BATTERY_HEALTH_UNSPECIFIED_FAILURE");
                }
            }
        };

        mContext.registerReceiver(mReceiver, ifilter);
    }

    private void checkChargingStatus() {
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
//                ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                batteryStatus = mContext.registerReceiver(null, ifilter);

                int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                if(status == BatteryManager.BATTERY_STATUS_CHARGING){
                    if (status != lastStatus)
                        mThis.onChargingStatusChanged(status, "BATTERY_STATUS_CHARGING");
                }

                if(status == BatteryManager.BATTERY_STATUS_DISCHARGING){
                    if (status != lastStatus)
                        mThis.onChargingStatusChanged(status, "BATTERY_STATUS_DISCHARGING");
                }

                if (status == BatteryManager.BATTERY_STATUS_FULL){
                    if (status != lastStatus)
                        mThis.onChargingStatusChanged(status, "BATTERY_STATUS_FULL");
                }

                if(status == BatteryManager.BATTERY_STATUS_UNKNOWN){
                    if (status != lastStatus)
                        mThis.onChargingStatusChanged(status, "BATTERY_STATUS_UNKNOWN");
                }

                if (status == BatteryManager.BATTERY_STATUS_NOT_CHARGING){
                    if (status != lastStatus)
                        mThis.onChargingStatusChanged(status, "BATTERY_STATUS_NOT_CHARGING");
                }

                lastStatus = status;
            }
        },0, mMilliseconds);
    }

    public abstract void onBatteryHealthChanged(int healthCode, String batteryHealth);

    public abstract void onChargingStatusChanged(int statusCode, String chargingStatus);

    public void stop() {
        if (mTimer != null)
            mTimer.cancel();
        if (mReceiver != null)
            mContext.unregisterReceiver(mReceiver);
        lastStatus = -1;
    }

}
