package com.example.restaurantpos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurantpos.Listeners.GeneratedMealPlanDayListener;
import com.example.restaurantpos.Models.GeneratedMealPlanResponse;
import com.example.restaurantpos.Models.Meal;
import com.example.restaurantpos.Models.Nutrients;
import com.example.restaurantpos.Models.Nutrition;
import com.example.restaurantpos.Models.Recipe;

import java.util.ArrayList;

public class GenerateMealPlanActivity extends AppCompatActivity {
    AutoCompleteTextView autoDiet;
    Dialog dialog;
    EditText etCalories;
    Button btnGenerate;
    String[]dietTypes=new String[]{"Gluten Free","Ketogenic","Vegetarian","Lacto-Vegetarian","Ovo-Vegetarian","Vegan","Pescetarian","Paleo","Primal","Low FODMAP","Whole30"};
    RequestManager manager;
    String diet, calories;
    TextView tvFirstDish, tvSecondDish, tvThirdDish, tvTime1, tvTime2, tvTime3;
    ScrollView scroll;
    CardView cvFirst, cvSecond, cvThird;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_meal_plan);
        findViews();
        autoDiet.setAdapter(new ArrayAdapter<>(GenerateMealPlanActivity.this, android.R.layout.simple_list_item_1,dietTypes));
        manager=new RequestManager(this);
        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diet=autoDiet.getText().toString();
                calories=etCalories.getText().toString();
                manager.getGeneratedMealPlanDay(generatedMealPlanDayListener,calories,diet);
                showPlan();
            }
        });
        dialog=new ProgressDialog(this);
        dialog.setTitle("Loading...");

    }

    private void showPlan() {
        scroll.setVisibility(View.VISIBLE);
    }

    private final GeneratedMealPlanDayListener generatedMealPlanDayListener=new GeneratedMealPlanDayListener() {
        @Override
        public void didFetch(GeneratedMealPlanResponse response, String message) {
            ArrayList<Meal>meals=response.meals;
            tvFirstDish.setText(meals.get(0).title);
            tvSecondDish.setText(meals.get(1).title);
            tvThirdDish.setText(meals.get(2).title);
            tvTime1.setText(String.valueOf(meals.get(0).readyInMinutes)+" minutes");
            tvTime2.setText(String.valueOf(meals.get(1).readyInMinutes)+" minutes");
            tvTime3.setText(String.valueOf(meals.get(2).readyInMinutes)+" minutes");
            cvFirst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(GenerateMealPlanActivity.this, RecipePage.class).putExtra("id",String.valueOf(meals.get(0).id)));
                }
            });
            cvSecond.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(GenerateMealPlanActivity.this, RecipePage.class).putExtra("id",String.valueOf(meals.get(1).id)));
                }
            });
            cvThird.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(GenerateMealPlanActivity.this, RecipePage.class).putExtra("id",String.valueOf(meals.get(2).id)));
                }
            });
            dialog.dismiss();
        }

        @Override
        public void didError(String message) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    };
    private void findViews() {
        autoDiet=findViewById(R.id.autoDiet);
        etCalories=findViewById(R.id.etCalories);
        btnGenerate=findViewById(R.id.btnGenerate);
        tvFirstDish=findViewById(R.id.tvFirstDish);
        tvSecondDish=findViewById(R.id.tvSecondDish);
        tvThirdDish=findViewById(R.id.tvThirdDish);
        scroll=findViewById(R.id.scroll);
        tvTime1=findViewById(R.id.tvTime1);
        tvTime2=findViewById(R.id.tvTime2);
        tvTime3=findViewById(R.id.tvTime3);
        cvFirst=findViewById(R.id.cvFirst);
        cvSecond=findViewById(R.id.cvSecond);
        cvThird=findViewById(R.id.cvThird);
    }


}