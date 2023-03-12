package com.example.restaurantpos.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantpos.Models.Step;
import com.example.restaurantpos.R;

import java.util.List;

public class InstructionStepAdapter extends RecyclerView.Adapter<InstructionStepViewHolder> {
    Context context;
    List<Step> list;
    public String equipment="";

    public InstructionStepAdapter(Context context, List<Step> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public InstructionStepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InstructionStepViewHolder(LayoutInflater.from(context).inflate(R.layout.list_step,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionStepViewHolder holder, int position) {
        holder.tvNumber.setText(String.valueOf(list.get(position).number));
        holder.tvStepDescription.setText(list.get(position).step);
        equipment=equipment+list.get(position).equipment+", ";
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
class InstructionStepViewHolder extends RecyclerView.ViewHolder{
    TextView tvNumber, tvStepDescription;
    public InstructionStepViewHolder(@NonNull View itemView) {
        super(itemView);
        tvNumber=itemView.findViewById(R.id.tvNumber);
        tvStepDescription=itemView.findViewById(R.id.tvStepDescription);

    }
}

