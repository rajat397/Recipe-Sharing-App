package com.example.testingproject.Fragments;
import com.example.testingproject.adapter.MyAdapter;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.testingproject.R;
import com.example.testingproject.adapter.MyAdapter;
import com.example.testingproject.displayRecipe;
import com.example.testingproject.models.Recipe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private EditText mSearchField;
    private ImageButton mSearchBtn;
    private RecyclerView mResultList;
    private DatabaseReference mUserDatabase;
    private MyAdapter myAdapter;
    ArrayList<Recipe> list;
    private SearchView searchView;
    ArrayList<Recipe> temp=new ArrayList<>();
    public SearchFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_search, container, false);



        mResultList=view.findViewById(R.id.mResultList);
        mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Recipes");
        searchView=view.findViewById(R.id.searchView);

        mResultList.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        return view;
    }


    @Override
    public void onStart() {
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
                        temp.clear();temp.addAll(list);
                        myAdapter=new MyAdapter(temp,getActivity());
                        mResultList.setAdapter(myAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
        temp.clear();temp.addAll(myList);

        //mResultList.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
    }




}

