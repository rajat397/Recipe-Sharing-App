package com.example.testingproject.Fragments;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.testingproject.MainActivity;
import com.example.testingproject.R;
import com.example.testingproject.UploadRecipeActivity;
import com.example.testingproject.models.Recipe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UploadRecipeFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private ProgressDialog progressDialog;


    private Button select;
    private ImageSwitcher imageView;
    private ArrayList<Uri> mArrayUri;
    private ArrayList<String>urlStrings;
    private int position = 0;
    private List<String> imagesEncodedList;
    private EditText dishTitle ;
    private EditText etIngredients;
    private EditText etDescription ;
    private Button publish ;
    ImageView cancel;

    public UploadRecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload_recipe, container, false);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Uploading Your Recipe");

         cancel = view.findViewById(R.id.cancel_img);
        select = view.findViewById(R.id.select);
        imageView = view.findViewById(R.id.image);
        mArrayUri = new ArrayList<Uri>();
        urlStrings = new ArrayList<String>();


        dishTitle =view.findViewById(R.id.dishTitle);
        etIngredients = view.findViewById(R.id.etIngredients);
        etDescription = view.findViewById(R.id.etdescription);
        publish =view.findViewById(R.id.btnPublish);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                final StorageReference reference = storage.getReference().child("Recipe Images").child(auth.getUid()).child(new Date().getTime()+"");
                for (int upload_count = 0; upload_count < mArrayUri.size(); upload_count++) {

                    Uri IndividualImage = mArrayUri.get(upload_count);
                    final StorageReference ImageName = reference.child("Images" + IndividualImage.getLastPathSegment());

                    ImageName.putFile(IndividualImage).addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    ImageName.getDownloadUrl().addOnSuccessListener(
                                            new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    urlStrings.add(uri.toString());
                                                    if (urlStrings.size() == mArrayUri.size()){
                                                        addDataToDatabase();
                                                    }


                                                }
                                            }
                                    );
                                }
                            }
                    );
                }
            }

        });






        // showing all images in imageswitcher
        imageView.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView1 = new ImageView(getApplicationContext());
                return imageView1;
            }
        });





        ImageView next1 = view.findViewById(R.id.next1);

        // click here to select next image
        next1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < mArrayUri.size() - 1) {
                    // increase the position by 1
                    position++;
                    imageView.setImageURI(mArrayUri.get(position));
                } else {
                    Toast.makeText(getContext(), "Last Image Already Shown", Toast.LENGTH_SHORT).show();
                }
            }
        });


        ImageView previous1 = view.findViewById(R.id.previous1);
        previous1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position > 0) {
                    // decrease the position by 1
                    position--;
                    imageView.setImageURI(mArrayUri.get(position));
                }
            }
        });


        imageView = view.findViewById(R.id.image);

        // click here to select image
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // initialising intent
                Intent intent = new Intent();
                cancel.setVisibility(View.VISIBLE);
                imageView.setBackground(null);
                // setting type to select to be image
                intent.setType("image/*");

                // allowing multiple image to be selected
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);


                //Launch activity to get result

                Intent.createChooser(intent, "Select Picture");
                mGetImage.launch(intent);

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mArrayUri.remove(position);
                if (position >= mArrayUri.size())
                    position--;

                if (mArrayUri.size() == 0) {
                    cancel.setVisibility(View.INVISIBLE);
                    imageView.setImageDrawable(null);
                    imageView.setBackgroundResource(R.drawable.ic_baseline_image_24);
                } else {
                    imageView.setImageURI(mArrayUri.get(position));
                }

            }
        });






        dishTitle.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String title =dishTitle.getText().toString();
                String ingredients = etIngredients.getText().toString();
                String description = etDescription.getText().toString();

                if(!description.isEmpty()&&!title.isEmpty()&&!ingredients.isEmpty())
                {

                    publish.setTextColor(getResources().getColor(R.color.green));
                    publish.setEnabled(true);



                }
                else{

                    publish.setTextColor(getResources().getColor(R.color.grey));
                    publish.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etIngredients.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String title =dishTitle.getText().toString();
                String ingredients = etIngredients.getText().toString();
                String description = etDescription.getText().toString();

                if(!description.isEmpty()&&!title.isEmpty()&&!ingredients.isEmpty())
                {

                    publish.setTextColor(getResources().getColor(R.color.green));
                    publish.setEnabled(true);



                }
                else{

                    publish.setTextColor(getResources().getColor(R.color.grey));
                    publish.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        etDescription.addTextChangedListener(new TextWatcher() {



            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String title =dishTitle.getText().toString();
                String ingredients = etIngredients.getText().toString();
                String description = etDescription.getText().toString();

                if((!description.isEmpty())&&(!title.isEmpty())&&(!ingredients.isEmpty()))
                {

                    publish.setTextColor(getResources().getColor(R.color.green));
                    publish.setEnabled(true);



                }
                else{

                    publish.setTextColor(getResources().getColor(R.color.grey));
                    publish.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




        return view;
    }

    public void addDataToDatabase(){


        Recipe recipe = new Recipe();
        recipe.setPublishedBy(auth.getUid());
        recipe.setPostedAt(new Date());
        recipe.setRecipeImages(urlStrings);
        recipe.setDishTitle(dishTitle.getText().toString());
        recipe.setIngredients(etIngredients.getText().toString());
        recipe.setDescription(etDescription.getText().toString());
        recipe.setValidate(false);


        database.getReference().child("Recipes").push().setValue(recipe).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        progressDialog.dismiss();
        publish.setTextColor(getResources().getColor(R.color.grey));
        publish.setEnabled(false);

        mArrayUri.clear();
    }


    ActivityResultLauncher<Intent> mGetImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data.getClipData() != null) {
                            ClipData mClipData = data.getClipData();
                            int cout = data.getClipData().getItemCount();
                            for (int i = 0; i < cout; i++) {
                                // adding imageuri in array
                                Uri imageurl = data.getClipData().getItemAt(i).getUri();
                                mArrayUri.add(imageurl);
                            }
                            // setting 1st selected image into image switcher
                            imageView.setImageURI(mArrayUri.get(0));
                            position = 0;
                        } else {
                            Uri imageurl = data.getData();
                            mArrayUri.add(imageurl);
                            imageView.setImageURI(mArrayUri.get(0));
                            position = 0;
                        }
                    } else {
                        // show this if no image is selected

                        if (mArrayUri.size() == 0) {
                            cancel.setVisibility(View.INVISIBLE);
                            imageView.setBackgroundResource(R.drawable.ic_baseline_image_24);
                        }
                        Toast.makeText(getContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
                    }

                }
            });
}