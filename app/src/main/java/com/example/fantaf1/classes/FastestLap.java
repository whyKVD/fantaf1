package com.example.fantaf1.classes;

public class FastestLap {
    private String grandPrix,
            driver,
            car,
            time;

    public FastestLap() {
    }

    public void setGrandPrix(String grandPrix) {
        this.grandPrix = grandPrix;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "FastestLap{" + "grandPrix=" + grandPrix + ", driver=" + driver + ", car=" + car + ", time=" + time + '}';
    }
}
