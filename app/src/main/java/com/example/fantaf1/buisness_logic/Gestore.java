package com.example.fantaf1.buisness_logic;

import android.content.Intent;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fantaf1.FirstActivity;
import com.example.fantaf1.PilotActivity;
import com.example.fantaf1.R;
import com.example.fantaf1.classes.Constructor;
import com.example.fantaf1.classes.FastestLap;
import com.example.fantaf1.classes.GrandPrix;
import com.example.fantaf1.classes.Pilota;
import com.example.fantaf1.view.BottomMenu;
import com.example.fantaf1.view.FormationCard;

import java.util.ArrayList;

public class Gestore {
    private final AppCompatActivity context;
    private ArrayList<Pilota> pilots = null;
    private ArrayList<Constructor> constructors = null;
    private ArrayList<GrandPrix> grandPrix = null;
    private ArrayList<FastestLap> fastsLap = null;

    public Gestore(AppCompatActivity aContext){
        context = aContext;
        if(!context.getClass().equals(FirstActivity.class))
            new BottomMenu(context);
    }

    public void findPilots(String id){
        for (Pilota p : pilots) {
            if (p.getName().toLowerCase().contains(id.toLowerCase())) {
                startActivityWithParams("pilota", PilotActivity.class, p);
                return;
            }
        }
    }

    public ArrayList<GrandPrix> getGrandPrix() {
        return grandPrix;
    }

    public ArrayList<FastestLap> getFastsLap() {
        return fastsLap;
    }

    public void setGrandPrix(ArrayList<GrandPrix> grandPrix) {
        this.grandPrix = grandPrix;
    }

    public void setFastsLap(ArrayList<FastestLap> fastsLap) {
        this.fastsLap = fastsLap;
    }

    private void startActivityWithParams(String action, Class<?> where, Object... p) {
        Intent i = new Intent(context,where);
        switch (action) {
            case "pilota" -> i.putExtra("pilot", (Pilota) p[0]);
            default -> {
            }
        }
        context.startActivity(i);
    }

    public ArrayList<Pilota> getPilots() {
        return pilots;
    }

    public void setPilots(ArrayList<Pilota> pilots) {
        this.pilots = pilots;
    }

    public ArrayList<Constructor> getConstructors() {
        return constructors;
    }

    public void setConstructors(ArrayList<Constructor> constructors) {
        this.constructors = constructors;
    }

    public AppCompatActivity getContext() {
        return context;
    }

    public void initRow(int row) {
        LinearLayout l = context.findViewById(row);
        for (int i = 0; i < 3; i++) {
            FormationCard f = new FormationCard(context,getPilots());
            l.addView(f.getV());
        }
    }

    public void row(int i) {
        switch (i) {
            case 1 -> initRow(R.id.firstRow);
            case 2 -> initRow(R.id.secondRow);
            case 3 -> initRow(R.id.thirdRow);
            default -> {
            }
        }
    }
}