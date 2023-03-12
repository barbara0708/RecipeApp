package com.example.restaurantpos.Listeners;

import com.example.restaurantpos.Models.InstructionsResponse;
import com.example.restaurantpos.Models.Step;

import java.util.List;

public interface AnalyzedInstructionsListener {
    void didFetch(List<InstructionsResponse> response, String message);
    void didError(String message);
}
