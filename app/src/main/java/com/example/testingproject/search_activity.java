package com.example.testingproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.testingproject.adapter.MyAdapter;
import com.example.testingproject.models.Recipe;
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class search_activity extends AppCompatActivity {

    private EditText mSearchField;
    private ImageButton mSearchBtn;
    private RecyclerView mResultList;
    private DatabaseReference mUserDatabase;
    private MyAdapter myAdapter;
    ArrayList<Recipe> list;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        mResultList=findViewById(R.id.mResultList);
        mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Recipes");
        searchView=findViewById(R.id.searchView);

        mResultList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));



    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mUserDatabase!=null)
        {
            mUserDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists())
                    {
                        list=new ArrayList<>();
                        for(DataSnapshot ds:snapshot.getChildren())
                        {
                            list.add(ds.getValue(Recipe.class));
                        }
                        MyAdapter adapterClass=new MyAdapter(list);
                        mResultList.setAdapter(adapterClass);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(search_activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        if(searchView!=null)
        {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return true;
                }
            });
        }

    }
    private void search(String str)
    {
        ArrayList<Recipe> myList=new ArrayList<>();
        for(Recipe object:list)
        {
            if(object.getDishTitle().toLowerCase().contains(str.toLowerCase()))
            {
                myList.add(object);
            }
        }
        MyAdapter adapterClass=new MyAdapter(myList);
        mResultList.setAdapter(adapterClass);
    }
}