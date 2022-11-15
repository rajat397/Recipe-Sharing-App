package com.example.testingproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testingproject.models.Recipe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class Accept_Reject_Page extends AppCompatActivity {

    TextView title,ingredients,steps,author;
    ImageButton backButton,nextButton;
    Button accept,reject;
    ImageView imageView;
    DatabaseReference db,modify;
    DataSnapshot dataSnapshot=null;
    int position=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_reject_page);

        Intent intent=getIntent();
        Recipe recipe=intent.getParcelableExtra("Package");

        title=findViewById(R.id.Title_text1);
        ingredients=findViewById(R.id.Ingredients_text1);
        steps=findViewById(R.id.steps_text1);
        author=findViewById(R.id.author);
        backButton=findViewById(R.id.backButton1);
        nextButton=findViewById(R.id.nextButton1);
        accept=findViewById(R.id.accept_button);
        reject=findViewById(R.id.reject_button);
        imageView=findViewById(R.id.recipe_images);
        db= FirebaseDatabase.getInstance().getReference().child("Users").child(recipe.getPublishedBy()).child("userName");
        modify=FirebaseDatabase.getInstance().getReference().child("Recipes");

        title.setText(recipe.getDishTitle());
        ingredients.setText(recipe.getIngredients());
        steps.setText(recipe.getDescription());
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                author.setText(" - " +snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Picasso.get().load(recipe.getRecipeImages().get(position)).resize(253,231).into(imageView);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position==0)
                    Toast.makeText(Accept_Reject_Page.this, "Displaying First Image", Toast.LENGTH_SHORT).show();
                else
                {
                    position--;
                    Picasso.get().load(recipe.getRecipeImages().get(position)).resize(253,231).into(imageView);
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position==recipe.getRecipeImages().size()-1)
                    Toast.makeText(Accept_Reject_Page.this, "Displaying last image", Toast.LENGTH_SHORT).show();
                else
                {
                    position++;
                    Picasso.get().load(recipe.getRecipeImages().get(position)).resize(253,231).into(imageView);
                }
            }
        });


        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> h1 = new HashMap<>();
                h1.put("validate", true);
                FirebaseDatabase.getInstance().getReference().child("Recipes").child(recipe.getRecipeId()).updateChildren(h1);
                Toast.makeText(Accept_Reject_Page.this, "Recipe Accepted !!", Toast.LENGTH_SHORT).show();
                finish();
//
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //FirebaseDatabase.getInstance().getReference().child("Recipes").child(recipe.getRecipeId()).child("recipeId").getRef().removeValue();
                Log.d("Testing123", "onClick: "+FirebaseDatabase.getInstance().getReference().child("Recipes").child(recipe.getRecipeId()).child("postLike").toString());
                Log.d("Testing123", "onClick: "+FirebaseDatabase.getInstance().getReference().child("Recipes").child(recipe.getRecipeId()).child("like_list").toString());
              //  FirebaseDatabase.getInstance().getReference().child("Recipes").child(recipe.getRecipeId()).child("postLike").removeValue();
             //   FirebaseDatabase.getInstance().getReference().child("Recipes").child(recipe.getRecipeId()).child("like_list").removeValue();
                FirebaseDatabase.getInstance().getReference().child("Recipes").child(recipe.getRecipeId()).removeValue();


//                FirebaseDatabase.getInstance().getReference().child("Recipes").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if(snapshot.hasChild(recipe.getRecipeId()))
//                        {
//                            FirebaseDatabase.getInstance().getReference().child("Recipes").child(recipe.getRecipeId()).child("postLike").getRef().removeValue();
//                            FirebaseDatabase.getInstance().getReference().child("Recipes").child(recipe.getRecipeId()).child("recipeId").getKey().removeValue();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
                Toast.makeText(Accept_Reject_Page.this, "Recipe Rejected !!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }
}