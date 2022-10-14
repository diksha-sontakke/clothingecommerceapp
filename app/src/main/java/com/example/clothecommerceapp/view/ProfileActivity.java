package com.example.clothecommerceapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clothecommerceapp.R;
import com.example.clothecommerceapp.accountcreation.LoginActivity;
import com.example.clothecommerceapp.model.UsersModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    ImageView back;
    ImageView profileImg,menuUserImage;
    EditText profileUsername;
    TextView profileEmail, profileLogout,doneEditing, displayUsername;

    String emailId;




    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    Uri setImageUri;

    Dialog dialog;

    DatabaseReference reference;
    StorageReference storageReference;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);


        back=findViewById(R.id.profileBack);
        doneEditing=findViewById(R.id.doneEditing);
        profileImg=findViewById(R.id.profileImage);
        profileUsername=findViewById(R.id.profileUsername);
        profileEmail=findViewById(R.id.profileEmail);
        profileLogout=findViewById(R.id.profileLogout);
        menuUserImage=findViewById(R.id.menuUserImage);
        displayUsername=findViewById(R.id.displayUsername);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();

        dataReference();

        profileImageUpdateFromGallery();
        menuUserImageForHistoryActivity();
        doneButton();
        profileLogoutButton();
        bacKImage();


        menuMethod();

    }

    public void menuMethod() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        //set home selected
        bottomNavigationView.setSelectedItemId(R.id.profile);


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0, 0);
                        return true;




                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.my_cart:
                        startActivity(new Intent(getApplicationContext(), MyCartBagActivity.class));
                        overridePendingTransition(0, 0);

                        return true;

                    case R.id.profile:

                        return true;


                }

                //true before
                return true;
            }
        });
    }

    public void dataReference(){

        reference= database.getReference().child("users").child(auth.getUid());



        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                emailId=snapshot.child("email").getValue().toString();  //accessing email from db
                String name= snapshot.child("name").getValue().toString();
                String image = snapshot.child("imageUri").getValue().toString();

                profileUsername.setText(name);
                displayUsername.setText(name);
                profileEmail.setText(emailId);
                Picasso.get().load(image).into(profileImg);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void profileImageUpdateFromGallery(){

        //when click on profile pic option to update profile from gallery
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //check below
                startActivityForResult(intent, 1);
            }
        });
    }

    private void menuUserImageForHistoryActivity(){

        menuUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu= new PopupMenu(ProfileActivity.this,menuUserImage);
                popupMenu.getMenuInflater().inflate(R.menu.order_history_menu,popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.orderHistory:
                                startActivity(new Intent(getApplicationContext(),OrderHistoryActivity.class));
                                overridePendingTransition(0, 0);
                                return true;
                            default:
                                return  false;


                        }

                    }
                });

                popupMenu.show();
            }
        });


    }

    private void doneButton(){
        //to update the profile pic
        storageReference = storage.getReference().child("upload").child(auth.getUid());

        doneEditing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show(); //till info get updated
                String name = profileUsername.getText().toString();
                String email = profileEmail.getText().toString();
                displayUsername.setText(name); //hello user the user will change

                if (setImageUri != null) {
                    //new image form the gallery

                    storageReference.putFile(setImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String finalImageUri = uri.toString();
                                    UsersModel users = new UsersModel(auth.getUid(), name, email, finalImageUri);
                                    Log.i("Url:",storageReference.getDownloadUrl().toString());

                                    //for new updated img,email in db
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();
                                                Toast.makeText(ProfileActivity.this, "Changes Saved!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(ProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                } else {
                    String imgUri = "https://firebasestorage.googleapis.com/v0/b/applicationall-3ae09.appspot.com/o/profilepic.png?alt=media&token=4826c841-a9fb-4bf1-9e7d-a3c59204567c";
                    UsersModel users = new UsersModel(auth.getUid(), name, email, imgUri);

                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(ProfileActivity.this, "Changes Saved!", Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(ProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    }

    private void profileLogoutButton(){
        profileLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog= new Dialog(ProfileActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_layout);

                TextView yesButton, noButton;
                yesButton= dialog.findViewById(R.id.yesButton);
                noButton= dialog.findViewById(R.id.noButton);

                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                    }
                });

                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });



    }


    private void bacKImage(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setImageUri = data.getData();

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), setImageUri);
                profileImg.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }





}