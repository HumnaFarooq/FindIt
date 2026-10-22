package com.example.findit;

import java.io.Serializable;

public class Item implements Serializable {
    private String id;
    private String name;
    private String type; // "LOST" or "FOUND"
    private String category;
    private String description;
    private String location;
    private String date;
    private String contact;
    private String imageUri;
    private boolean isUserReport;

    public Item(String id, String name, String type, String category,
                String description, String location, String date,
                String contact, String imageUri, boolean isUserReport) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.category = category;
        this.description = description;
        this.location = location;
        this.date = date;
        this.contact = contact;
        this.imageUri = imageUri;
        this.isUserReport = isUserReport;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public boolean isUserReport() {
        return isUserReport;
    }

    public void setUserReport(boolean userReport) {
        isUserReport = userReport;
    }
}
