package com.appcook.user.findhospitalsandpharmacies.Model;

import java.io.Serializable;

public class Location implements Serializable{

    private double latitude;
    private double longtitude;

    public Location() {
        this.latitude = 40.632022;
        this.longtitude = 22.951696;
    }

    public Location(double latitude, double longtitude) {
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    @Override
    public String toString() {
        return "Location{" +
                "latitude=" + latitude +
                ", longtitude=" + longtitude +
                '}';
    }
}
