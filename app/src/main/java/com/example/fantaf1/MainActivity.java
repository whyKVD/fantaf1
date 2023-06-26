package com.example.fantaf1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fantaf1.buisness_logic.BgTask;
import com.example.fantaf1.buisness_logic.Gestore;

public class MainActivity extends AppCompatActivity {
    private Gestore g = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        g = new Gestore(this);
        new BgTask(g, "readFile","drivers.json");
        Button searchP = findViewById(R.id.searchPilot);
        EditText name = findViewById(R.id.nameTOsearch);
        searchP.setOnClickListener(view -> new BgTask(g,"findPilot",name.getText().toString()));
    }
}