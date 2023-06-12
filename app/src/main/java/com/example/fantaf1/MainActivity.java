package com.example.fantaf1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button start = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Gestore g = new Gestore(this);
        start = this.findViewById(R.id.start);
        start.setOnClickListener(view -> {
            BgTask task = new BgTask(g,"client","alonso");
        });
    }
}