package com.example.testingproject;
import static android.view.View.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class user_login extends AppCompatActivity implements OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
    TextView sup=(TextView)findViewById(R.id.signupbutt);
    sup.setOnClickListener(this);
    }
    public void onClick(View v){
        startActivity(new Intent(user_login.this,user_signup.class));
    }

}

