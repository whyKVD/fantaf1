package com.example.fantaf1.view;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public abstract class ParentView {
    protected View v = null;

    public ParentView(AppCompatActivity aContext, int aLayoutId){
        v = aContext.getLayoutInflater().inflate(aLayoutId,null);
    }

    public View getV() {
        return v;
    }
}
