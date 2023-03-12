package com.example.restaurantpos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurantpos.Adapters.IngredientsAdapter;
import com.example.restaurantpos.Adapters.InstructionStepAdapter;
import com.example.restaurantpos.Adapters.InstructionsAdapter;
import com.example.restaurantpos.Adapters.SimilarRecipeAdapter;
import com.example.restaurantpos.Listeners.AnalyzedInstructionsListener;
import com.example.restaurantpos.Listeners.RecipeClickListener;
import com.example.restaurantpos.Listeners.RecipeDetailsListener;
import com.example.restaurantpos.Listeners.RecipeNutritionListener;
import com.example.restaurantpos.Listeners.SimilarRecipeListener;
import com.example.restaurantpos.Models.InstructionsResponse;
import com.example.restaurantpos.Models.Nutrition;
import com.example.restaurantpos.Models.Recipe;
import com.example.restaurantpos.Models.RecipeDetailsResponse;
import com.example.restaurantpos.Models.RecipeNutritionResponse;
import com.example.restaurantpos.Models.SimilarRecipeResponse;
import com.example.restaurantpos.Models.Step;
import com.example.restaurantpos.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class RecipePage extends AppCompatActivity {
    int id;
    TextView tvName, tvServings, tvReadyInMinutes,tvDietType, tvFats, tvCalories, tvCarbs, tvProtein;
    ImageView imgMain, imgDiet, imgFavoriteRecipePage;
    RequestManager manager;
    RecyclerView recyclerIngredients, recyclerSimilar,recyclerInstructions;
    ProgressDialog dialog;
    IngredientsAdapter adapter;
    SimilarRecipeAdapter similarRecipeAdapter;
    InstructionsAdapter instructionsAdapter;
    CardView cvLikeRecipePage;
    DatabaseReference mDatabase;
    boolean like=false;
    ArrayList<Integer>ids;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_page);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        id=Integer.parseInt(getIntent().getStringExtra("id"));
        findViews();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.child("Users").child(FirebaseAuth.getInstance().getUid()).getValue(User.class);
                ids=new ArrayList<>();
                if (user.getLiked()!=null){
                    ids= user.getLiked();
                }
                if(ids.contains(id)){
                    imgFavoriteRecipePage.setImageResource(R.drawable.favorite_pink);
                    like=true;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        cvLikeRecipePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!like){
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user=snapshot.child("Users").child(FirebaseAuth.getInstance().getUid()).getValue(User.class);
                            ids=new ArrayList<>();
                            if (user.getLiked()!=null){
                                ids= user.getLiked();
                            }
                            if(!ids.contains(id)){
                                ids.add(id);
                                mDatabase.child("Users").child(FirebaseAuth.getInstance().getUid()).child("liked").setValue(ids);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    imgFavoriteRecipePage.setImageResource(R.drawable.favorite_pink);
                    like=true;
                }else {
                    Query query=mDatabase.child("Users").child(FirebaseAuth.getInstance().getUid());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user=snapshot.getValue(User.class);
                            ArrayList<Integer>liked=user.getLiked();
                            int index=liked.indexOf(id);
                            mDatabase.child("Users").child(FirebaseAuth.getInstance().getUid()).child("liked").child(String.valueOf(index)).removeValue();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    imgFavoriteRecipePage.setImageResource(R.drawable.favorite);
                    like=false;
                }

            }
        });
        manager=new RequestManager(this);
        manager.getRecipeDetails(recipeDetailsListener, id);
        manager.getSimilarRecipes(similarRecipeListener,id);
        manager.getAnalyzedInstructions(analyzedInstructionsListener,id);
        manager.getRecipeNutrition(recipeNutritionListener, id);
        dialog=new ProgressDialog(this);
        dialog.setTitle("Loading Details...");
        dialog.show();

    }
    private void findViews() {
        imgFavoriteRecipePage=findViewById(R.id.imgFavoriteRecipePge);
        cvLikeRecipePage=findViewById(R.id.cvLikeRecipePage);
        tvDietType=findViewById(R.id.tvDietType);
        imgDiet=findViewById(R.id.imgDiet);
        recyclerIngredients=findViewById(R.id.recyclerIngredients);
        tvName=findViewById(R.id.tvName);
        tvServings=findViewById(R.id.tvServings);
        imgMain=findViewById(R.id.imgMain);
        tvReadyInMinutes=findViewById(R.id.tvReadyInMinutes);
        recyclerSimilar=findViewById(R.id.recyclerSimilar);
        recyclerInstructions=findViewById(R.id.recyclerInstructions);
        tvCarbs=findViewById(R.id.tvCarbs);
        tvProtein=findViewById(R.id.tvProtein);
        tvCalories=findViewById(R.id.tvCalories);
        tvFats=findViewById(R.id.tvFats);
    }
    private final RecipeNutritionListener recipeNutritionListener=new RecipeNutritionListener() {
        @Override
        public void didFetch(Nutrition response, String message) {
            tvCalories.setText(response.calories+" calories");
            tvCarbs.setText(response.carbs+" carbs");
            tvFats.setText(response.fat+ " fat");
            tvProtein.setText(response.protein+ " protein");

        }

        @Override
        public void didError(String message) {
            Toast.makeText(RecipePage.this, message, Toast.LENGTH_SHORT).show();
        }
    };
    private final RecipeDetailsListener recipeDetailsListener=new RecipeDetailsListener() {
        @Override
        public void didFetch(RecipeDetailsResponse response, String message) {
            int serv=response.servings;
            String servings;
            if(serv>1){
                servings=" servings";
            }else {
                servings=" serving";
            }
            if(response.vegan){
                imgDiet.setImageResource(R.drawable.vegan);
                tvDietType.setText("Vegan");
            }else if(response.vegetarian){
                imgDiet.setImageResource(R.drawable.no_meat);
                tvDietType.setText("Vegetarian");
            }else {
                imgDiet.setImageResource(R.drawable.fish);
                tvDietType.setText("Meat/Fish");
            }

            dialog.dismiss();
            tvName.setText(response.title);
            tvServings.setText(Integer.toString(serv)+servings);
            tvReadyInMinutes.setText(Integer.toString(response.readyInMinutes)+" min.");

            Picasso.get().load("https://spoonacular.com/recipeImages/"+response.id+"-636x393."+response.imageType).into(imgMain);
            recyclerIngredients.setLayoutManager(new LinearLayoutManager(RecipePage.this,LinearLayoutManager.VERTICAL,false));
            adapter=new IngredientsAdapter(RecipePage.this,response.extendedIngredients);
            recyclerIngredients.setAdapter(adapter);

        }

        @Override
        public void didError(String message) {
            Toast.makeText(RecipePage.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    };
    private final SimilarRecipeListener similarRecipeListener=new SimilarRecipeListener() {
        @Override
        public void didFetch(List<SimilarRecipeResponse> responses, String message) {
            recyclerSimilar.setHasFixedSize(true);
            recyclerSimilar.setLayoutManager(new LinearLayoutManager(RecipePage.this,LinearLayoutManager.HORIZONTAL, false));
            similarRecipeAdapter=new SimilarRecipeAdapter(RecipePage.this,responses,recipeClickListener);
            recyclerSimilar.setAdapter(similarRecipeAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(RecipePage.this, message, Toast.LENGTH_SHORT).show();
        }
    };
    private final RecipeClickListener recipeClickListener=new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {
            startActivity(new Intent(RecipePage.this,RecipePage.class).putExtra("id",id));
        }
    };
    private final AnalyzedInstructionsListener analyzedInstructionsListener=new AnalyzedInstructionsListener() {
        @Override
        public void didFetch(List<InstructionsResponse> responses, String message) {
            recyclerInstructions.setHasFixedSize(true);
            recyclerInstructions.setLayoutManager(new LinearLayoutManager(RecipePage.this,LinearLayoutManager.VERTICAL,false));
            instructionsAdapter=new InstructionsAdapter(RecipePage.this,responses);
            recyclerInstructions.setAdapter(instructionsAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(RecipePage.this, message, Toast.LENGTH_SHORT).show();
        }
    };
}