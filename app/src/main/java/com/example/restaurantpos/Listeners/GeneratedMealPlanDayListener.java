package com.example.restaurantpos.Listeners;

import com.example.restaurantpos.Models.GeneratedMealPlanResponse;

public interface GeneratedMealPlanDayListener {
    void didFetch(GeneratedMealPlanResponse response, String message);
    void didError(String message);
}
