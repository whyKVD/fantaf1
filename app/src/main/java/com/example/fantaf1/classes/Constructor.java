package com.example.fantaf1.classes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 *
 * @author Alessandro
 */
public class Constructor implements Parcelable {
    String name,
            base,
            teamChief,
            techChief,
            chassis,
            powUnit,
            firstTeamEntry,
            worldChamps,
            polePos,
            fastestlap;

    public Constructor() {
    }

    protected Constructor(Parcel in) {
        name = in.readString();
        base = in.readString();
        teamChief = in.readString();
        techChief = in.readString();
        chassis = in.readString();
        powUnit = in.readString();
        firstTeamEntry = in.readString();
        worldChamps = in.readString();
        polePos = in.readString();
        fastestlap = in.readString();
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

    public void setBase(String base) {
        this.base = base;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeamChief(String teamChief) {
        this.teamChief = teamChief;
    }

    public void setTechChief(String techChief) {
        this.techChief = techChief;
    }

    public void setChassis(String chassis) {
        this.chassis = chassis;
    }

    public void setPowUnit(String powUnit) {
        this.powUnit = powUnit;
    }

    public void setFirstTeamEntry(String firstTeamEntry) {
        this.firstTeamEntry = firstTeamEntry;
    }

    public void setWorldChamps(String worldChamps) {
        this.worldChamps = worldChamps;
    }

    public void setPolePos(String polePos) {
        this.polePos = polePos;
    }

    public void setFastestlap(String fastestlap) {
        this.fastestlap = fastestlap;
    }

    @Override
    public String toString() {
        return "Constructor{" + "name=" + name + ", base=" + base + ", teamChief=" + teamChief + ", techChief=" + techChief + ", chassis=" + chassis + ", powUnit=" + powUnit + ", firstTeamEntry=" + firstTeamEntry + ", worldChamps=" + worldChamps + ", polePos=" + polePos + ", fastestlap=" + fastestlap + '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(base);
        parcel.writeString(teamChief);
        parcel.writeString(techChief);
        parcel.writeString(chassis);
        parcel.writeString(powUnit);
        parcel.writeString(firstTeamEntry);
        parcel.writeString(worldChamps);
        parcel.writeString(polePos);
        parcel.writeString(fastestlap);
    }
}

