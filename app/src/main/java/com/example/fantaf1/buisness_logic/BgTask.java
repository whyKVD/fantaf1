package com.example.fantaf1.buisness_logic;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.example.fantaf1.FormationActivity;
import com.example.fantaf1.MainActivity;
import com.example.fantaf1.classes.Constructor;
import com.example.fantaf1.classes.Pilota;
import com.example.fantaf1.R;
import com.example.fantaf1.network.F1APIservice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import com.google.gson.Gson;

public class BgTask {
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private String action = null;

    public BgTask(Gestore g, String... params){
        AtomicReference<String> data = null;
        executor.execute(() -> {
            g.getContext().runOnUiThread(() -> g.getContext().findViewById(R.id.loader).setVisibility(View.VISIBLE));
            switch (params[0]) {
                case "client":
                    BgTask.this.action = params[0];
                    //fetching data from api
                    F1APIservice f1APIservice = new F1APIservice();
                    try {
                        ArrayList<Object> obj = f1APIservice.fetchData();
                        File file = new File(g.getContext().getFilesDir(), "drivers.json");

                        FileOutputStream fos = new FileOutputStream(file);
                        OutputStreamWriter osw = new OutputStreamWriter(fos);
                        osw.write(new Gson().toJson(obj));
                        osw.flush();
                        osw.close();
                        fos.close();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                    break;
                case "readFile":
                    BgTask.this.action = params[0];
                    try {
                        File file = new File(g.getContext().getFilesDir(), "drivers.json");

                        FileInputStream fis = new FileInputStream(file);
                        InputStreamReader isr = new InputStreamReader(fis);
                        BufferedReader br = new BufferedReader(isr);

                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }

                        br.close();
                        isr.close();
                        fis.close();

                        JSONArray pAc = new JSONArray(sb.toString());
                        parsePAC(pAc,g);
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    break;
                case "findPilot":
                    BgTask.this.action = params[0];
                    g.findPilots(params[1]);
                    break;
                default:
                    break;
            }

            handler.post(() ->{
                switch (action){
                    case "client":
                        Intent i = new Intent(g.getContext(), MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        g.getContext().startActivity(i);
                        break;
                    case "readFile":
                        if (g.getContext().getClass().equals(FormationActivity.class)) {
                            g.row(1);
                            g.row(2);
                            g.row(3);
                        }
                        break;
                    default:
                        break;
                }
                g.getContext().runOnUiThread(() -> g.getContext().findViewById(R.id.loader).setVisibility(View.INVISIBLE));
            });
        });
    }

    private void parsePAC(JSONArray pAc, Gestore g) {
        try {
            JSONArray c = pAc.getJSONArray(1);
            JSONArray p = pAc.getJSONArray(0);
            ArrayList<Pilota> pilots = new ArrayList<>();
            ArrayList<Constructor> constructors = new ArrayList<>();

            for (int i = 0; i < p.length(); i++) {
                pilots.add(new Pilota((JSONObject) p.get(i)));
            }
            for (int i = 0; i < c.length(); i++) {
                constructors.add(new Constructor((JSONObject) c.get(i)));
            }
            g.setPilots(pilots);
            g.setConstructors(constructors);
        }catch (Exception ex){}
    }
}
