package com.example.fantaf1.network;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fantaf1.R;
import com.example.fantaf1.buisness_logic.Gestore;
import com.example.fantaf1.classes.Circuit;
import com.example.fantaf1.classes.Constructor;
import com.example.fantaf1.classes.Location;
import com.example.fantaf1.classes.Pilota;
import com.example.fantaf1.classes.Standing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class F1APIservice {
    private final ArrayList<Pilota> pilots = new ArrayList<>();
    private ArrayList<Standing> standings = null;
    private final int MINTIME = 270;
    private String records = "";
    private int responseCode = 0;
    private URL url = null;
    private long start = 0;
    private final ArrayList<Constructor> constructors = new ArrayList<>();

    private final AppCompatActivity context;
    private final Gestore g;

    public F1APIservice(Gestore aG){
        g = aG;
        context = aG.getContext();
    }

    public ArrayList<Object>  fetchData(){
        start = System.currentTimeMillis();
        sendMessage("scaricando i costruttori...");
        interrogateUrl(0,"constructors","https://ergast.com/api/f1/current/constructors.json");
        sendMessage("scaricando i piloti...");
        for(int i = 0; i < constructors.size(); i++){
            start = System.currentTimeMillis();
            interrogateUrl(i,"piloti","https://ergast.com/api/f1/current/constructors/"+constructors.get(i).getConstructorId()+"/drivers.json",constructors.get(i).getConstructorId());
        }
        sendMessage("scaricando i piazzamenti...");
        for(int i = 0; i < pilots.size(); i++){
            start = System.currentTimeMillis();
            interrogateUrl(i,"standings","https://ergast.com/api/f1/current/drivers/"+ pilots.get(i).getDriverId() +"/results.json");
        }
        ArrayList<Object> pilotsANDcons = new ArrayList<>();
        pilotsANDcons.add(pilots);
        pilotsANDcons.add(constructors);
        return pilotsANDcons;
    }

    public ArrayList<Object> updateStandings() {
        sendMessage("aggiornando i piazzamenti...");
        for(int i = 0; i < g.getPilots().size(); i++){
            start = System.currentTimeMillis();
            interrogateUrl(i,"updatestandings","https://ergast.com/api/f1/current/drivers/"+ g.getPilots().get(i).getDriverId() +"/results.json");
        }
        ArrayList<Object> pilotsANDcons = new ArrayList<>();
        pilotsANDcons.add(pilots);
        pilotsANDcons.add(constructors);
        return pilotsANDcons;
    }

    private void sendMessage(String aMessage) {
        context.runOnUiThread(()->{
            TextView t = context.findViewById(R.id.message);
            t.setText(aMessage);
        });
    }

    private void interrogateUrl(int i,String... q){
        try{
            url = new URL(q[1]);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            //Getting the response code
            responseCode = conn.getResponseCode();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (responseCode != 200) {
                records += "CONNECTION FAILED:\t" + responseCode;
            } else if (url != null) {
            switch (q[0]) {
                case "constructors" -> constructorsParsing();
                
                case "piloti" -> pilotsParsing(q[2]);
                
                case "standings" -> standingsParsing(i);
                
                case "updatestandings" -> updateS(i);
                default -> {}
            }
        }
        url = null;
    }

    private void updateS(int i) {
        try {
            JSONObject obj = parseJson();
            //Get the required object from the above created object

            JSONArray races = (JSONArray) (getObject(getObject(obj, "MRData"), "RaceTable")).get("Races");

            g.getPilots().get(i).addStanding(addS((JSONObject) races.get(races.length() - 1)));
        }catch(Exception ex){}

        long delay = MINTIME - (System.currentTimeMillis()-start);
        if(delay > 0){
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex){}
        }
    }
    private static Standing addS(JSONObject obj){
        try {
            return new Standing(obj.get("raceName").toString(),
                    ((JSONObject) ((JSONArray) obj.get("Results")).get(0)).get("position").toString(),
                    obj.get("date").toString(),
                    new Circuit(((JSONObject) obj.get("Circuit")).get("circuitId").toString(),
                            ((JSONObject) obj.get("Circuit")).get("circuitName").toString(),
                            new Location(((JSONObject) ((JSONObject) obj.get("Circuit")).get("Location")).get("locality").toString(),
                                    ((JSONObject) ((JSONObject) obj.get("Circuit")).get("Location")).get("country").toString()
                            )
                    )
            );
        }catch(Exception ex){
            return null;
        }
    }

    private JSONObject parseJson(){
        JSONObject data_obj = null;
        Scanner scanner;
        records = "";
        try {
            scanner = new Scanner(url.openStream());//Write all the JSON data into a string using a scanner
            while (scanner.hasNext()) {
                records += scanner.nextLine();
            }

            //Close the scanner
            scanner.close();

            //Using the JSON simple library parse the string into a json object
            data_obj = new JSONObject(records);
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        }

        return data_obj;
    }

    private void pilotsParsing(String constructor){
        JSONObject obj = parseJson();
        //Get the required object from the above created object

        JSONArray drivers = null;
        try {
            drivers = (JSONArray)(getObject(getObject(obj,"MRData"),"DriverTable")).get("Drivers");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int j = 0; j < drivers.length(); j++) {
            try {
                addPilot((JSONObject) drivers.get(j), constructor);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        long delay = MINTIME - (System.currentTimeMillis()-start);
        if(delay > 0){
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void standingsParsing(int i){
        standings = new ArrayList<>();
        JSONObject obj = parseJson();
        //Get the required object from the above created object

        JSONArray races = null;
        try {
            races = (JSONArray) (getObject(getObject(obj,"MRData"),"RaceTable")).get("Races");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int j = 0; j < races.length(); j++) {
            try {
                addStanding((JSONObject) races.get(j));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        long delay = MINTIME - (System.currentTimeMillis()-start);
        if(delay > 0){
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        pilots.get(i).setStandings(standings);
        standings = null;
    }

    private void constructorsParsing() {
        JSONObject obj = parseJson();

        JSONArray constrs = null;
        try {
            constrs = (JSONArray)(getObject(getObject(obj,"MRData"),"ConstructorTable")).get("Constructors");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int j = 0; j < constrs.length(); j++) {
            try {
                addConstructor((JSONObject) constrs.get(j));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private JSONObject getObject(JSONObject obj, String key) throws JSONException {
        return (JSONObject)obj.get(key);
    }

    private void addPilot(JSONObject obj, String constructor){
        try {
            pilots.add(new Pilota(obj.get("driverId").toString(),
                            constructor,
                            obj.get("code").toString(),
                            Integer.parseInt(obj.get("permanentNumber").toString()),
                            obj.get("nationality").toString(),
                            obj.get("givenName").toString(),
                            obj.get("familyName").toString(),
                            obj.get("dateOfBirth").toString()
                    )
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addStanding(JSONObject obj){
        try {
            standings.add(new Standing(obj.get("raceName").toString(),
                            ((JSONObject)((JSONArray)obj.get("Results")).get(0)).get("position").toString(),
                            obj.get("date").toString(),
                            new Circuit(((JSONObject)obj.get("Circuit")).get("circuitId").toString(),
                                    ((JSONObject)obj.get("Circuit")).get("circuitName").toString(),
                                    new Location(((JSONObject)((JSONObject)obj.get("Circuit")).get("Location")).get("locality").toString(),
                                            ((JSONObject)((JSONObject)obj.get("Circuit")).get("Location")).get("country").toString()
                                    )
                            )
                    )
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addConstructor(JSONObject obj) {
        try {
            constructors.add(new Constructor(obj.get("constructorId").toString(),
                            obj.get("name").toString(),
                            obj.get("nationality").toString()
                    )
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
