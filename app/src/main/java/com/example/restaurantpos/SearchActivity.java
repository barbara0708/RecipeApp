package com.example.restaurantpos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.restaurantpos.Adapters.RandomRecipeAdapter;
import com.example.restaurantpos.Adapters.SearchViewAdapter;
import com.example.restaurantpos.Listeners.RandomRecipeResponseListener;
import com.example.restaurantpos.Listeners.RecipeClickListener;
import com.example.restaurantpos.Models.RandomRecipeApiResponse;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    Spinner spinner_tags;
    RecyclerView recyclerView2;
    RequestManager manager;
    SearchViewAdapter adapter;
    SearchView searchView_home;
    List<String>tags=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        recyclerView2=findViewById(R.id.recyclerView2);
        spinner_tags=findViewById(R.id.spinner_tags);
        searchView_home=findViewById(R.id.searchView_home);
        searchView_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                tags.clear();
                tags.add(query);
                manager.getRandomRecipes(randomRecipeResponseListener,tags);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        spinner_tags.setOnItemSelectedListener(spinnerSelectedListener);
        manager=new RequestManager(this);
        //manager.getRandomRecipes(randomRecipeResponseListener);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.spinner_text, getResources().getStringArray(R.array.tags));
        adapter.setDropDownViewResource(R.layout.spinner_inner_text);
        spinner_tags.setAdapter(adapter);

    }
    private final RandomRecipeResponseListener randomRecipeResponseListener=new RandomRecipeResponseListener() {
        @Override
        public void didFetch(RandomRecipeApiResponse response, String message) {
            recyclerView2.setHasFixedSize(true);
            //recyclerView.setLayoutManager(new GridLayoutManager(MainMenuPage.this,1));
            recyclerView2.setLayoutManager(new LinearLayoutManager(SearchActivity.this,LinearLayoutManager.VERTICAL,false));
            adapter=new SearchViewAdapter(SearchActivity.this,response.recipes,recipeClickListener);
            recyclerView2.setAdapter(adapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    };

    private final AdapterView.OnItemSelectedListener spinnerSelectedListener=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            tags.clear();
            tags.add(adapterView.getSelectedItem().toString());
            manager.getRandomRecipes(randomRecipeResponseListener,tags);

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };
    private final RecipeClickListener recipeClickListener=new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {
            startActivity(new Intent(getApplicationContext(),RecipePage.class).putExtra("id",id));
        }
    };
}