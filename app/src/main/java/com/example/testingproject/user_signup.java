package com.example.testingproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.testingproject.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class user_signup extends AppCompatActivity  {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private EditText etEmailSignUp, etPasswordSignUp, etNameSignUp;
    private Button btnSignUp;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getSupportActionBar().hide();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);

// ...
// Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        etEmailSignUp = findViewById(R.id.etEmailSignUp);
        etPasswordSignUp = findViewById(R.id.etPasswordSignUp);
        etNameSignUp = findViewById(R.id.etNameSignUp);
        btnSignUp = findViewById(R.id.btnSignUp);

        progressDialog = new ProgressDialog(user_signup.this);
        progressDialog.setTitle("Creating Account ");
        progressDialog.setMessage("We are creating your account");









        btnSignUp.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                progressDialog.show();
                String email = etEmailSignUp.getText().toString();
                String password = etPasswordSignUp.getText().toString();
                String name = etNameSignUp.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    etEmailSignUp.setError("Email is Required");
                    return;
                }


                if (TextUtils.isEmpty(password)) {
                    etPasswordSignUp.setError("Password is Required");
                    return;
                }

                if (TextUtils.isEmpty(name)) {
                    etNameSignUp.setError("Name is Required");
                    return;
                }


                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){

                            User user = new User(etNameSignUp.getText().toString(),etEmailSignUp.getText().toString(),etPasswordSignUp.getText().toString());

                            String id = task.getResult().getUser().getUid();



                            database.getReference().child("Users").child(id).setValue(user);
                            Toast.makeText(user_signup.this, "Account Created Successfully",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(user_signup.this, MainActivity.class));
                        }
                        else
                        {
                            Toast.makeText(user_signup.this, "Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT ).show();
                        }
                    }
                });
            }

        });


    }




}






