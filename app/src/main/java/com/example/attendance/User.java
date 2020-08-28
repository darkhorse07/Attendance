package com.example.attendance;

public class User {

    private String date;
    private String present;

    public User() {

    }

    public User(String date, String present) {
        this.date = date;
        this.present = present;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }
}
