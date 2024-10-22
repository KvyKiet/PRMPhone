package com.example.fellphonef;

public class Product {
    private int id;
    private String name;
    private double price;
    private String details;
    private int imageResId;
    private boolean isSelected; // Track selection state

    public Product(String name, double price, String details, int imageResId) {
        this.name = name;
        this.price = price;
        this.details = details;
        this.imageResId = imageResId;
        this.isSelected = false; // Default to not selected
    }

    public Product(int id, String name, double price, String details, int imageResId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.details = details;
        this.imageResId = imageResId;
        this.isSelected = false; // Default to not selected
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDetails() { return details; }
    public int getImageResId() { return imageResId; }
    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean selected) { this.isSelected = selected; }
}
