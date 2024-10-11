package com.example.fellphonef;

public class Product {
    private String name;
    private double price;
    private String details;
    private int imageResId;

    public Product(String name, double price, String details, int imageResId) {
        this.name = name;
        this.price = price;
        this.details = details;
        this.imageResId = imageResId;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDetails() { return details; }
    public int getImageResId() { return imageResId; }
}
