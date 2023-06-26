package com.example.fantaf1.classes;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.json.JSONObject;

/**
 *
 * @author Alessandro
 */
public class Standing implements Parcelable {
    private String raceName = null,
            pos = null,
            date = null;
    private Circuit circuit = null;

    public Standing(String aRaceName, String aPos, String aDate, Circuit aCircuit) {
        raceName = aRaceName;
        pos = aPos;
        date = aDate;
        circuit = aCircuit;
    }

    public Standing(JSONObject obj) {
        try {
            raceName = obj.get("raceName").toString();
            pos = obj.get("pos").toString();
            date = obj.get("date").toString();
            circuit = new Circuit((JSONObject) obj.get("circuit"));
        }catch (Exception ex){}
    }

    protected Standing(Parcel in) {
        raceName = in.readString();
        pos = in.readString();
        date = in.readString();
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

    public String getRaceName() {
        return raceName;
    }

    public String getPos() {
        return pos;
    }

    public String getDate() {
        return date;
    }

    public Circuit getCircuit() {
        return circuit;
    }

    @NonNull
    @Override
    public String toString() {
        return "Standing{" + "raceName=" + raceName + ", pos=" + pos + ", date=" + date + ", circuit=" + circuit.toString() + '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(raceName);
        parcel.writeString(pos);
        parcel.writeString(date);
    }
}
