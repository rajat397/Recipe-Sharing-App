package com.example.testingproject.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeAdapter extends FirebaseRecyclerAdapter<Recipe,HomeAdapter.viewHolder> {

    ArrayList<Recipe> list;
    Context context;

    public HomeAdapter(Context context,@NonNull FirebaseRecyclerOptions<Recipe> options) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull viewHolder holder, int position, @NonNull Recipe model) {

        String key = getRef(position).getKey();
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


           ArrayList<String>imageList = model.getRecipeImages();
           holder.FoodTitle.setText(model.getDishTitle());
           Picasso.get().load(imageList.get(0)).placeholder(R.drawable.user_avatar).resize(150,150).into(holder.foodImage);

    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_recipe_item,parent,false);
       return  new viewHolder(view);
    }

            

    public class viewHolder extends RecyclerView.ViewHolder{

        ImageView profile , foodImage;
        TextView userName, FoodTitle;
        View view;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            profile= itemView.findViewById(R.id.profile_image);
            foodImage = itemView.findViewById(R.id.admin_food_image);
            userName = itemView.findViewById(R.id.etUserName);
            FoodTitle = itemView.findViewById(R.id.food_title);
            view = itemView;

        }
    }
}
