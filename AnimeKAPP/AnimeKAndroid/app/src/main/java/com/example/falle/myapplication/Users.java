package com.example.falle.myapplication;

public class Users {

    private String name;
    private String email;
    private String picture;
    private String pass;


    public Users() {
    }

    public Users(String name, String email, String picture, String pass) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
