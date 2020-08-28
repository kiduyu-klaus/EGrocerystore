package com.kiduyu.kevinproject.e_grocerystore.model;

public class Grocery {
    String name;
     String price;
    String description;
    String date;
    String stock;
    String image;

    public Grocery(){

    }

    public Grocery(String name, String price, String description, String date, String stock, String image) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.date = date;
        this.stock = stock;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
