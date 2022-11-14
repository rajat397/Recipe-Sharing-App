package com.example.testingproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testingproject.adapter.AdminAdapter;
import com.example.testingproject.models.Recipe;
import com.example.testingproject.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class RecipeDisplayActivity extends AppCompatActivity  {

    TextView userName,Title,Ingredients,Steps,likeCount;
    ImageView profileImage,recipeImage,like;
    ImageButton nextButton,prevButton;
    int position=0;
    Recipe model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_display);
        userName = findViewById(R.id.etUserName);
        Title = findViewById(R.id.Title_text);
        Ingredients=findViewById(R.id.Ingredients_text);
        Steps = findViewById(R.id.steps_text);
        profileImage = findViewById(R.id.profile_image);
        recipeImage=findViewById(R.id.accept_reject_page_image);
        nextButton=findViewById(R.id.nextButton);
        prevButton=findViewById(R.id.backButton);
        like=findViewById(R.id.like);
        likeCount=findViewById(R.id.likeCount);











        Intent intent=getIntent();
        model= (Recipe) intent.getSerializableExtra("Model");
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

        FirebaseDatabase.getInstance().getReference().child("Recipes").child(model.getRecipeId()).child("like_list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot d3:snapshot.getChildren())
                {
                    if(d3.getKey().toString().equals(FirebaseAuth.getInstance().getUid())){
                        like.setImageResource(R.drawable.heart);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        updateLike();
        Title.setText(model.getDishTitle());
        Ingredients.setText(model.getIngredients());
        Steps.setText(model.getDescription());
        ArrayList<String> imageList = model.getRecipeImages();
//        FoodTitle.setText(model.getDishTitle());
        Picasso.get().load(imageList.get(position)).placeholder(R.drawable.user_avatar).resize(150,150).into(recipeImage);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position==imageList.size()-1)
                    Toast.makeText(RecipeDisplayActivity.this, "Showing the last image", Toast.LENGTH_SHORT).show();
                else
                {
                    position++;
                    Picasso.get().load(imageList.get(position)).transform(new RoundedTransformation(50, 4)).placeholder(R.drawable.user_avatar).resize(150, 150).into(recipeImage);
                }
            }
        });
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position==0)
                    Toast.makeText(RecipeDisplayActivity.this, "Showing the first image", Toast.LENGTH_SHORT).show();
                else {
                    position--;
                    Picasso.get().load(imageList.get(position)).placeholder(R.drawable.user_avatar).resize(150, 150).into(recipeImage);
                }
            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                like.setImageResource(R.drawable.heart);
                Toast.makeText(RecipeDisplayActivity.this, "You Liked this recipe !!", Toast.LENGTH_SHORT).show();
                Log.d("check kar", "onClick: "+model.getRecipeId());

                FirebaseDatabase.getInstance().getReference().child("Recipes").child(model.getRecipeId()).child("like_list").child(FirebaseAuth.getInstance().getUid()).setValue(true);
               // Log.d("qwerty", "onClick: "+model.getPostLike());
//                FirebaseDatabase.getInstance().getReference().child("Recipes").child(model.getRecipeId()).child("postLike").setValue(
//                        FirebaseDatabase.getInstance().getReference().child("Recipes").child(model.getRecipeId()).child("postLike").addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                FirebaseDatabase.getInstance().getReference().child("Recipes").child(model.getRecipeId()).child("postLike").setValue((int)snapshot.getValue()+1);
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        })
//                );
                    updateLike();
                //model.setPostLike(model.getPostLike()+1);
//                HashMap<String,String> h1=new HashMap<>();
//                h1.put(Integer.toString(),FirebaseAuth.getInstance().getUid());
//                ds.setValue(h1);



            }
        });

    }

    public void updateLike()
    {
        FirebaseDatabase.getInstance().getReference().child("Recipes").child(model.getRecipeId()).child("like_list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int ans=0;
                for(DataSnapshot sp:snapshot.getChildren())
                    ans++;
                Log.d("Likes hai", "Like Count: "+ans);
                likeCount.setText(Integer.toString(ans));
                //FirebaseDatabase.getInstance().getReference().child("Recipes").child(model.getRecipeId()).child("postLike").setValue(ans);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}