package com.example.qrcodescanner.models;

public class HistoryItems {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    String name;

    public HistoryItems(String name, String subject, String mail) {
        this.name = name;
        this.subject = subject;
        this.mail = mail;
    }

    public HistoryItems() {
    }

    String subject;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    String mail;
}
