package com.example.testingproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testingproject.R;
import com.example.testingproject.RecipeDisplayActivity;
import com.example.testingproject.models.Recipe;
import com.example.testingproject.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    ArrayList<Recipe> list;
    Context context;
    public MyAdapter(ArrayList<Recipe> list,Context context)
    {
        this.context= context;
        this.list=list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.my_recipe_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Recipe model = list.get(position);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RecipeDisplayActivity.class);
                intent.putExtra("Model", (Parcelable) model);
                context.startActivity(intent);

            }
        });


        FirebaseDatabase.getInstance().getReference().child("Users").child(model.getPublishedBy()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                holder.userName.setText(user.getUserName());
                Picasso.get().load(user.getProfilepic()).placeholder(R.drawable.user_avatar).into(holder.profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
        View view;
        ImageView profile ;
        TextView userName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            profile= itemView.findViewById(R.id.profile_image);
            userName = itemView.findViewById(R.id.etUserName);
            foodname=itemView.findViewById(R.id.food_title);
            foodimage=itemView.findViewById(R.id.admin_food_image);
            view = itemView;
        }
    }


}
