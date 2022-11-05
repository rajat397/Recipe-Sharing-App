package com.example.testingproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testingproject.adapter.AdminAdapter;
import com.example.testingproject.models.Recipe;
import com.example.testingproject.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeDisplayActivity extends AppCompatActivity  {

    TextView userName,Title,Ingredients,Steps;
    ImageView profileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_display);
        userName = findViewById(R.id.etUserName);
        Title = findViewById(R.id.Title_text);
        Ingredients=findViewById(R.id.Ingredients_text);
        Steps = findViewById(R.id.steps_text);
        profileImage = findViewById(R.id.profile_image);
        Intent intent=getIntent();
        Recipe model= (Recipe) intent.getSerializableExtra("Model");
        FirebaseDatabase.getInstance().getReference().child("Users").child(model.getPublishedBy()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                userName.setText(user.getUserName());
                Picasso.get().load(user.getProfilepic()).placeholder(R.drawable.user_avatar).into(profileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Title.setText(model.getDishTitle());
        Ingredients.setText(model.getIngredients());
        Steps.setText(model.getDescription());
        ArrayList<String> imageList = model.getRecipeImages();
//        FoodTitle.setText(model.getDishTitle());
//        Picasso.get().load(imageList.get(0)).placeholder(R.drawable.user_avatar).resize(150,150).into(holder.foodImage);


    }


}