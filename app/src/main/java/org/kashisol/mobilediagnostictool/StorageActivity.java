package org.kashisol.mobilediagnostictool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.widget.TextView;

import com.timqi.sectorprogressview.SectorProgressView;

import java.io.File;

public class StorageActivity extends AppCompatActivity {

    SectorProgressView storage_progress;
    private TextView free_storage_field, total_storage_field, percent_field;
    int KILOBYTE = 1024;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        setTitle("Storage Diagnosis");

        total_storage_field = findViewById(R.id.total_storage_field);
        free_storage_field = findViewById(R.id.free_storage_field);
        percent_field = findViewById(R.id.percent_field);

        storage_progress = findViewById(R.id.storage_progress);
//        storage_progress.setPercent(76);

//        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
//        long bytesAvailable;
//        if (android.os.Build.VERSION.SDK_INT >=
//                android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
//            bytesAvailable = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
//        }
//        else {
//            bytesAvailable = (long)stat.getBlockSize() * (long)stat.getAvailableBlocks();
//        }
//        long megAvailable = bytesAvailable / (1024 * 1024);
//        Log.e("()()()()","Available MB : " + megAvailable);
//        free_storage_field.setText("FREE STORAGE : " + megAvailable);

//        File path = Environment.getExternalStorageDirectory();
//        StatFs stat = new StatFs(path.getPath());
//        long availBlocks = stat.getAvailableBlocksLong();
//        long blockSize = stat.getBlockSizeLong();
//        long free_memory = (long)availBlocks * (long)blockSize;
//        total_storage_field.setText("TOTAL STORAGE : " + (blockSize));
//        free_storage_field.setText("FREE STORAGE : " + (free_memory/1000000) + " MB");

        StatFs internalStatFs = new StatFs( Environment.getRootDirectory().getAbsolutePath() );
        long internalTotal;
        long internalFree;

        StatFs externalStatFs = new StatFs( Environment.getExternalStorageDirectory().getAbsolutePath() );
        long externalTotal;
        long externalFree;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            internalTotal = ( internalStatFs.getBlockCountLong() * internalStatFs.getBlockSizeLong() ) / ( KILOBYTE * KILOBYTE );
            internalFree = ( internalStatFs.getAvailableBlocksLong() * internalStatFs.getBlockSizeLong() ) / ( KILOBYTE * KILOBYTE );
            externalTotal = ( externalStatFs.getBlockCountLong() * externalStatFs.getBlockSizeLong() ) / ( KILOBYTE * KILOBYTE );
            externalFree = ( externalStatFs.getAvailableBlocksLong() * externalStatFs.getBlockSizeLong() ) / ( KILOBYTE * KILOBYTE );
        }
        else {
            internalTotal = ( (long) internalStatFs.getBlockCount() * (long) internalStatFs.getBlockSize() ) / ( KILOBYTE * KILOBYTE );
            internalFree = ( (long) internalStatFs.getAvailableBlocks() * (long) internalStatFs.getBlockSize() ) / ( KILOBYTE * KILOBYTE );
            externalTotal = ( (long) externalStatFs.getBlockCount() * (long) externalStatFs.getBlockSize() ) / ( KILOBYTE * KILOBYTE );
            externalFree = ( (long) externalStatFs.getAvailableBlocks() * (long) externalStatFs.getBlockSize() ) / ( KILOBYTE * KILOBYTE );
        }

        long total = internalTotal + externalTotal;
        long free = internalFree + externalFree;
        long used = total - free;

        free_storage_field.setText("FREE STORAGE : " + free + " MB");
        total_storage_field.setText("TOTAL STORAGE : " + total + " MB");

        storage_progress.setPercent(used * 100 / total);
        percent_field.setText("" + (used * 100 / total) + "%");

    }
}