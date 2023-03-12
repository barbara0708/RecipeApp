package com.example.restaurantpos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.restaurantpos.Adapters.LikedRecipeAdapter;
import com.example.restaurantpos.Adapters.RandomRecipeAdapter;
import com.example.restaurantpos.Listeners.RecipeClickListener;
import com.example.restaurantpos.Listeners.RecipeDetailsListener;
import com.example.restaurantpos.Models.Recipe;
import com.example.restaurantpos.Models.RecipeDetailsResponse;
import com.example.restaurantpos.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LikedRecipes extends AppCompatActivity {
    RecyclerView rvFavorite;
    public List<Integer> ids=new ArrayList<Integer>();
    LikedRecipeAdapter adapter;
    RequestManager manager;
    RecipeDetailsListener recipeDetailsListener;
    private DatabaseReference mDatabase;
    List<Integer> list=new ArrayList<>();
    List<Recipe>recipes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_recipes);
        rvFavorite=findViewById(R.id.rvFavorite);
        recipes=new ArrayList<>();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        rvFavorite.setAdapter(adapter);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.child("Users").child(FirebaseAuth.getInstance().getUid()).getValue(User.class);
                list=user.getLiked();
                for(int i=0;i<list.size();i++){
                    if(list.get(i)!=null){
                        ids.add(list.get(i));
                    }
                }

                adapter=new LikedRecipeAdapter(LikedRecipes.this,ids,recipeClickListener);
                        rvFavorite.setLayoutManager(new LinearLayoutManager(LikedRecipes.this,LinearLayoutManager.VERTICAL,false));
                        rvFavorite.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private final RecipeClickListener recipeClickListener=new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {
            startActivity(new Intent(getApplicationContext(),RecipePage.class).putExtra("id",id));
        }
    };


}