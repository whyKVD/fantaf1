package com.example.fantaf1.view;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fantaf1.R;

public class StandingCard extends ParentView{

    private TextView raceName,
                    position;
    public StandingCard(AppCompatActivity aContext, String aRaceName, String aPosition) {
        super(aContext, R.layout.standing_card);

        raceName = v.findViewById(R.id.raceName);
        position = v.findViewById(R.id.position);

        raceName.setText(aRaceName);
        position.setText(aPosition);
    }
}
