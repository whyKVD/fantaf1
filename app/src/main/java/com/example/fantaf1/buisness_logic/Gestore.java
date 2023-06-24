package com.example.fantaf1.buisness_logic;

import android.content.Intent;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fantaf1.PilotActivity;
import com.example.fantaf1.classes.Constructor;
import com.example.fantaf1.classes.Pilota;
import com.example.fantaf1.classes.Standing;
import com.example.fantaf1.view.BottomMenu;

import java.util.ArrayList;

public class Gestore {
    private AppCompatActivity context;
    private ArrayList<Pilota> pilots = null;
    private ArrayList<Constructor> constructors = null;
    private Button home = null;
    private Button formation = null;
    private final BottomMenu bm;

    public Gestore(AppCompatActivity aContext){
        context = aContext;
        bm = new BottomMenu(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        context.runOnUiThread(() -> context.addContentView(bm.getV(), params));
    }

    public void findPilots(String id){
        for (Pilota p : pilots) {
            context.runOnUiThread(() -> Toast.makeText(context, p.getDriverId(), Toast.LENGTH_SHORT).show());
            if (p.getDriverId().equalsIgnoreCase(id) | p.getName().equalsIgnoreCase(id) | p.getSecond_name().equalsIgnoreCase(id))
                for (Constructor c : constructors) {
                    if (c.getConstructorId().equalsIgnoreCase(p.getConstructor())) {
                        startActivity("pilota", PilotActivity.class, p, c, p.getStandings());
                        return;
                    }
                }
        }
    }

    private void startActivity(String action,Class<?> where,Object... p) {
        Intent i = new Intent(context,where);
        switch (action) {
            case "pilota":
                i.putExtra("pilot", (Pilota) p[0]);
                i.putExtra("constructor", ((Constructor) p[1]).getName());
                i.putParcelableArrayListExtra("standings", ((ArrayList<Standing>) p[2]));
                break;
            default:
                break;
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
}