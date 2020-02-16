package com.appcook.user.findhospitalsandpharmacies.Model;


import java.io.Serializable;

public class DistanceService implements Serializable{

    private Double distance;
    private Service service;

    public DistanceService() {
        this.distance = 0.0;
        this.service = new Service();
    }

    public DistanceService(Double distance, Service service) {
        this.distance = distance;
        this.service = service;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    @Override
    public String toString() {
        return "DistanceService{" +
                "distance=" + distance +
                ", service=" + service +
                '}';
    }

}
