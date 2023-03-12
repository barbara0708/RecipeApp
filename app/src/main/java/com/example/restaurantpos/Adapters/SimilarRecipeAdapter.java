package com.example.restaurantpos.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantpos.Listeners.RecipeClickListener;
import com.example.restaurantpos.Models.SimilarRecipeResponse;
import com.example.restaurantpos.R;
import com.google.android.material.slider.LabelFormatter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SimilarRecipeAdapter extends RecyclerView.Adapter<SimilarRecipesViewHolder> {

    Context context;
    List<SimilarRecipeResponse>list;
    RecipeClickListener listener;

    public SimilarRecipeAdapter(Context context, List<SimilarRecipeResponse> list, RecipeClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SimilarRecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SimilarRecipesViewHolder(LayoutInflater.from(context).inflate(R.layout.list_similar_recipe,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SimilarRecipesViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvDishName.setText(list.get(position).title);
        holder.tvDishName.setSelected(true);
        holder.tvTimeCooking.setText(String.valueOf(list.get(position).readyInMinutes)+" minutes");
        Picasso.get().load("https://spoonacular.com/recipeImages/"+list.get(position).id+"-556x370."+list.get(position).imageType).into(holder.imgDish);
        holder.random_list_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRecipeClicked(String.valueOf(list.get(position).id));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
class SimilarRecipesViewHolder extends RecyclerView.ViewHolder{
    TextView tvDishName, tvTimeCooking;
    ImageView imgDish;
    CardView random_list_container;
    public SimilarRecipesViewHolder(@NonNull View itemView) {
        super(itemView);
        tvDishName=itemView.findViewById(R.id.tvDishName2);
        imgDish=itemView.findViewById(R.id.imgDish2);
        random_list_container=itemView.findViewById(R.id.random_list_container2);
        tvTimeCooking=itemView.findViewById(R.id.tvTimeCooking2);

    }
}
