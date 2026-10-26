package com.example.mongo.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * A student. In MongoDB this object is stored as ONE document, like this (JSON):
 *
 *   {
 *     "_id": "6650f1c2a1b2c3d4e5f60718",          <- made by MongoDB. Here it is the field "id", as text
 *     "firstName": "John",
 *     "lastName": "Smith",
 *     "email": "john@test.com",
 *     "age": 21,
 *     "address": { "city": "Paris", "country": "France" },      <- a document INSIDE the document
 *     "tags": ["java", "mongodb"]                               <- a list INSIDE the document
 *   }
 *
 * PROVIDED CODE - students do not need to change it.
 */
public class Student {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private int age;
    private Address address;
    private List<String> tags = new ArrayList<>();

    public Student() {
    }

    public Student(String firstName, String lastName, String email, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
    }

    public Student(String firstName, String lastName, String email, int age, Address address, List<String> tags) {
        this(firstName, lastName, email, age);
        this.address = address;
        this.tags = new ArrayList<>(tags);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Student{id=" + id + ", " + firstName + " " + lastName + ", " + email + ", age=" + age
                + ", address=" + address + ", tags=" + tags + "}";
    }
}
