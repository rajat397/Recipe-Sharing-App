package com.example.testingproject;
import com.example.testingproject.adapter.AdminAdapter;
import com.example.testingproject.adapter.MyAdapter;
import com.example.testingproject.models.Recipe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class adminPage extends AppCompatActivity implements AdminAdapter.OnNoteListener {
    RecyclerView recview;
    private DatabaseReference db;
    ArrayList<Recipe> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        recview=(RecyclerView) findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));

        db=FirebaseDatabase.getInstance().getReference().child("Recipes");

        if(db!=null)
        {
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list=new ArrayList<>();
                    for(DataSnapshot ds:snapshot.getChildren())
                    {

                        Recipe obj=ds.getValue(Recipe.class);
                        obj.setRecipeId(ds.getKey());
                        if(!obj.isValidate())
                            list.add(obj);
                    }
                    //adapter here
                    AdminAdapter adminAdapter=new AdminAdapter(list,adminPage.this::onNoteClick);
                    recview.setAdapter(adminAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(adminPage.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    @Override
    public void onNoteClick(int position) {

        Intent intent=new Intent(adminPage.this,Accept_Reject_Page.class);
        intent.putExtra("Package",list.get(position));
        startActivity(intent);

    }
}