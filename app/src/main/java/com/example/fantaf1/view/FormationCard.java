package com.example.fantaf1.view;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fantaf1.R;
import com.example.fantaf1.classes.Pilota;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FormationCard extends ParentView{

    private final FloatingActionButton add;
    private final TextView name,
                secondName,
                number;
    private final ArrayList<Pilota> p;
    private final ArrayList<DriverCard> dcs= new ArrayList<>();
    private final LinearLayout l;

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
                dcs.get(dcs.size()-1).getV().setOnClickListener(view -> {
                    for (DriverCard d : dcs) {
                        if (d.getV().equals(view))
                            clicked(view);
                    }
                });
            }
            add.setVisibility(View.INVISIBLE);
            add.setEnabled(false);
        });
    }

    private void clicked(View view) {
        context.runOnUiThread(() -> {
            ((TextView)v.findViewById(R.id.name)).setText(((TextView) view.findViewById(R.id.name)).getText().toString());
            //name.setText(((TextView) view.findViewById(R.id.name)).getText().toString());
            ((TextView)v.findViewById(R.id.secondName)).setText(((TextView) view.findViewById(R.id.secondName)).getText().toString());
            number.setText(((TextView) view.findViewById(R.id.number)).getText().toString());
            l.setVisibility(View.INVISIBLE);
        });
    }
}
