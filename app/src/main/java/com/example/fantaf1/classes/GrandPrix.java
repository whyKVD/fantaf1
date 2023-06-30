package com.example.fantaf1.classes;

import java.util.ArrayList;

public class GrandPrix {
    private String url,
            name,
            date;

    private ArrayList<Standing> standings;

    public GrandPrix() {}

    public void setUrl(String url) {
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public ArrayList<Standing> getStandings() {
        return standings;
    }

    public void setStandings(ArrayList<Standing> standings) {
        this.standings = standings;
    }

    public Standing getStandingFromPos(String num){
        for(Standing s : standings){
            if(s.getNumber().equals(num))
                return s;
        }
        return null;
    }

    @Override
    public String toString() {
        return "GrandPrix{" + "url=" + url + ", name=" + name + ", date=" + date + ", standings=" + standings.toString() + '}';
    }
}
