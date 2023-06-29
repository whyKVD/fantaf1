package com.example.fantaf1.classes;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 *
 * @author Alessandro
 */
public class Standing implements Parcelable {
    private String name = null,
            secondName = null,
            code = null,
            pos = null,
            laps = null,
            number = null,
            constructor = null,
            time = null;

    public Standing() {}

    protected Standing(Parcel in) {
        name = in.readString();
        secondName = in.readString();
        code = in.readString();
        pos = in.readString();
        laps = in.readString();
        number = in.readString();
        constructor = in.readString();
        time = in.readString();
    }

    public static final Creator<Standing> CREATOR = new Creator<Standing>() {
        @Override
        public Standing createFromParcel(Parcel in) {
            return new Standing(in);
        }

        @Override
        public Standing[] newArray(int size) {
            return new Standing[size];
        }
    };

    public void setName(String n) {
        name = n;
    }

    public void setSecondName(String s){
        secondName = s;
    }

    public void setCode(String c){
        code = c;
    }

    public void setPos(String p) {
        pos = p;
    }

    public void setLaps(String l) {
        laps = l;
    }

    public void setNumber(String n) {
        number = n;
    }

    public void setConstructor(String c){
        constructor = c;
    }

    public void setTime(String t){
        time = t;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "Standing{" + "name=" + name + ", secondName=" + secondName + ", code=" + code + ", pos=" + pos + ", laps=" + laps + ", number=" + number + ", constructor=" + constructor + ", time=" + time + '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(secondName);
        parcel.writeString(code);
        parcel.writeString(pos);
        parcel.writeString(laps);
        parcel.writeString(number);
        parcel.writeString(constructor);
        parcel.writeString(time);
    }
}
