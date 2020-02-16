package com.appcook.user.findhospitalsandpharmacies.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable{

    private Location location;
    private List<DistanceService> distanceService;

    public User() {
        this.location = new Location();
        this.distanceService = new ArrayList<>();
    }

    public User(Location location, List<DistanceService> distanceService) {
        this.location = location;
        this.distanceService = distanceService;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<DistanceService> getDistanceService() {
        return distanceService;
    }

    public void setDistanceService(List<DistanceService> distanceService) {
        this.distanceService = distanceService;
    }

    @Override
    public String toString() {
        return "User{" +
                "location=" + location +
                ", distanceService=" + distanceService +
                '}';
    }
}
