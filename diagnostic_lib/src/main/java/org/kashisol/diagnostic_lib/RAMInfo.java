package org.kashisol.diagnostic_lib;

import android.app.ActivityManager;
import android.content.Context;

import java.util.Timer;
import java.util.TimerTask;

public abstract class RAMInfo {

    private Context mContext;
    private Timer mTimer;

    public abstract void onRamInfoUpdated(double availableRAM, double usedRAM, double totalRAM);

    public RAMInfo(Context context) {
        mContext = context;
        startGettingRamInfo(0,500);
    }

    public RAMInfo(Context context, int delay, int period) {
        mContext = context;
        startGettingRamInfo(delay, period);
    }

    private void startGettingRamInfo(int delay, int period) {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                onRamInfoUpdated(
                        RAMInfo.availableRAMInMB(mContext),
                        RAMInfo.usedRAMInMB(mContext),
                        RAMInfo.totalRAMInMB(mContext)
                );
            }
        }, delay, period);
    }

    public void stop() {
        if (mTimer != null)
            mTimer.cancel();
    }

    private static ActivityManager.MemoryInfo init(Context context) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        return mi;
    }

    public static double totalRAMInMB(Context context) {
        return init(context).totalMem /0x100000L;
    }

    public static double availableRAMInMB(Context context) {
        return init(context).availMem /0x100000L;
    }

    public static double usedRAMInMB(Context context) {
        return RAMInfo.totalRAMInMB(context) - RAMInfo.availableRAMInMB(context);
    }

}
