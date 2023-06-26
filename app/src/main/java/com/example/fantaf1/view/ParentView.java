package com.example.fantaf1.view;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public abstract class ParentView {
    protected View v = null;
    protected AppCompatActivity context;

    public ParentView(AppCompatActivity aContext, int aLayoutId){
        v = aContext.getLayoutInflater().inflate(aLayoutId,null);
        context = aContext;
    }

    public View getV() {
        return v;
    }
}
