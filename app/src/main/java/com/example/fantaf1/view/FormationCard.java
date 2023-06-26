package com.example.fantaf1.view;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fantaf1.R;
import com.example.fantaf1.classes.Pilota;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FormationCard extends ParentView{

    private FloatingActionButton add;
    private TextView name,
                secondName,
                number;
    private ArrayList<Pilota> p = null;
    private ArrayList<DriverCard> dcs= new ArrayList<>();
    private LinearLayout l;

    public FormationCard(AppCompatActivity aContext, ArrayList<Pilota> aP) {
        super(aContext, R.layout.formation_card);
        p = aP;

        l = context.findViewById(R.id.driverRow);
        add = v.findViewById(R.id.add);
        name = v.findViewById(R.id.name);
        secondName = v.findViewById(R.id.secondName);
        number = v.findViewById(R.id.number);

        context.runOnUiThread(() -> add.setOnClickListener(view -> addDriver()));
    }

    private void addDriver() {
        context.runOnUiThread(() -> {
            l.setVisibility(View.VISIBLE);
            for (Pilota tmp : p) {
                dcs.add(new DriverCard(context,tmp));
                l.addView(dcs.get(dcs.size()-1).getV());
                dcs.get(dcs.size()-1).getV().setOnClickListener(view -> clicked(dcs.get(dcs.size()-1).getV()));
            }
            add.setVisibility(View.INVISIBLE);
            add.setEnabled(false);
        });
    }

    private void clicked(View v) {
        context.runOnUiThread(() -> {
            name.setText(((TextView) v.findViewById(R.id.name)).getText().toString());
            secondName.setText(((TextView) v.findViewById(R.id.secondName)).getText().toString());
            number.setText(((TextView) v.findViewById(R.id.number)).getText().toString());
            l.setVisibility(View.INVISIBLE);
        });
    }
}
