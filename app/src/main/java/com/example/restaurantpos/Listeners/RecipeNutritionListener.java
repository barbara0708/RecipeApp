package com.example.restaurantpos.Listeners;

import com.example.restaurantpos.Models.Nutrition;
import com.example.restaurantpos.Models.RecipeNutritionResponse;

public interface RecipeNutritionListener {
    void didFetch(Nutrition response, String message);
    void didError(String message);
}
