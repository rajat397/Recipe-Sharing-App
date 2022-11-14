package com.example.testingproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.testingproject.Fragments.HomeFragment;
import com.example.testingproject.Fragments.ProfileFragment;
import com.example.testingproject.Fragments.SearchFragment;
import com.example.testingproject.Fragments.UploadRecipeFragment;
import com.example.testingproject.adapter.AdminAdapter;
import com.example.testingproject.adapter.HomeAdapter;
import com.example.testingproject.models.Recipe;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {



    public class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context) {
            super(context);
        }

        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public WrapContentLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("TAG", "meet a IOOBE in RecyclerView");
            }
        }
    }

    RecyclerView recview;
    HomeAdapter adapter;
    private FirebaseAuth auth;
    ArrayList<Recipe> recipeList;
    ImageView home,search,add,profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FirebaseDatabase.getInstance().getReference().child("Recipes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    ds.child("recipeId").getRef().setValue(ds.getKey().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        home=(ImageView) findViewById(R.id.home);
        search=(ImageView) findViewById(R.id.search);
        add=(ImageView) findViewById(R.id.add);
        profile=(ImageView) findViewById(R.id.profile);


        search.setBackgroundResource(R.color.white);
        add.setBackgroundResource(R.color.white);
        profile.setBackgroundResource(R.color.white);
        home.setBackgroundResource(R.color.teal_200);
        replaceFragment(new HomeFragment());


        home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                search.setBackgroundResource(R.color.white);
                add.setBackgroundResource(R.color.white);
                profile.setBackgroundResource(R.color.white);
                home.setBackgroundResource(R.color.teal_200);
                replaceFragment(new HomeFragment());
            }
        });
        search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                home.setBackgroundResource(R.color.white);
                add.setBackgroundResource(R.color.white);
                profile.setBackgroundResource(R.color.white);
                search.setBackgroundResource(R.color.teal_200);
                replaceFragment(new SearchFragment());
            }
        });
        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                search.setBackgroundResource(R.color.white);
                home.setBackgroundResource(R.color.white);
                profile.setBackgroundResource(R.color.white);
                add.setBackgroundResource(R.color.teal_200);
                replaceFragment(new UploadRecipeFragment());
            }
        });
        profile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                search.setBackgroundResource(R.color.white);
                add.setBackgroundResource(R.color.white);
                home.setBackgroundResource(R.color.white);
                profile.setBackgroundResource(R.color.teal_200);
                replaceFragment(new ProfileFragment());
            }
        });

//        recview = findViewById(R.id.homeRecView);
//        recview.setLayoutManager(new WrapContentLinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
//
//         Query query = FirebaseDatabase.getInstance().getReference().child("Recipes");
//        FirebaseRecyclerOptions<Recipe> options =
//                new FirebaseRecyclerOptions.Builder<Recipe>()
//                        .setQuery(query, Recipe.class)
//                        .build();
//
//        adapter = new HomeAdapter(options,MainActivity.this::onNoteClick);
//        recview.setAdapter(adapter);








    }







    private void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
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

                FirebaseAuth.getInstance().signOut();

                startActivity(new Intent(MainActivity.this,user_login.class));

                finish();
                //System.exit(0);

                //Log.d("hotahai", "onOptionsItemSelected: sdsdfdsfsdf");
               // System.exit(0);
                break;


        }
        return true;
    }
}