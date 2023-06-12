package com.example.fantaf1;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public abstract class Char implements Parcelable {
    protected Scuderia scuderia;
    protected int punti;

    public Char(Scuderia aScuderia){
        scuderia = aScuderia;
        punti=0;
    }

    protected Char(Parcel in) {
        punti = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(punti);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Char> CREATOR = new Creator<Char>() {
        @Override
        public Char createFromParcel(Parcel in) {
            return new Char(in) {
                @Override
                public void addPoint(JSONObject result) {
                    
                }
            };
        }

        @Override
        public Char[] newArray(int size) {
            return new Char[size];
        }
    };

    public abstract void addPoint(JSONObject result);
}
