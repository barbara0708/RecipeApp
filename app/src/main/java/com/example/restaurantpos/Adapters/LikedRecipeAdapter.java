package com.example.restaurantpos.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantpos.LikedRecipes;
import com.example.restaurantpos.Listeners.RecipeClickListener;
import com.example.restaurantpos.Listeners.RecipeDetailsListener;
import com.example.restaurantpos.Models.Recipe;
import com.example.restaurantpos.Models.RecipeDetailsResponse;
import com.example.restaurantpos.Models.User;
import com.example.restaurantpos.R;
import com.example.restaurantpos.RequestManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class LikedRecipeAdapter extends RecyclerView.Adapter<LikedRecipeViewHolder> {
    Context context;
    RequestManager manager;
    List<RecipeDetailsResponse> list;
    RecipeClickListener listener;
    public List<Integer> ids=new ArrayList<Integer>();
    private DatabaseReference mDatabase;
    private boolean liked;
    public LikedRecipeAdapter(Context context, List<Integer>ids, RecipeClickListener listener) {
        this.context = context;
        mDatabase= FirebaseDatabase.getInstance().getReference();
        this.ids = ids;
        manager=new RequestManager(context);
        this.listener=listener;
        liked=true;
    }

    @NonNull
    @Override
    public LikedRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LikedRecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.item_liked_page,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LikedRecipeViewHolder holder, int position) {
        RecipeDetailsListener recipeDetailsListener=new RecipeDetailsListener() {
            @Override
            public void didFetch(RecipeDetailsResponse response, String message) {
                holder.favorite.setBackgroundResource(R.drawable.favorite_pink);
                holder.tvDishName.setText(response.title);
                holder.tvTimeCooking.setText(String.valueOf(response.readyInMinutes)+" minutes");
                holder.tvDishName.setSelected(true);
                Picasso.get().load(response.image).into(holder.imgDish);

                holder.liked_list_container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onRecipeClicked(String.valueOf(response.id));
                    }
                });
                holder.cvLikeLiked.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(liked){
                            Query query=mDatabase.child("Users").child(FirebaseAuth.getInstance().getUid());
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    User user=snapshot.getValue(User.class);
                                    ArrayList<Integer>liked=user.getLiked();
                                    int index=liked.indexOf(response.id);
                                    mDatabase.child("Users").child(FirebaseAuth.getInstance().getUid()).child("liked").child(String.valueOf(index)).removeValue();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            holder.favorite.setBackgroundResource(R.drawable.favorite);
                            liked=false;
                        }else {
                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    User user = snapshot.child("Users").child(FirebaseAuth.getInstance().getUid()).getValue(User.class);
                                    if (user.getLiked() != null) {
                                        ids = user.getLiked();
                                    }
                                    ids.add(response.id);
                                    mDatabase.child("Users").child(FirebaseAuth.getInstance().getUid()).child("liked").setValue(ids);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            holder.favorite.setBackgroundResource(R.drawable.favorite_pink);
                            liked=true;

                        }
                    }
                });

                holder.cvLikedShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent shareIntent=new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT,R.string.app_name);
                            String shareMessage="\nLet me recommend you this recipe\n\n";
                            shareMessage=shareMessage+response.sourceUrl;
                            shareIntent.putExtra(Intent.EXTRA_TEXT,shareMessage);
                            context.startActivity(Intent.createChooser(shareIntent,"choose one"));
                        }catch (Exception e){
                            e.toString();
                        }

                    }
                });
            }

            @Override
            public void didError(String message) {

            }
        };
        manager=new RequestManager(context.getApplicationContext());
        manager.getRecipeDetails(recipeDetailsListener, ids.get(position));

    }

    @Override
    public int getItemCount() {
        return ids.size();
    }

}
class LikedRecipeViewHolder extends RecyclerView.ViewHolder{
    TextView tvDishName;
    ImageView imgDish,favorite;
    TextView tvTimeCooking;
    CardView liked_list_container, cvLikedShare, cvLikeLiked;
    public LikedRecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        favorite=itemView.findViewById(R.id.favorite);
        cvLikeLiked=itemView.findViewById(R.id.cvLikeLiked);
        tvDishName=itemView.findViewById(R.id.tvLikedDishName);
        imgDish=itemView.findViewById(R.id.imgLikedDish);
        tvTimeCooking=itemView.findViewById(R.id.tvLikedTimeCooking);
        liked_list_container=itemView.findViewById(R.id.liked_list_container);
        cvLikedShare=itemView.findViewById(R.id.cvLikedShare);
    }
}