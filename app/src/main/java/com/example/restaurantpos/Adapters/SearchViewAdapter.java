package com.example.restaurantpos.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantpos.Listeners.RecipeClickListener;
import com.example.restaurantpos.Models.Recipe;
import com.example.restaurantpos.Models.User;
import com.example.restaurantpos.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchViewAdapter extends RecyclerView.Adapter<SearchViewHolder> {
    Context context;
    List<Recipe> list;
    RecipeClickListener listener;
    private DatabaseReference mDatabase;
    ArrayList<Integer> ids;
    private boolean liked;
    public SearchViewAdapter(Context context, List<Recipe>list, RecipeClickListener listener) {
        this.context = context;
        this.list=list;
        this.listener=listener;
        ids=new ArrayList<>();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        liked=false;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchViewHolder(LayoutInflater.from(context).inflate(R.layout.item_liked_page,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvDishName.setText(list.get(position).title);
        holder.tvTimeCooking.setText((list.get(position).readyInMinutes+" minutes"));
        holder.tvDishName.setSelected(true);
        Picasso.get().load(list.get(position).image).into(holder.imgDish);
        holder.liked_list_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRecipeClicked(String.valueOf(list.get(holder.getAdapterPosition()).id));
            }
        });

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.child("Users").child(FirebaseAuth.getInstance().getUid()).getValue(User.class);
                ids=new ArrayList<>();
                if (user.getLiked()!=null){
                    ids= user.getLiked();
                }
                if(ids.contains(list.get(position).id)){
                    holder.favorite.setBackgroundResource(R.drawable.favorite_pink);
                    liked=true;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                            int index=liked.indexOf(list.get(position).id);
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
                            ids.add(list.get(position).id);
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
                    shareMessage=shareMessage+list.get(position).sourceUrl;
                    shareIntent.putExtra(Intent.EXTRA_TEXT,shareMessage);
                    context.startActivity(Intent.createChooser(shareIntent,"choose one"));
                }catch (Exception e){
                    e.toString();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
class SearchViewHolder extends RecyclerView.ViewHolder{
    TextView tvDishName;
    ImageView imgDish,favorite;
    TextView tvTimeCooking;
    CardView liked_list_container, cvLikedShare,cvLikeLiked;

    public SearchViewHolder(@NonNull View itemView) {
        super(itemView);
        tvDishName=itemView.findViewById(R.id.tvLikedDishName);
        imgDish=itemView.findViewById(R.id.imgLikedDish);
        tvTimeCooking=itemView.findViewById(R.id.tvLikedTimeCooking);
        liked_list_container=itemView.findViewById(R.id.liked_list_container);
        cvLikedShare=itemView.findViewById(R.id.cvLikedShare);
        cvLikeLiked=itemView.findViewById(R.id.cvLikeLiked);
        favorite=itemView.findViewById(R.id.favorite);
    }
}
