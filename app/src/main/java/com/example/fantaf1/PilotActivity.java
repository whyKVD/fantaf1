package com.example.fantaf1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fantaf1.buisness_logic.Gestore;
import com.example.fantaf1.classes.Pilota;
import com.example.fantaf1.classes.Standing;
import com.example.fantaf1.view.StandingCard;

import java.util.ArrayList;

public class PilotActivity extends AppCompatActivity {
    private Pilota p = null;
    private String cons;

    private ArrayList<Standing> standings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilot);
        Gestore g = new Gestore(this);
        LinearLayout stScroll = findViewById(R.id.standings);
        getExtra();
        TextView name = findViewById(R.id.name);
        TextView second_name = findViewById(R.id.secondName);
        TextView permNum = findViewById(R.id.permNum);
        TextView constructor = findViewById(R.id.constructor);
        TextView nationality = findViewById(R.id.nationality);

        name.setText(p.getName());
        second_name.setText(p.getSecond_name());
        permNum.setText(String.valueOf(p.getPerm_num()));
        constructor.setText(cons);
        nationality.setText(p.getNationality());
        for (Standing s : standings) {
            StandingCard sc = new StandingCard(this,s.getRaceName(),s.getPos());
            stScroll.addView(sc.getV());
        }
    }

    private void getExtra() {
        Intent i = getIntent();

        Bundle extras = i.getExtras();
        p = (Pilota)extras.getParcelable("pilot");
        cons = extras.getString("constructor");
        standings = extras.getParcelableArrayList("standings");
    }
}