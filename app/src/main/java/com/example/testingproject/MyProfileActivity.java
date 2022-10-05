package com.example.testingproject;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.testingproject.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class MyProfileActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        getSupportActionBar().hide();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();


        EditText etUserName = findViewById(R.id.etUserName);
        EditText etAbout = findViewById(R.id.etAbout);
        Button btnEdit = findViewById(R.id.btnEdit);
        Button btnSave = findViewById(R.id.btnSave);


        database.getReference().child("Users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 User user = snapshot.getValue(User.class);
                String username , about;

                username = user.getUserName();
                about = user.getAbout();
                etUserName.setText(username);
                 if(user.getAbout()!=null)
                 {
                     etAbout.setText(about);
                 }
                 else
                 {
                     etAbout.setText("");
                 }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        etUserName.setEnabled(false);
        etAbout.setEnabled(false);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etAbout.setEnabled(true);
                etUserName.setEnabled(true);

                etAbout.setHint("Enter about yourself");
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        String new_userName, new_About;
                        new_userName= etUserName.getText().toString();
                        new_About= etAbout.getText().toString();


                        if(TextUtils.isEmpty(new_userName) )
                        {
                            Toast.makeText(MyProfileActivity.this,"User Name is required",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        etUserName.setEnabled(false);
                        etAbout.setEnabled(false);

                        etUserName.setText(new_userName);
                        etAbout.setText(new_About);

                        HashMap<String, Object>obj = new HashMap<>();
                        obj.put("userName",new_userName);
                        obj.put("about",new_About);
                        database.getReference().child("Users").child(auth.getUid()).updateChildren(obj);
                        etAbout.setHint("");

                    }
                });
            }
        });




        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyProfileActivity.this, MainActivity.class));
                finish();
            }
        });

        ImageView profileImage = findViewById(R.id.profile_image);
        database.getReference().child("Users").child(auth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        Picasso.get().load(user.getProfilepic()).placeholder(R.drawable.user_avatar).into(profileImage);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        ImageView plus = findViewById(R.id.plus_sign);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
//                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                //Launch activity to get result
                mGetImage.launch(intent);

            }
        });
    }


    ActivityResultLauncher<Intent> mGetImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Uri uri = data.getData();
                        ImageView profileImage = findViewById(R.id.profile_image);
                        profileImage.setImageURI(uri);
                        StorageReference reference = storage.getReference().child("Profile Images").child(auth.getUid());
                        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        database.getReference().child("Users").child(auth.getUid()).child("profilepic").setValue(uri.toString());
                                    }
                                });
                                Toast.makeText(MyProfileActivity.this, "uploaded successfully", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                }

            });
}



