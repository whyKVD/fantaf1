package com.example.fantaf1.classes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *
 * @author Alessandro
 */
public class Pilota implements Parcelable {
    private String constructor = null;

    private String code = null;
    private String birth_date = null;
    private String nationality = null;
    private String name = null;
    private String second_name = null;
    private String driverId = null;
    private int perm_num = 0,
                punti = 0;
    private ArrayList<Standing> standings = new ArrayList<>();

    protected Pilota(Parcel in) {
        constructor = in.readString();
        code = in.readString();
        birth_date = in.readString();
        nationality = in.readString();
        name = in.readString();
        second_name = in.readString();
        driverId = in.readString();
        perm_num = in.readInt();
        punti = in.readInt();
    }

    public static final Creator<Pilota> CREATOR = new Creator<Pilota>() {
        @Override
        public Pilota createFromParcel(Parcel in) {
            return new Pilota(in);
        }

        @Override
        public Pilota[] newArray(int size) {
            return new Pilota[size];
        }
    };

    public void setStandings(ArrayList<Standing> standings) {
        this.standings = standings;
    }
    public void setStandings(JSONArray sts) {
        for (int i = 0; i < sts.length(); i++) {
            try {
                JSONObject obj = (JSONObject) sts.get(i);
                Standing s = new Standing(obj);
                standings.add(s);
            }catch (Exception ignored){}
        }
    }

    public void addStanding(Standing aStanding){
        standings.add(aStanding);
    }


    public ArrayList<Standing> getStandings() {
        return standings;
    }

    /**
     *  the basic contructor
     */
    public Pilota(String aDriverId, String aConstructor, String aCode,  int aPerm_num, String aNationality, String aName, String aSecond_name, String aBirth_date) {
        driverId = aDriverId;
        constructor = aConstructor;
        code = aCode;
        birth_date = aBirth_date;
        nationality = aNationality;
        name = aName;
        second_name = aSecond_name;
        perm_num = aPerm_num;
    }

    public Pilota(JSONObject obj){
        try {
            driverId = obj.get("driverId").toString();
            constructor = obj.get("constructor").toString();
            code = obj.get("code").toString();
            birth_date = obj.get("birth_date").toString();
            nationality = obj.get("nationality").toString();
            name = obj.get("name").toString();
            second_name = obj.get("second_name").toString();
            perm_num = Integer.parseInt(obj.get("perm_num").toString());
            setStandings(obj.getJSONArray("standings"));
        }catch (Exception ignored){}
    }

    public void addPoint(JSONObject result) {
        try {
            if ((boolean)result.get("giroVeloce"))
                punti += 5;
            switch ((int)result.get("pos")){
                case 1: punti += 25;
                    break;
                case 2: punti += 18;
                    break;
                case 3: punti += 15;
                    break;
                case 4: punti += 12;
                    break;
                case 5: punti += 10;
                    break;
                case 6: punti += 8;
                    break;
                case 7: punti += 6;
                    break;
                case 8: punti += 4;
                    break;
                case 9: punti += 2;
                    break;
                case 10: punti += 1;
                    break;
                case -1: punti -= 20;
                    break;
                default:
                    break;
            }
        }catch (Exception ignored){}
    }

    public String getConstructor() {
        return constructor;
    }

    public String getCode() {
        return code;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public String getNationality() {
        return nationality;
    }

    public String getName() {
        return name;
    }

    public String getSecond_name() {
        return second_name;
    }

    public String getDriverId() {
        return driverId;
    }

    public int getPerm_num() {
        return perm_num;
    }

    @NonNull
    @Override
    public String toString() {
        return "Pilota{" + "constructor=" + constructor + ", code=" + code + ", birth_date=" + birth_date + ", nationality=" + nationality + ", name=" + name + ", second_name=" + second_name + ", driverId=" + driverId + ", perm_num=" + perm_num + '}';
    }

    public int getPunti() {
        return punti;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(constructor);
        parcel.writeString(code);
        parcel.writeString(birth_date);
        parcel.writeString(nationality);
        parcel.writeString(name);
        parcel.writeString(second_name);
        parcel.writeString(driverId);
        parcel.writeInt(perm_num);
        parcel.writeInt(punti);
    }
}

