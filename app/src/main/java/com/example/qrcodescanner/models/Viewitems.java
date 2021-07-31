package com.example.qrcodescanner.models;

public class Viewitems {
    String title;

    String lottieid;

    public Viewitems() {
    }

    public Viewitems( String title, String lottieid) {

        this.title = title;
        this.lottieid = lottieid;
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
