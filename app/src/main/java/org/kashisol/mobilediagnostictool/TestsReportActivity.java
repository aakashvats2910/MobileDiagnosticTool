package org.kashisol.mobilediagnostictool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;


import org.kashisol.mobilediagnostictool.database.DBStatic;
import org.kashisol.mobilediagnostictool.database.Tests;
import org.kashisol.mobilediagnostictool.database.TestsDAO;
import org.kashisol.mobilediagnostictool.database.TestsRoomDatabase;
import org.kashisol.mobilediagnostictool.util.PDFFunctionality;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.UUID;

public class TestsReportActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();
    private TestsDAO testsDAO;
    private TestsRoomDatabase testsDB;
    private LiveData<List<Tests>> mAllTests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests_report);
        setTitle("Tests Reports");


        testsDB = TestsRoomDatabase.getDatabase(getApplicationContext());
        testsDAO = testsDB.testsDAO();

        mAllTests = testsDAO.getAllTests();



        mAllTests.observe(TestsReportActivity.this, new Observer<List<Tests>>() {
            @Override
            public void onChanged(List<Tests> tests) {
                String fulloutput = "";
                for (Tests t: tests) {
                    System.out.println("()()()() " + t.toString());
                    fulloutput += "TYPE: " + t.getType() +"\n";
                    fulloutput += "TIME: " + t.getTime() + "\n";
                    fulloutput += t.getExtra() + "\n";
                    fulloutput += "\n      ===============     \n";
                }
                System.out.println("()()()() sz " + tests.size());
                if (tests.size() != 0) {
                    PDFFunctionality.createMyPDF(getApplicationContext(), fulloutput);
                    DBStatic.truncateTestsTable(getApplicationContext());
                }
                System.out.println("()()()() DONE");
            }
        });

        openPDFv3();
    }

    private void openPDFFile() {
        File file = new File(getFilesDir() + "/myPDFFile.pdf");
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(file),"application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open File");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
        }
    }

    private void openPDFFilev2() {
        System.out.println("()()()() RAN RAN");
        File file = new File(getFilesDir() + "/myPDFFile.pdf");
        Uri uri = FileProvider.getUriForFile(this,
                BuildConfig.APPLICATION_ID + ".provider",
                file);
        System.out.println("()()()() GETTING PATH: " + file.getAbsolutePath());
//        Uri fileURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName()+".provider", file);
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(uri,"application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open File");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            System.out.println("()()()() FATAL : " + e.toString());
            // Instruct the user to install a PDF reader here, or something
        }
        System.out.println("ENDED ENDED");
    }

    private void openPDFv3() {
        File file = new File(getFilesDir() + "/myPDFFile.pdf");
        Uri uri = FileProvider.getUriForFile(this,
                BuildConfig.APPLICATION_ID + ".provider",
                file);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
        browserIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(browserIntent);
    }

}