package com.example.testingproject.adapter;

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

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.MyViewHolder> {
    ArrayList<Recipe> list;
    OnNoteListener onNoteListener;
    private OnNoteListener mOnNoteListener;
    public AdminAdapter(ArrayList<Recipe> list,OnNoteListener onNoteListener)
    {
        this.list=list;
        this.mOnNoteListener=onNoteListener;
    }


    @NonNull
    @Override
    public AdminAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_row,parent,false);
        return new MyViewHolder(view,mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminAdapter.MyViewHolder holder, int position) {
        holder.foodname.setText(list.get(position).getDishTitle());
        Picasso.get().load(list.get(position).getRecipeImages().get(0)).resize(200,200).into(holder.foodimage);
        holder.userid.setText(list.get(position).getPublishedBy());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView foodname,userid;
        ImageView foodimage;
        OnNoteListener onNoteListener;
        public MyViewHolder(@NonNull View itemView,OnNoteListener onNoteListener) {
            super(itemView);
            foodimage=itemView.findViewById(R.id.admin_food_image);
            foodname=itemView.findViewById(R.id.food_title);
            userid=itemView.findViewById(R.id.userid);
            this.onNoteListener=onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition());
            //itemView.setOnClickListener(this);
        }
    }

    public interface  OnNoteListener{
        void onNoteClick(int position);
    }
}
