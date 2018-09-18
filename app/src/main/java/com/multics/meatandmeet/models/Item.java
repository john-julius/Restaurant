package com.multics.meatandmeet.models;

import java.io.Serializable;

public class Item  implements Serializable {

    private int id;
    private String name;
    private String photo;
    private String description;
    private String price;

    public Item(int id, String name, String photo, String description, String price) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.description = description;
        this.price = price;
    }

    public Item(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
