package com.example.fantaf1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.fantaf1.buisness_logic.BgTask;
import com.example.fantaf1.buisness_logic.Gestore;
import com.example.fantaf1.classes.Pilota;
import com.example.fantaf1.view.FormationCard;

import java.util.ArrayList;

public class FormationActivity extends AppCompatActivity {

    Gestore g = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formation);
        g = new Gestore(this);
        new BgTask(g, "readFile","drivers.json");
    }
}