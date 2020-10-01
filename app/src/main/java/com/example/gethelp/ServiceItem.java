package com.example.gethelp;

public class ServiceItem {
    private String id;
    private String name;
    private String category;
    private String about;
    private float rating;

    public ServiceItem(String name, String category, String about, float rating) {
        this.name = name;
        this.category = category;
        this.about = about;
        this.rating = rating;
    }

    public ServiceItem() {
        //Empty constructor needed
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getAbout() {
        return about;
    }

    public float getRating() {
        return rating;
    }
}
