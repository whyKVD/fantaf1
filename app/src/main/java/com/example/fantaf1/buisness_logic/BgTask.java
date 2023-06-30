package com.example.fantaf1.buisness_logic;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;

import com.example.fantaf1.FormationActivity;
import com.example.fantaf1.MainActivity;
import com.example.fantaf1.PilotActivity;
import com.example.fantaf1.R;
import com.example.fantaf1.classes.Constructor;
import com.example.fantaf1.classes.FastestLap;
import com.example.fantaf1.classes.GrandPrix;
import com.example.fantaf1.classes.Pilota;
import com.example.fantaf1.classes.Standing;
import com.example.fantaf1.network.F1APIservice;
import com.example.fantaf1.view.StandingCard;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BgTask {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private String action = null;

    private static String[] files = {"constructors.json","drivers.json","fastestsLap.json","grandPrix.json"};

    public BgTask(Gestore g, String... params){
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                g.getContext().runOnUiThread(() -> g.getContext().findViewById(R.id.loader).setVisibility(View.VISIBLE));
            }catch(Exception ex){}
            switch (params[0]) {
                case "fetchData":
                    BgTask.this.action = params[0];
                    //fetching data from api
                    new F1APIservice(g).scrapeData();
                    break;
                case "readAllFile":
                    BgTask.this.action = params[0];
                    String data = null;
                    try {
                        try {
                            for(String f : files){
                                FileReader fin = new FileReader(new File(g.getContext().getFilesDir(), f));
                                BufferedReader bin = new BufferedReader(fin);
                                data = bin.readLine();
                                JSONArray data_obj = new JSONArray(data);
                                bin.close();
                                fileToObj(f,data_obj,g);
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    break;
                case "readFile":
                    BgTask.this.action = params[0];
                    try {
                        for (int i = 1; i < params.length; i++) {
                            File file = new File(g.getContext().getFilesDir(), params[i]);

                            FileInputStream fis = new FileInputStream(file);
                            InputStreamReader isr = new InputStreamReader(fis);
                            BufferedReader br = new BufferedReader(isr);

                            StringBuilder sb = new StringBuilder();
                            String line;
                            while ((line = br.readLine()) != null) {
                                sb.append(line);
                            }

                            data = sb.toString();

                            JSONArray data_obj = new JSONArray(data);

                            br.close();
                            isr.close();
                            fis.close();
                            fileToObj(params[i],data_obj,g);
                        }
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    break;
                case "findPilot":
                    BgTask.this.action = params[0];
                    g.findPilots(params[1]);
                    break;
                case "updateStandings":
                    new F1APIservice(g).updateStandings();

                    Intent i = new Intent(g.getContext(), MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    g.getContext().startActivity(i);
                    break;
                default:
                    break;
            }

            handler.post(() -> {
                switch (action){
                    case "fetchData":
                        Intent i = new Intent(g.getContext(), MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        g.getContext().startActivity(i);
                        break;
                    case "readFile":
                        if (g.getContext().getClass().equals(FormationActivity.class)) {
                            g.row(1);
                            g.row(2);
                            g.row(3);
                        } else if (g.getContext().getClass().equals(PilotActivity.class)) {
                            g.getContext().runOnUiThread(()->{
                                Pilota p = g.getPilots().get(0);
                                LinearLayout stScroll = g.getContext().findViewById(R.id.standings);

                                for(GrandPrix gp : g.getGrandPrix()){
                                    for(Standing st: gp.getStandings()){
                                        if(st.getNumber().equals(p.getNumber())) {
                                            StandingCard sc = new StandingCard(g.getContext(), gp.getName(), st.getPos());
                                            stScroll.addView(sc.getV());
                                        }
                                    }
                                }
                            });
                        }
                        break;
                    default:
                        break;
                }
                try {
                    g.getContext().runOnUiThread(() -> g.getContext().findViewById(R.id.loader).setVisibility(View.INVISIBLE));
                }catch(Exception ex){}
            });
        });
    }

    private void fileToObj(String wich, JSONArray objs, Gestore g) {
        try {
            switch (wich) {
                case "constructors.json" -> {
                    ArrayList<Constructor> constructors = new ArrayList<>();

                    for (int i = 0; i < objs.length(); i++) {
                        Constructor tmp = new Constructor();
                        tmp.setName((String) ((JSONObject) objs.get(i)).get("name"));
                        tmp.setBase((String) ((JSONObject) objs.get(i)).get("base"));
                        tmp.setTeamChief((String) ((JSONObject) objs.get(i)).get("teamChief"));
                        tmp.setTechChief((String) ((JSONObject) objs.get(i)).get("techChief"));
                        tmp.setChassis((String) ((JSONObject) objs.get(i)).get("chassis"));
                        tmp.setPowUnit((String) ((JSONObject) objs.get(i)).get("powUnit"));
                        tmp.setFirstTeamEntry((String) ((JSONObject) objs.get(i)).get("firstTeamEntry"));
                        tmp.setFastestlap((String) ((JSONObject) objs.get(i)).get("fastestlap"));
                        tmp.setPolePos((String) ((JSONObject) objs.get(i)).get("polePos"));
                        tmp.setWorldChamps((String) ((JSONObject) objs.get(i)).get("worldChamps"));

                        constructors.add(tmp);
                    }

                    g.setConstructors(constructors);
                }
                case "fastestsLap.json" -> {
                    ArrayList<FastestLap> fls = new ArrayList<>();

                    for (int i = 0; i < objs.length(); i++) {
                        FastestLap tmp = new FastestLap();
                        tmp.setGrandPrix((String) ((JSONObject) objs.get(i)).get("grandPrix"));
                        tmp.setDriver((String) ((JSONObject) objs.get(i)).get("driver"));
                        tmp.setCar((String) ((JSONObject) objs.get(i)).get("car"));
                        tmp.setTime((String) ((JSONObject) objs.get(i)).get("time"));

                        fls.add(tmp);
                    }

                    g.setFastsLap(fls);
                }
                case "drivers.json" -> {
                    ArrayList<Pilota> pilots = new ArrayList<>();

                    for (int i = 0; i < objs.length(); i++) {
                        Pilota tmp = new Pilota();
                        tmp.setName((String) ((JSONObject) objs.get(i)).get("name"));
                        tmp.setDateOfBirth((String) ((JSONObject) objs.get(i)).get("dateOfBirth"));
                        tmp.setPlaceOfBirth((String) ((JSONObject) objs.get(i)).get("placeOfBirth"));
                        tmp.setNumber((String) ((JSONObject) objs.get(i)).get("number"));
                        tmp.setTeam((String) ((JSONObject) objs.get(i)).get("team"));
                        tmp.setNationality((String) ((JSONObject) objs.get(i)).get("nationality"));
                        tmp.setPodiums((String) ((JSONObject) objs.get(i)).get("podiums"));
                        tmp.setGrandPrixEntered((String) ((JSONObject) objs.get(i)).get("grandPrixEntered"));
                        tmp.setWorldChamps((String) ((JSONObject) objs.get(i)).get("worldChamps"));

                        pilots.add(tmp);
                    }

                    g.setPilots(pilots);
                }
                case "grandPrix.json" -> {
                    ArrayList<GrandPrix> gps = new ArrayList<>();

                    for (int i = 0; i < objs.length(); i++) {
                        GrandPrix tmp = new GrandPrix();
                        tmp.setName((String) ((JSONObject) objs.get(i)).get("name"));
                        tmp.setDate((String) ((JSONObject) objs.get(i)).get("date"));
                        JSONArray standings = ((JSONObject) objs.get(i)).getJSONArray("standings");
                        ArrayList<Standing> sts = new ArrayList<>();
                        for (int j = 0; j < standings.length(); j++) {
                            Standing s = new Standing();
                            s.setName((String) ((JSONObject) standings.get(j)).get("name"));
                            s.setSecondName((String) ((JSONObject) standings.get(j)).get("secondName"));
                            s.setCode((String) ((JSONObject) standings.get(j)).get("code"));
                            s.setPos((String) ((JSONObject) standings.get(j)).get("pos"));
                            s.setLaps((String) ((JSONObject) standings.get(j)).get("laps"));
                            s.setNumber((String) ((JSONObject) standings.get(j)).get("number"));
                            s.setConstructor((String) ((JSONObject) standings.get(j)).get("constructor"));
                            s.setTime((String) ((JSONObject) standings.get(j)).get("time"));

                            sts.add(s);
                        }
                        tmp.setStandings(sts);

                        gps.add(tmp);
                    }

                    g.setGrandPrix(gps);
                }
                default -> {}
            }
        }catch (Exception ex){}
    }
}
