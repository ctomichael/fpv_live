package com.dji.mapkit.core.places;

public class DJIRegeocodeResult {
    private String address;
    private String city;
    private String country;
    private String district;
    private String region;
    private String street;
    private String subStreet;

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country2) {
        this.country = country2;
    }

    public String getRegion() {
        return this.region;
    }

    public void setRegion(String region2) {
        this.region = region2;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city2) {
        this.city = city2;
    }

    public String getDistrict() {
        return this.district;
    }

    public void setDistrict(String district2) {
        this.district = district2;
    }

    public String getStreet() {
        return this.street;
    }

    public void setStreet(String street2) {
        this.street = street2;
    }

    public String getSubStreet() {
        return this.subStreet;
    }

    public void setSubStreet(String subStreet2) {
        this.subStreet = subStreet2;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address2) {
        this.address = address2;
    }

    public String toString() {
        return "DJIRegeocodeResult{country='" + this.country + '\'' + ", region='" + this.region + '\'' + ", city='" + this.city + '\'' + ", district='" + this.district + '\'' + ", street='" + this.street + '\'' + ", subStreet='" + this.subStreet + '\'' + ", address='" + this.address + '\'' + '}';
    }
}
