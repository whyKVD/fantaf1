package com.example.fantaf1.view;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fantaf1.FormationActivity;
import com.example.fantaf1.MainActivity;
import com.example.fantaf1.R;

public class BottomMenu extends ParentView{

    public Button home, formation;

    public BottomMenu(AppCompatActivity a){
        super(a,R.layout.bottom_menu);

        home = v.findViewById(R.id.home);
        formation = v.findViewById(R.id.formation);

        home.setOnClickListener(view -> {
            if(a.getClass().equals(MainActivity.class))
                return;
            Intent i = new Intent(a, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            a.startActivity(i);
        });
        formation.setOnClickListener(view -> {
            if(a.getClass().equals(FormationActivity.class))
                return;
            a.startActivity(new Intent(a, FormationActivity.class));
        });
    }
}
