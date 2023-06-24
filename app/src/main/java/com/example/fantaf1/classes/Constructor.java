package com.example.fantaf1.classes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.json.JSONObject;

/**
 *
 * @author Alessandro
 */
public class Constructor implements Parcelable {
    private String constructorId = null,
            name = null,
            nationality = null;

    public Constructor(String aConstructorId, String aName, String aNationality) {
        constructorId = aConstructorId;
        name = aName;
        nationality = aNationality;
    }

    public Constructor(JSONObject obj){
        try {
            constructorId = obj.get("constructorId").toString();
            name = obj.get("name").toString();
            nationality = obj.get("nationality").toString();
        }catch (Exception ex){}
    }

    protected Constructor(Parcel in) {
        constructorId = in.readString();
        name = in.readString();
        nationality = in.readString();
    }

    public static final Creator<Constructor> CREATOR = new Creator<Constructor>() {
        @Override
        public Constructor createFromParcel(Parcel in) {
            return new Constructor(in);
        }

        @Override
        public Constructor[] newArray(int size) {
            return new Constructor[size];
        }
    };

    public String getConstructorId() {
        return constructorId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Constructor{" + "constructorId=" + constructorId + ", name=" + name + '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(constructorId);
        parcel.writeString(name);
        parcel.writeString(nationality);
    }
}

