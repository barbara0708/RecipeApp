package com.example.restaurantpos.Listeners;

import com.example.restaurantpos.Models.SimilarRecipeResponse;

import java.util.List;

public interface SimilarRecipeListener {
    void didFetch(List<SimilarRecipeResponse>responses, String message);
    void didError(String message);
}
