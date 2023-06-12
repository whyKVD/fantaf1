package com.example.fantaf1;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

public class Gestore {
    private AppCompatActivity context;

    public Gestore(AppCompatActivity aContext){
        context = aContext;
    }

    public AppCompatActivity getContext() {
        return context;
    }
}
