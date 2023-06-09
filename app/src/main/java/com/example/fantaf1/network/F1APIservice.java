package com.example.fantaf1.network;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fantaf1.classes.*;

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
    private final int MINTIME = 1000;
    private String records = "";
    private int responseCode = 0;
    private URL url = null;
    private long start = 0;
    private final ArrayList<Constructor> constructors = new ArrayList<>();

    public F1APIservice(){}

    public ArrayList<Object>  fetchData(){
        start = System.currentTimeMillis();
        interrogateUrl(0,"constructors","https://ergast.com/api/f1/current/constructors.json");
        for(int i = 0; i < constructors.size(); i++){
            start = System.currentTimeMillis();
            interrogateUrl(i,"piloti","https://ergast.com/api/f1/current/constructors/"+constructors.get(i).getConstructorId()+"/drivers.json",constructors.get(i).getConstructorId());
        }
        for(int i = 0; i < pilots.size(); i++){
            start = System.currentTimeMillis();
            interrogateUrl(i,"standings","http://ergast.com/api/f1/current/drivers/"+ pilots.get(i).getDriverId() +"/results.json");
        }
        ArrayList<Object> pilotsANDcons = new ArrayList<>();
        pilotsANDcons.add(pilots);
        pilotsANDcons.add(constructors);
        return pilotsANDcons;
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
                case "constructors":
                    constructorsParsing();
                    break;
                case "piloti":
                        pilotsParsing(q[2]);
                    break;
                case "standings":
                    standingsParsing(i);
                    break;
                default:
                    break;
            }
        }
        url = null;
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

        long delay = MINTIME - (System.currentTimeMillis()-start);
        if(delay > 0){
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
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
