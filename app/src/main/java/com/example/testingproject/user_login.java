package com.example.testingproject;
import static android.view.View.*;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.BreakIterator;

public class user_login extends AppCompatActivity {
    private TextView tv;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);


        tv=findViewById(R.id.loginsign);
        Typeface customfont=Typeface.createFromAsset(getAssets(),"fonts/Lobster-Regular.ttf");
        tv.setTypeface(customfont);
        TextView signUpPromt = findViewById(R.id.SignUpPromt);
        // ...
// Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog= new ProgressDialog(user_login.this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Login to your account");
        EditText EmailLogin = findViewById(R.id.etEmailLogin);
        EditText PasswordLogin= findViewById(R.id.etPasswordLogin);

        Button btnLogin= findViewById(R.id.btnLogin);

        if(mAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(user_login.this,MainActivity.class));
        }


        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                mAuth.signInWithEmailAndPassword(EmailLogin.getText().toString(),PasswordLogin.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            startActivity(new Intent(user_login.this,MainActivity.class));
                            Toast.makeText(user_login.this, "Login Successfull",Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Toast.makeText(user_login.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        signUpPromt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(user_login.this,user_signup.class));
            }
        });



    }

}

