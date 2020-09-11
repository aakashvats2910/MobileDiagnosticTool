package org.kashisol.mobilediagnostictool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import org.kashisol.mobilediagnostictool.util.CPUAdapter;
import org.kashisol.mobilediagnostictool.util.NewLocationCapture;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CPUActivity extends AppCompatActivity {

    private TextView info_text_view_cpu;
    String output;
    private LinearLayout parent_layout;
    private RecyclerView recycler_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_p_u);

        parent_layout = findViewById(R.id.parent_layout);
        recycler_view = findViewById(R.id.recycler_view);

        Context context;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(linearLayoutManager);

        // TODO to be included in the future versions
        try {
            String[] DATA = {"/system/bin/cat", "/proc/cpuinfo"};
            ProcessBuilder processBuilder = new ProcessBuilder(DATA);
            Process process = processBuilder.start();
            InputStream inputStream = process.getInputStream();
            byte[] byteArry = new byte[1024];
            String output = "";
            while (inputStream.read(byteArry) != -1) {
                output = output + new String(byteArry);
                this.output = output;
            }
            inputStream.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        CPUAdapter adapter = new CPUAdapter(getOutputArray(output));
        recycler_view.setAdapter(adapter);

        System.out.println("()()()() " + output);
        System.out.println("=======================");
    }

    private ArrayList<String> getOutputArray(String input) {
        ArrayList<String> toReturn = new ArrayList<>();
        String[] lines = input.split("\n");
        for (String line: lines) {
            String[] split = line.split(":");
            if (split.length <= 1) continue;
            split[0].replaceAll(" ","");
            split[1].replaceAll(" ","");
            toReturn.add(split[0] + " : " + split[1]);
        }
        return toReturn;
    }

//    private void filterOutputMap(String input) {
//        Map<String, String> map = new HashMap<>();
//        String[] lines = input.split("\n");
//        for (String line: lines) {
//            String[] split = line.split(":");
//            if (split.length <= 1) continue;
//            map.put(split[0].replaceAll(" ",""), split[1].replaceAll(" ",""));
//        }
//        for (Map.Entry<String, String> entry: map.entrySet()) {
//
//        }
//    }

}