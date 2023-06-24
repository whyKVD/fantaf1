package com.example.fantaf1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Constraints;

import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.fantaf1.buisness_logic.BgTask;
import com.example.fantaf1.buisness_logic.Gestore;
import com.example.fantaf1.R;

public class MainActivity extends AppCompatActivity {
    private Gestore g = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        g = new Gestore(this);
        //new BgTask(g, "client");
        new BgTask(g, "readFile","drivers.json");
        Button searchP = (Button) findViewById(R.id.searchPilot);
        EditText name = (EditText) findViewById(R.id.nameTOsearch);

        searchP.setOnClickListener(view -> new BgTask(g,"findPilot",name.getText().toString()));
    }
}