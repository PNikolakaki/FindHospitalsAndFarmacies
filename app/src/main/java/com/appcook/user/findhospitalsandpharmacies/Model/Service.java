package com.appcook.user.findhospitalsandpharmacies.Model;

import java.io.Serializable;

public class Service implements Serializable{

    private String name;
    private String address;
    private String type;
    private Location location;

    public Service() {
        this.name = "-";
        this.address = "-";
        this.type = "-";
        this.location = null;
    }

    public Service(String name, String address, String type, Location location) {
        this.name = name;
        this.address = address;
        this.type = type;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Service{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", type='" + type + '\'' +
                ", location=" + location +
                '}';
    }
}
