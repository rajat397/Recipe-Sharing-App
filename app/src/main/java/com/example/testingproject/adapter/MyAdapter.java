package com.example.testingproject.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testingproject.R;
import com.example.testingproject.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    ArrayList<Recipe> list;
    public MyAdapter(ArrayList<Recipe> list)
    {
        this.list=list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.foodname.setText(list.get(position).getDishTitle());
        Picasso.get().load(list.get(position).getRecipeImages().get(0)).resize(300,300).into(holder.foodimage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView foodname;
        ImageView foodimage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            foodname=itemView.findViewById(R.id.food_name);
            foodimage=itemView.findViewById(R.id.food_image);

        }
    }


}
