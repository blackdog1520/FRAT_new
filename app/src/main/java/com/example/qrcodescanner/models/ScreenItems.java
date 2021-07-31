package com.example.qrcodescanner.models;

public class ScreenItems {
    String description,title;

    String lottieid;

    public ScreenItems() {
    }

    public ScreenItems(String description, String title, String lottieid) {
        this.description = description;
        this.title = title;
        this.lottieid = lottieid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLottieid() {
        return lottieid;
    }

    public void setLottieid(String lottieid) {
        this.lottieid = lottieid;
    }
}
