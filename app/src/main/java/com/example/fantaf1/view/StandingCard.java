package com.example.fantaf1.view;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fantaf1.R;

public class StandingCard extends ParentView{

    public StandingCard(AppCompatActivity aContext, String aRaceName, String aPosition) {
        super(aContext, R.layout.standing_card);

        TextView raceName = v.findViewById(R.id.raceName);
        TextView position = v.findViewById(R.id.position);

        raceName.setText(aRaceName);
        position.setText(aPosition);
    }
}
