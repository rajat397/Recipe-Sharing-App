package com.example.testingproject;
import static android.view.View.*;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testingproject.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.BreakIterator;

public class user_login extends AppCompatActivity {
    private TextView tv,signUpPrompt;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private ProgressDialog progressDialog;
    public Typeface customfont;
    private EditText EmailLogin,PasswordLogin;
    private Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        tv=findViewById(R.id.loginsign);
        customfont=Typeface.createFromAsset(getAssets(),"fonts/Lobster-Regular.ttf");
        tv.setTypeface(customfont);
        signUpPrompt = findViewById(R.id.SignUpPromt);

            // ...
// Initialize Firebase Auth
            mAuth = FirebaseAuth.getInstance();
            database = FirebaseDatabase.getInstance();
            progressDialog = new ProgressDialog(user_login.this);
            progressDialog.setTitle("Login");
            progressDialog.setMessage("Login to your account");
            EmailLogin = findViewById(R.id.etEmailLogin);
            PasswordLogin = findViewById(R.id.etPasswordLogin);
            btnLogin= findViewById(R.id.btnLogin);

        if(mAuth.getCurrentUser()!=null)
        {
            FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    String userType = user.getUserType();
                    if(userType.equals("GENERAL"))
                    {
                        progressDialog.hide();
                        startActivity(new Intent(user_login.this, MainActivity.class));
                        finish();


                    }
                    else{
                        progressDialog.hide();
                        startActivity(new Intent(user_login.this, adminPage.class));
                        finish();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

      
        }


        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(EmailLogin.getText().toString()) || EmailLogin==null)
                {
                    EmailLogin.setError("Email is invalid");
                    Toast.makeText(user_login.this,"Email is invalid",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(PasswordLogin.getText().toString()) || PasswordLogin==null)
                {
                    PasswordLogin.setError("Password is invalid");
                    Toast.makeText(user_login.this,"Password is invalid",Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.show();
                mAuth.signInWithEmailAndPassword(EmailLogin.getText().toString(),PasswordLogin.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {

                            FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    User user = snapshot.getValue(User.class);
                                    String userType = user.getUserType();
                                    if(userType.equals("GENERAL"))
                                    {
                                        progressDialog.hide();
                                        startActivity(new Intent(user_login.this, MainActivity.class));
                                        finish();


                                    }
                                    else{
                                        progressDialog.hide();
                                        startActivity(new Intent(user_login.this, adminPage.class));
                                        finish();

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            Toast.makeText(user_login.this, "Login Successfull",Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Toast.makeText(user_login.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progressDialog.hide();
                        }
                    }
                });
            }
        });

        signUpPrompt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(user_login.this,user_signup.class));
                finish();
            }
        });



    }

}

