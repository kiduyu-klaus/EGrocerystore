package com.kiduyu.kevinproject.e_grocerystore.model;

public class Data {

    String image;
    String name;
    String owner;
    String location;
    String description;
    String date_added;
    String owner_phoneNumber;

    public Data(){

    }

    public Data(String image, String name, String owner, String location, String description, String date_added, String owner_phoneNumber) {
        this.image = image;
        this.name = name;
        this.owner = owner;
        this.location = location;
        this.description = description;
        this.date_added = date_added;
        this.owner_phoneNumber = owner_phoneNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public String getOwner_phoneNumber() {
        return owner_phoneNumber;
    }

    public void setOwner_phoneNumber(String owner_phoneNumber) {
        this.owner_phoneNumber = owner_phoneNumber;
    }
}
