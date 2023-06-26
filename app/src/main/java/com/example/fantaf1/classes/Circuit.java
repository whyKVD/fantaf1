package com.example.fantaf1.classes;


import androidx.annotation.NonNull;

import org.json.JSONObject;

/**
 *
 * @author Alessandro
 */
public class Circuit {
    private String circuitId = null,
            circuitName = null;
    private Location location = null;

    public Circuit(String aCircuitId, String aCircuitName, Location aLocation) {
        circuitId = aCircuitId;
        circuitName = aCircuitName;
        location = aLocation;
    }

    public Circuit(JSONObject obj){
        try {
            circuitId = obj.get("circuitId").toString();
            circuitName = obj.get("circuitName").toString();
            location = new Location((JSONObject) obj.get("location"));
        }catch (Exception ignored){}
    }

    public String getRaceId() {
        return circuitId;
    }

    public String getCircuitName() {
        return circuitName;
    }

    public Location getLocation() {
        return location;
    }

    @NonNull
    @Override
    public String toString() {
        return "Circuit{" + "raceId=" + circuitId + ", circuitName=" + circuitName + ", location=" + location.toString() + '}';
    }
}
