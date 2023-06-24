package com.example.fantaf1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;

import com.example.fantaf1.buisness_logic.Gestore;
import com.example.fantaf1.R;

public class FormationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formation);
        Gestore g = new Gestore(this);
    }
}