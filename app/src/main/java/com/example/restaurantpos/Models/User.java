package com.example.restaurantpos.Models;

import java.util.ArrayList;

public class User {
    private String username;
    private String email;
    private ArrayList<Integer> liked;

    public User() {

    }

    public User(String username, String email, ArrayList arr) {
        this.username = username;
        this.email = email;
        this.liked=arr;
    }

    public ArrayList<Integer> getLiked() {
        return liked;
    }

    public void setLiked(ArrayList<Integer> liked) {
        this.liked = liked;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

