package com.example.restaurantpos.Listeners;

import com.example.restaurantpos.Models.RecipeDetailsResponse;

public interface RecipeDetailsListener {
    void didFetch(RecipeDetailsResponse response,String message);
    void didError(String message);
}
