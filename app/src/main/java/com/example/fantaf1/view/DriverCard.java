package com.example.fantaf1.view;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fantaf1.R;
import com.example.fantaf1.classes.Pilota;

public class DriverCard extends ParentView{
    TextView name,
            secondName,
            number;
    public DriverCard(AppCompatActivity aContext, Pilota aP) {
        super(aContext, R.layout.drivers_card);

        name = v.findViewById(R.id.name);
        secondName = v.findViewById(R.id.secondName);
        number = v.findViewById(R.id.number);

        name.setText(aP.getName());
        secondName.setText(aP.getSecond_name());
        number.setText(String.valueOf(aP.getPerm_num()));
    }
}
