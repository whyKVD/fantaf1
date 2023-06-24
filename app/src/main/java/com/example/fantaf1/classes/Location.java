package com.example.fantaf1.classes;

import org.json.JSONObject;

/**
 *
 * @author Alessandro
 */
public class Location {
    private String locality = null,
            country = null;

    public Location(String aLocality, String aCountry) {
        locality = aLocality;
        country = aCountry;
    }

    public Location(JSONObject obj) {
        try {
            locality = obj.get("locality").toString();
            country = obj.get("country").toString();
        }catch(Exception ex){}
    }

    public String getLocality() {
        return locality;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return "Location{" + "locality=" + locality + ", country=" + country + '}';
    }
}
