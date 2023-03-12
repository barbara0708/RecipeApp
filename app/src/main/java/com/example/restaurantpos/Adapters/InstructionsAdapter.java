package com.example.restaurantpos.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantpos.Models.InstructionsResponse;
import com.example.restaurantpos.Models.Step;
import com.example.restaurantpos.R;

import java.util.List;

public class InstructionsAdapter extends RecyclerView.Adapter<InstructionsViewHolder>{
    Context context;
    List<InstructionsResponse>list;
    String equipment="";

    public String getEquipment() {
        return equipment;
    }

    public InstructionsAdapter(Context context, List<InstructionsResponse> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public InstructionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InstructionsViewHolder(LayoutInflater.from(context).inflate(R.layout.list_meal_step,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionsViewHolder holder, int position) {
        holder.tvStep.setText(list.get(position).name);
        holder.recyclerMealInstructions.setHasFixedSize(true);
        holder.recyclerMealInstructions.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        InstructionStepAdapter instructionStepAdapter=new InstructionStepAdapter(context,list.get(position).steps);
        holder.recyclerMealInstructions.setAdapter(instructionStepAdapter);
        equipment=instructionStepAdapter.equipment;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
class InstructionsViewHolder extends RecyclerView.ViewHolder{
    TextView tvStep;
    RecyclerView recyclerMealInstructions;
    public InstructionsViewHolder(@NonNull View itemView) {
        super(itemView);
        tvStep=itemView.findViewById(R.id.tvStep);
        recyclerMealInstructions=itemView.findViewById(R.id.recyclerMealInstructions);
    }
}
