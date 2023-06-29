package com.example.fantaf1.classes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 *
 * @author Alessandro
 */
public class Pilota implements Parcelable {
    private String name,
            number,
            team,
            nationality,
            podiums,
            grandPrixEntered,
            worldChamps,
            dateOfBirth,
            placeOfBirth;

    public Pilota() {}

    protected Pilota(Parcel in) {
        name = in.readString();
        number = in.readString();
        team = in.readString();
        nationality = in.readString();
        podiums = in.readString();
        grandPrixEntered = in.readString();
        worldChamps = in.readString();
        dateOfBirth = in.readString();
        placeOfBirth = in.readString();
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

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getTeam() {
        return team;
    }

    public String getNationality() {
        return nationality;
    }

    public String getPodiums() {
        return podiums;
    }

    public String getGrandPrixEntered() {
        return grandPrixEntered;
    }

    public String getWorldChamps() {
        return worldChamps;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setPodiums(String podiums) {
        this.podiums = podiums;
    }

    public void setGrandPrixEntered(String grandPrixEntered) {
        this.grandPrixEntered = grandPrixEntered;
    }

    public void setWorldChamps(String worldChamps) {
        this.worldChamps = worldChamps;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    @Override
    public String toString() {
        return "Pilota{" + "name=" + name + ", number=" + number + ", team=" + team + ", nationality=" + nationality + ", podiums=" + podiums + ", grandPrixEntered=" + grandPrixEntered + ", worldChamps=" + worldChamps + ", dateOfBirth=" + dateOfBirth + ", placeOfBirth=" + placeOfBirth + '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(number);
        parcel.writeString(team);
        parcel.writeString(nationality);
        parcel.writeString(podiums);
        parcel.writeString(grandPrixEntered);
        parcel.writeString(worldChamps);
        parcel.writeString(dateOfBirth);
        parcel.writeString(placeOfBirth);
    }
}

