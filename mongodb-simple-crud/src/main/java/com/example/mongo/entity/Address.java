package com.example.mongo.entity;

/**
 * An address. In MongoDB it is NOT a separate table: it is stored INSIDE the student document
 * (an "embedded document"). One read gives you the student and the address together, without a JOIN.
 *
 * PROVIDED CODE - students do not need to change it.
 */
public class Address {

    private String city;
    private String country;

    public Address() {
    }

    public Address(String city, String country) {
        this.city = city;
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return city + ", " + country;
    }
}
