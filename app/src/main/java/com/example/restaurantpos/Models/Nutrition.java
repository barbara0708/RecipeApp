package com.example.restaurantpos.Models;

import java.util.ArrayList;

public class Nutrition {
    public String calories;
    public String carbs;
    public String fat;
    public String protein;
    public ArrayList<Bad> bad;
    public ArrayList<Good> good;
    public long expires;
    public boolean isStale;
}
