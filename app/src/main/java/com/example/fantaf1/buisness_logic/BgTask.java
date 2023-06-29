package com.example.fantaf1.buisness_logic;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.example.fantaf1.FormationActivity;
import com.example.fantaf1.MainActivity;
import com.example.fantaf1.R;
import com.example.fantaf1.classes.Constructor;
import com.example.fantaf1.classes.FastestLap;
import com.example.fantaf1.classes.GrandPrix;
import com.example.fantaf1.classes.Pilota;
import com.example.fantaf1.classes.Standing;
import com.example.fantaf1.network.F1APIservice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
            g.getContext().runOnUiThread(() -> g.getContext().findViewById(R.id.loader).setVisibility(View.VISIBLE));
            switch (params[0]) {
                case "fetchData":
                    BgTask.this.action = params[0];
                    //fetching data from api
                    F1APIservice f1APIservice = new F1APIservice(g);
                    try {
                        f1APIservice.scrapeData();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                    break;
                case "readAllFile":
                    BgTask.this.action = params[0];
                    String data = null;
                    ArrayList<Object> objs = new ArrayList<>();
                    try {
                        try {
                            for(String f : files){
                                FileReader fin = new FileReader(new File(g.getContext().getFilesDir(), f));
                                BufferedReader bin = new BufferedReader(fin);
                                data = bin.readLine();
                                JSONArray data_obj = new JSONArray(data);
                                bin.close();
                                objs.add(data_obj);
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        fileToObj("all",objs,g);
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    break;
                /*case "readFile":
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
                    break;*/
                case "findPilot":
                    BgTask.this.action = params[0];
                    g.findPilots(params[1]);
                    break;
                default:
                    break;
            }

            handler.post(() ->{
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
                        }/* else if (g.getContext().getClass().equals(FirstActivity.class)) {
                            F1APIservice f1 = new F1APIservice(g);
                            try {
                                ArrayList<Object> obj = f1.updateStandings();
                                File file = new File(g.getContext().getFilesDir(), "drivers.json");

                                FileOutputStream fos = new FileOutputStream(file);
                                OutputStreamWriter osw = new OutputStreamWriter(fos);
                                osw.write(new Gson().toJson(obj));
                                osw.flush();
                                osw.close();
                                fos.close();
                            }catch(Exception ex){}

                            i = new Intent(g.getContext(), MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            g.getContext().startActivity(i);
                            break;
                        }*/
                        break;
                    default:
                        break;
                }
                g.getContext().runOnUiThread(() -> g.getContext().findViewById(R.id.loader).setVisibility(View.INVISIBLE));
            });
        });
    }

    private void fileToObj(String wich, ArrayList<Object> objs, Gestore g) {
        switch (wich){
            case "piloti" -> {}
            default -> {
                try {
                    JSONArray c = (JSONArray) objs.get(0);
                    JSONArray p = (JSONArray) objs.get(1);
                    JSONArray f = (JSONArray) objs.get(2);
                    JSONArray gp = (JSONArray) objs.get(3);

                    ArrayList<Pilota> pilots = new ArrayList<>();
                    ArrayList<Constructor> constructors = new ArrayList<>();
                    ArrayList<FastestLap> fls = new ArrayList<>();
                    ArrayList<GrandPrix> gps = new ArrayList<>();

                    for (int i = 0; i < p.length(); i++) {
                        Pilota tmp = new Pilota();
                        tmp.setName((String) ((JSONObject)p.get(i)).get("name"));
                        tmp.setDateOfBirth((String) ((JSONObject)p.get(i)).get("dateOfBirth"));
                        tmp.setPlaceOfBirth((String) ((JSONObject)p.get(i)).get("placeOfBirth"));
                        tmp.setNumber((String) ((JSONObject)p.get(i)).get("number"));
                        tmp.setTeam((String) ((JSONObject)p.get(i)).get("team"));
                        tmp.setNationality((String) ((JSONObject)p.get(i)).get("nationality"));
                        tmp.setPodiums((String) ((JSONObject)p.get(i)).get("podiums"));
                        tmp.setGrandPrixEntered((String) ((JSONObject)p.get(i)).get("grandPrixEntered"));
                        tmp.setWorldChamps((String) ((JSONObject)p.get(i)).get("worldChamps"));

                        pilots.add(tmp);
                    }
                    for (int i = 0; i < c.length(); i++) {
                        Constructor tmp = new Constructor();
                        tmp.setName((String) ((JSONObject)c.get(i)).get("name"));
                        tmp.setBase((String) ((JSONObject)c.get(i)).get("base"));
                        tmp.setTeamChief((String) ((JSONObject)c.get(i)).get("teamChief"));
                        tmp.setTechChief((String) ((JSONObject)c.get(i)).get("techChief"));
                        tmp.setChassis((String) ((JSONObject)c.get(i)).get("chassis"));
                        tmp.setPowUnit((String) ((JSONObject)c.get(i)).get("powUnit"));
                        tmp.setFirstTeamEntry((String) ((JSONObject)c.get(i)).get("firstTeamEntry"));
                        tmp.setFastestlap((String) ((JSONObject)c.get(i)).get("fastestlap"));
                        tmp.setPolePos((String) ((JSONObject)c.get(i)).get("polePos"));
                        tmp.setWorldChamps((String) ((JSONObject)c.get(i)).get("worldChamps"));

                        constructors.add(tmp);
                    }
                    for (int i = 0; i < f.length(); i++) {
                        FastestLap tmp = new FastestLap();
                        tmp.setGrandPrix((String) ((JSONObject)f.get(i)).get("grandPrix"));
                        tmp.setDriver((String) ((JSONObject)f.get(i)).get("driver"));
                        tmp.setCar((String) ((JSONObject)f.get(i)).get("car"));
                        tmp.setTime((String) ((JSONObject)f.get(i)).get("time"));

                        fls.add(tmp);
                    }
                    for (int i = 0; i < gp.length(); i++) {
                        GrandPrix tmp = new GrandPrix();
                        tmp.setName((String) ((JSONObject)gp.get(i)).get("name"));
                        tmp.setDate((String) ((JSONObject)gp.get(i)).get("date"));
                        JSONArray standings = ((JSONObject)gp.get(i)).getJSONArray("standings");
                        ArrayList<Standing> sts = new ArrayList<>();
                        for(int j = 0; j < standings.length(); j++){
                            Standing s = new Standing();
                            s.setName((String)((JSONObject)standings.get(j)).get("name"));
                            s.setSecondName((String)((JSONObject)standings.get(j)).get("secondName"));
                            s.setCode((String)((JSONObject)standings.get(j)).get("code"));
                            s.setPos((String)((JSONObject)standings.get(j)).get("pos"));
                            s.setLaps((String)((JSONObject)standings.get(j)).get("laps"));
                            s.setNumber((String)((JSONObject)standings.get(j)).get("number"));
                            s.setConstructor((String)((JSONObject)standings.get(j)).get("constructor"));
                            s.setTime((String)((JSONObject)standings.get(j)).get("time"));

                            sts.add(s);
                        }
                        tmp.setStandings(sts);

                        gps.add(tmp);
                    }

                    g.setPilots(pilots);
                    g.setConstructors(constructors);
                    g.setFastsLap(fls);
                    g.setGrandPrix(gps);
                }catch (Exception ex){}
            }
        }
    }

    /*private void parsePAC(JSONArray pAc, Gestore g) {
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
        }catch (Exception ignored){}
    }*/
}
