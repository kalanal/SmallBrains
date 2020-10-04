package com.example.gethelp.Consumer;

public class ProfessionalItem {
    private String name;
    private String category;
    private String about;
    private float rating;

    public ProfessionalItem(String name, String category, String about, float rating) {
        this.name = name;
        this.category = category;
        this.about = about;
        this.rating = rating;
    }

    public ProfessionalItem() {
        //Empty constructor needed
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
