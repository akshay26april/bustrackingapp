package com.example.bustracking;

public class Model {
    String name, number;
    private double latitude, longitude;

    public Model(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Model(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public Character getProfileLetter(){
        return name.charAt(0);
    }
}
