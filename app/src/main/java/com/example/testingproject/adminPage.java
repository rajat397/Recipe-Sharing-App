package com.example.testingproject;
import com.example.testingproject.models.Recipe;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class adminPage extends AppCompatActivity {
    RecyclerView recview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        recview=(RecyclerView) findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));

//        FirebaseRecyclerOptions<Recipe> options =
//                new FirebaseRecyclerOptions.Builder<Recipe>()
//                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Recipes"), Recipe.class)
//                        .build();
    }
}