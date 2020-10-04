package com.example.gethelp.Admin;

public class UserApprovalItem {
    String about,category,name;

    public UserApprovalItem() {
        //Empty constructor needed
    }

    public UserApprovalItem(String about, String category, String name) {
        this.about = about;
        this.category = category;
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }
}
