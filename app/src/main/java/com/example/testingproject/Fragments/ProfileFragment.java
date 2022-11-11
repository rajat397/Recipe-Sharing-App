package com.example.testingproject.Fragments;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testingproject.MainActivity;
import com.example.testingproject.R;
import com.example.testingproject.adapter.MyRecipesAdapter;
import com.example.testingproject.models.Recipe;
import com.example.testingproject.models.User;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileFragment extends Fragment {

    ImageView profileImage;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private FirebaseAuth auth;
    RecyclerView recview;
    MyRecipesAdapter adapter;
    ArrayList<Recipe> list;
    private ProgressDialog progressDialog;
    public ProfileFragment() {
        // Required empty public constructor
    }


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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);

//        View view = inflater.inflate(R.layout.fragment_home, container, false);


        recview=view.findViewById(R.id.profileRecView);
        recview.setLayoutManager(new ProfileFragment.WrapContentLinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));



        TextView tv=view.findViewById(R.id.yourrecipestext);
        Typeface customfont = Typeface.createFromAsset(getContext().getAssets(), "fonts/Lobster-Regular.ttf");
        tv.setTypeface(customfont);

        Query query = FirebaseDatabase.getInstance().getReference().child("Recipes").orderByChild("publishedBy").equalTo(FirebaseAuth.getInstance().getUid());
        FirebaseRecyclerOptions<Recipe> options=
                new FirebaseRecyclerOptions.Builder<Recipe>()
                        .setQuery(query,Recipe.class)
                        .build();

        adapter = new MyRecipesAdapter(getActivity(),options);
        recview.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        profileImage = view.findViewById(R.id.profile_image);


        EditText etUserName = view.findViewById(R.id.etUserName);
        EditText etAbout = view.findViewById(R.id.etAbout);
        Button btnEdit = view.findViewById(R.id.btnEdit);
        Button btnSave = view.findViewById(R.id.btnSave);
        TextView txtvi=view.findViewById(R.id.txtcnt);
        etAbout.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int length=txtvi.length();
                String convert=String.valueOf(length);
                txtvi.setText(convert+"/150 Chars");;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                txtvi.setText(String.valueOf(charSequence.length())+"/150 Chars");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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
                            Toast.makeText(getContext(),"User Name is required",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        etUserName.setEnabled(false);
                        etAbout.setEnabled(false);

                        etUserName.setText(new_userName);
                        etAbout.setText(new_About);

                        HashMap<String, Object> obj = new HashMap<>();
                        obj.put("userName",new_userName);
                        obj.put("about",new_About);
                        database.getReference().child("Users").child(auth.getUid()).updateChildren(obj);
                        etAbout.setHint("");

                    }
                });
            }
        });






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


        ImageView plus = view.findViewById(R.id.plus_sign);
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



        return view;
    }


    private ActivityResultLauncher<Intent> mGetImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Uri uri = data.getData();
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
                                Toast.makeText(getContext(), "uploaded successfully", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                }

            });


    @Override
    public  void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
     public  void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}