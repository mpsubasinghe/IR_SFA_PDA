package com.example.mano.sfa;

/**
 * Created by MANO on 09/07/2019.
 */

public class Product {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        this.Price = price;
    }

    private int id;
    private String Name;
    private String Description;
    private double Price;

    public static final String PRODUCT_ID = "id";
    public static final String PRODUCT_NAME = "Name";
}

