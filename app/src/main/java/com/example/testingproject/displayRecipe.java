package com.example.testingproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testingproject.models.Recipe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class displayRecipe extends AppCompatActivity {
    ImageView img;
    TextView title,ingredients,steps,author;
    ImageButton backButton,nextButton;
    ImageView like;
    DatabaseReference db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipe);

        Intent intent=new Intent();
        Recipe recipe=intent.getParcelableExtra("pack");
        img=findViewById(R.id.recipe_images);
        nextButton=findViewById(R.id.nextButton1);
        backButton=findViewById(R.id.backButton1);
        title=findViewById(R.id.Title_text1);
        ingredients=findViewById(R.id.Ingredients_text1);
        steps=findViewById(R.id.steps_text1);
        author=findViewById(R.id.postedBy);
        like=findViewById(R.id.like);

        title.setText(recipe.getDishTitle());
        ingredients.setText(recipe.getIngredients());
        steps.setText(recipe.getDescription());

        db= FirebaseDatabase.getInstance().getReference().child("Users").child(recipe.getPublishedBy()).child("userName");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                author.setText("Posted by - " +snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}