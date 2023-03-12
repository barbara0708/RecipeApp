package com.example.restaurantpos.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantpos.Models.ExtendedIngredient;
import com.example.restaurantpos.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsViewHolder> {
    Context context;
    List<ExtendedIngredient>ingredients;

    public IngredientsAdapter(Context context, List<ExtendedIngredient> ingredients) {
        this.context = context;
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IngredientsViewHolder(LayoutInflater.from(context).inflate(R.layout.list_meal_ingredients,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder holder, int position) {
        holder.tvProductName.setText(ingredients.get(position).name);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }
}
class IngredientsViewHolder extends RecyclerView.ViewHolder{
    TextView tvProductName;
    public IngredientsViewHolder(@NonNull View itemView) {
        super(itemView);
        tvProductName=itemView.findViewById(R.id.tvProductName);
    }
}
