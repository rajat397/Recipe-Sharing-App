package com.example.testingproject;
import com.example.testingproject.adapter.AdminAdapter;
import com.example.testingproject.adapter.MyAdapter;
import com.example.testingproject.models.Recipe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
        getSupportActionBar().setTitle("REVIEW RECIPES");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.adminmenu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.logout:

                FirebaseAuth.getInstance()
                        .signOut();

                startActivity(new Intent(adminPage.this,user_login.class));
                finish();

                break;


        }
        return true;
    }
}