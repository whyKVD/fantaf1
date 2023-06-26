package com.example.fantaf1;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fantaf1.buisness_logic.BgTask;
import com.example.fantaf1.buisness_logic.Gestore;

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