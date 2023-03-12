package com.example.restaurantpos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurantpos.Adapters.LikedRecipeAdapter;
import com.example.restaurantpos.Adapters.RandomRecipeAdapter;
import com.example.restaurantpos.Listeners.RandomRecipeResponseListener;
import com.example.restaurantpos.Listeners.RecipeClickListener;
import com.example.restaurantpos.Login.Login;
import com.example.restaurantpos.Login.Registration;
import com.example.restaurantpos.Models.RandomRecipeApiResponse;
import com.example.restaurantpos.Models.Recipe;
import com.example.restaurantpos.Models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainMenuPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ProgressDialog dialog;
    RequestManager managerFirst, managerSecond;
    RandomRecipeAdapter adapter;
    RecyclerView recyclerView;
    EditText etSearch;
    List<String> tags=new ArrayList<>();
    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    ImageView imgBottomMain;
    String itemId;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView tvWeekRecipeName;
    CardView cvWeekRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_page);

        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        gsc=GoogleSignIn.getClient(this,gso);
        GoogleSignInAccount account=GoogleSignIn.getLastSignedInAccount(this);
        //for profile data of the user

        cvWeekRecipe=findViewById(R.id.cvWeekRecipe);
        tvWeekRecipeName=findViewById(R.id.tvWeekRecipeName);
        imgBottomMain=findViewById(R.id.imgBottomMain);
        recyclerView=findViewById(R.id.recyclerView);
        etSearch=findViewById(R.id.etSearch);
        navigationView=findViewById(R.id.navigationView1);
        drawer=findViewById(R.id.drawer);
        toolbar=findViewById(R.id.toolbar);

        mAuth=FirebaseAuth.getInstance();
        mDatabase=FirebaseDatabase.getInstance().getReference();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.child("Users").child(FirebaseAuth.getInstance().getUid()).getValue(User.class);
                getSupportActionBar().setTitle("Welcome to Foodies, "+user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Foodies");

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer, toolbar,R.string.open, R.string.close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        adapter=new RandomRecipeAdapter(this);
        final RandomRecipeResponseListener randomRecipeResponseListener=new RandomRecipeResponseListener() {
            @Override
            public void didFetch(RandomRecipeApiResponse response, String message) {
                dialog.dismiss();
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainMenuPage.this,LinearLayoutManager.HORIZONTAL,false));
                adapter=new RandomRecipeAdapter(MainMenuPage.this,response.recipes,recipeClickListener);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void didError(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        };
        dialog=new ProgressDialog(this);
        dialog.setTitle("Loading...");

        managerFirst=new RequestManager(this);
        managerFirst.getRandomRecipes(randomRecipeResponseListener,tags);

        managerSecond=new RequestManager(this);
        managerSecond.getRandomRecipe(mainRandomRecipeListener);

        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SearchActivity.class));
            }
        });
        View view=navigationView.getHeaderView(0);

        dialog.show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.notifyDataSetChanged();
    }

    private final RandomRecipeResponseListener mainRandomRecipeListener=new RandomRecipeResponseListener() {
        @Override
        public void didFetch(RandomRecipeApiResponse response, String message) {
            dialog.dismiss();
            Recipe recipe=response.recipes.get(0);
            Picasso.get().load("https://spoonacular.com/recipeImages/"+recipe.id+"-312x231."+recipe.imageType).into(imgBottomMain);
            tvWeekRecipeName.setText(recipe.title);
            cvWeekRecipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainMenuPage.this,RecipePage.class).putExtra("id",String.valueOf(recipe.id)));
                }
            });
        }

        @Override
        public void didError(String message) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    };


    private final RecipeClickListener recipeClickListener=new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {
            startActivity(new Intent(getApplicationContext(),RecipePage.class).putExtra("id",id));
            itemId=id;
        }
    };

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                break;
            case R.id.nav_liked:
                startActivity(new Intent(MainMenuPage.this,LikedRecipes.class));
                break;
            case R.id.nav_more:
                goToSite("https://spoonacular.com/about");
                break;
            case R.id.nav_settings:
                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                break;
            case R.id.nav_logout:
                logout();
                break;
            case R.id.nav_meal_plan:
                startActivity(new Intent(MainMenuPage.this,GenerateMealPlanActivity.class
                ));
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goToSite(String s) {
        Uri uri=Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }

    private void logout() {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                startActivity(new Intent(MainMenuPage.this, MainActivity.class));
            }
        });
    }


}