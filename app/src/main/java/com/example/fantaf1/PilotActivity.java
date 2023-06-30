package com.example.fantaf1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fantaf1.buisness_logic.BgTask;
import com.example.fantaf1.buisness_logic.Gestore;
import com.example.fantaf1.classes.Pilota;
import com.example.fantaf1.classes.Standing;

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
        new BgTask(g,"readFile","grandPrix.json");
        getExtra();
        TextView name = findViewById(R.id.name);
        TextView permNum = findViewById(R.id.permNum);
        TextView constructor = findViewById(R.id.constructor);
        TextView nationality = findViewById(R.id.nationality);
        TextView podi = findViewById(R.id.podi);
        TextView granPremi = findViewById(R.id.granPremi);
        TextView campionati = findViewById(R.id.campionati);

        name.setText(name.getText()+p.getName());
        permNum.setText(permNum.getText()+p.getNumber());
        constructor.setText(constructor.getText()+p.getTeam());
        nationality.setText(nationality.getText()+p.getNationality());
        podi.setText(podi.getText()+p.getPodiums());
        granPremi.setText(granPremi.getText()+p.getGrandPrixEntered());
        campionati.setText(campionati.getText()+p.getWorldChamps());
    }

    private void getExtra() {
        Intent i = getIntent();

        Bundle extras = i.getExtras();
        p = (Pilota)extras.getParcelable("pilot");
    }
}