package com.example.testingproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


         auth =FirebaseAuth.getInstance();

         add = findViewById(R.id.UploadRecipe);

         add.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 startActivity(new Intent(MainActivity.this, UploadRecipeActivity.class));
             }
         });

         Button search=findViewById(R.id.search_button1);
         search.setOnClickListener(new View.OnClickListener(){

             @Override
             public void onClick(View view) {
                 startActivity(new Intent(MainActivity.this,search_activity.class));
             }
         });

         Button admin=findViewById(R.id.admin_side);
         admin.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 startActivity(new Intent(MainActivity.this,adminPage.class));
             }
         });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.logout:

                AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(MainActivity.this,user_login.class));
                        finish();
                    }
                });
                break;

            case R.id.MyProfile:

                startActivity(new Intent(MainActivity.this,MyProfileActivity.class));


                break;
        }
        return true;
    }
}