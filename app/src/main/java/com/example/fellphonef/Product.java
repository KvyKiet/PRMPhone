package com.example.fellphonef;

public class Product {
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

    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDetails() { return details; }
    public int getImageResId() { return imageResId; }
    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean selected) { this.isSelected = selected; }
}
