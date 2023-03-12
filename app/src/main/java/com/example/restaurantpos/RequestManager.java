package com.example.restaurantpos;

import android.content.Context;

import com.example.restaurantpos.Listeners.AnalyzedInstructionsListener;
import com.example.restaurantpos.Listeners.GeneratedMealPlanDayListener;
import com.example.restaurantpos.Listeners.RandomRecipeResponseListener;
import com.example.restaurantpos.Listeners.RecipeDetailsListener;
import com.example.restaurantpos.Listeners.RecipeNutritionListener;
import com.example.restaurantpos.Listeners.SimilarRecipeListener;
import com.example.restaurantpos.Models.GeneratedMealPlanResponse;
import com.example.restaurantpos.Models.InstructionsResponse;
import com.example.restaurantpos.Models.MealPlanWeekResponse;
import com.example.restaurantpos.Models.Nutrition;
import com.example.restaurantpos.Models.RandomRecipeApiResponse;
import com.example.restaurantpos.Models.RecipeDetailsResponse;
import com.example.restaurantpos.Models.SimilarRecipeResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class RequestManager {
    Context context;
    Retrofit retrofit=new Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public RequestManager(Context context) {
        this.context = context;
    }

    public void getRandomRecipes(RandomRecipeResponseListener listener, List<String>tags){
        CallRandomRecipes callRandomRecipes=retrofit.create(CallRandomRecipes.class);
        Call<RandomRecipeApiResponse>call=callRandomRecipes.callRandomRecipe(context.getString(R.string.api_key),"10",tags);
        call.enqueue(new Callback<RandomRecipeApiResponse>() {
            @Override
            public void onResponse(Call<RandomRecipeApiResponse> call, Response<RandomRecipeApiResponse> response) {
                if(!response.isSuccessful()){
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(),response.message());
            }

            @Override
            public void onFailure(Call<RandomRecipeApiResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }
    public void getRecipeNutrition(RecipeNutritionListener listener,int id){
        CallRecipeNutrition callRecipeNutrition=retrofit.create(CallRecipeNutrition.class);
        Call<Nutrition>call=callRecipeNutrition.callRecipeNutrition(id,context.getString(R.string.api_key));
        call.enqueue(new Callback<Nutrition>() {
            @Override
            public void onResponse(Call<Nutrition> call, Response<Nutrition> response) {
                if (!response.isSuccessful()) {
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }
            @Override
            public void onFailure(Call<Nutrition> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }
    public void getRandomRecipe(RandomRecipeResponseListener listener){
        CallRandomRecipes callRandomRecipes=retrofit.create(CallRandomRecipes.class);
        List<String>tags=new ArrayList<>();
        tags.add("vegan");
        Call<RandomRecipeApiResponse>call=callRandomRecipes.callRandomRecipe(context.getString(R.string.api_key),"1",tags);
        call.enqueue(new Callback<RandomRecipeApiResponse>() {
            @Override
            public void onResponse(Call<RandomRecipeApiResponse> call, Response<RandomRecipeApiResponse> response) {
                if(!response.isSuccessful()){
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(),response.message());
            }

            @Override
            public void onFailure(Call<RandomRecipeApiResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }
    public void getGeneratedMealPlanDay(GeneratedMealPlanDayListener listener,String targetCalories, String diet){
        CallGeneratedMealPlan callGeneratedMealPlan=retrofit.create(CallGeneratedMealPlan.class);
        Call<GeneratedMealPlanResponse> call=callGeneratedMealPlan.callGeneratedMealPlan(context.getString(R.string.api_key),"day",targetCalories,diet);
        call.enqueue(new Callback<GeneratedMealPlanResponse>() {
            @Override
            public void onResponse(Call<GeneratedMealPlanResponse> call, Response<GeneratedMealPlanResponse> response) {
                if(!response.isSuccessful()){
                    listener.didError(response.message());
                }
                listener.didFetch(response.body(),response.message());
            }

            @Override
            public void onFailure(Call<GeneratedMealPlanResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }
    public void getRecipeDetails(RecipeDetailsListener listener, int id){
        CallRecipeDetails callRecipeDetails=retrofit.create(CallRecipeDetails.class);
        Call<RecipeDetailsResponse> call=callRecipeDetails.callRecipeDetails(id, context.getString(R.string.api_key));
        call.enqueue(new Callback<RecipeDetailsResponse>() {
            @Override
            public void onResponse(Call<RecipeDetailsResponse> call, Response<RecipeDetailsResponse> response) {
                if(!response.isSuccessful()){
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(),response.message());
            }

            @Override
            public void onFailure(Call<RecipeDetailsResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }
    public void getSimilarRecipes(SimilarRecipeListener listener, int id){
        CallSimilarRecipes callSimilarRecipes=retrofit.create(CallSimilarRecipes.class);
        Call<List<SimilarRecipeResponse>> call=callSimilarRecipes.callSimilarRecipe(id,"5",context.getString(R.string.api_key));
        call.enqueue(new Callback<List<SimilarRecipeResponse>>() {
            @Override
            public void onResponse(Call<List<SimilarRecipeResponse>> call, Response<List<SimilarRecipeResponse>> response) {
                if(!response.isSuccessful()){
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<List<SimilarRecipeResponse>> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }
    public void getAnalyzedInstructions(AnalyzedInstructionsListener listener, int id){
        CallAnalyzedInstructions callAnalyzedInstructions=retrofit.create(CallAnalyzedInstructions.class);
        Call<List<InstructionsResponse>>call=callAnalyzedInstructions.callAnalyzedInstructions(id,context.getString(R.string.api_key));
        call.enqueue(new Callback<List<InstructionsResponse>>() {
            @Override
            public void onResponse(Call<List<InstructionsResponse>> call, Response<List<InstructionsResponse>> response) {
                if(!response.isSuccessful()){
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<List<InstructionsResponse>> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }
    private interface CallRandomRecipes{
        @GET("recipes/random")
        Call<RandomRecipeApiResponse> callRandomRecipe(
                @Query("apiKey") String apiKey,
                @Query("number") String number,
                @Query("tags")List<String>tags
                );
    }
    private interface CallRecipeDetails{
        @GET("recipes/{id}/information")
        Call<RecipeDetailsResponse>callRecipeDetails(
                @Path("id") int id,
                @Query("apiKey")String apiKey
        );
    }
    private interface CallSimilarRecipes{
        @GET("recipes/{id}/similar")
        Call<List<SimilarRecipeResponse>>callSimilarRecipe(
                @Path("id")int id,
                @Query("number") String number,
                @Query("apiKey")String apiKey
        );
    }
    private interface CallAnalyzedInstructions{
        @GET("recipes/{id}/analyzedInstructions")
        Call<List<InstructionsResponse>>callAnalyzedInstructions(
                @Path("id")int id,
                @Query("apiKey")String apiKey
        );
    }
    private interface CallRecipeNutrition{
        @GET("recipes/{id}/nutritionWidget.json")
        Call<Nutrition>callRecipeNutrition(
                @Path("id") int id,
                @Query("apiKey")String apiKey
        );
    }
    private interface CallGeneratedMealPlan{
        @GET("mealplanner/generate")
        Call<GeneratedMealPlanResponse>callGeneratedMealPlan(
                @Query("apiKey") String apiKey,
                @Query("timeFrame") String timeFrame,
                @Query("targetCalories") String targetCalories,
                @Query("diet")String diet
        );
    }
    private interface CallGeneratedMealPlanWeek{
        @GET("mealplanner/generate")
        Call<MealPlanWeekResponse>callGeneratedMealPlanWeek(
                @Query("apiKey") String apiKey,
                @Query("timeFrame") String timeFrame,
                @Query("targetCalories") String targetCalories,
                @Query("diet")String diet
        );
    }

}
