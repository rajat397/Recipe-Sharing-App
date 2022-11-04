package com.example.testingproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.testingproject.adapter.AdminAdapter;
import com.example.testingproject.models.Recipe;

public class RecipeDisplayActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_display);

        Intent intent=getIntent();
        Recipe recipe=intent.getParcelableExtra("Package");

    }


}