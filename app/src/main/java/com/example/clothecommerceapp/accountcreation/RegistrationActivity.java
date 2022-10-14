package com.example.clothecommerceapp.accountcreation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.clothecommerceapp.R;
import com.example.clothecommerceapp.model.UsersModel;
import com.example.clothecommerceapp.view.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    EditText regName,regEmail,regPass,regConfirmPass;
    AppCompatButton signUpButton,logInButtonRegister;

    String imgUri;
    String emailPattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;



    // defining our own password pattern
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[@#$%^&+=])" +     // at least 1 special character
                    "(?=\\S+$)" +            // no white spaces
                    ".{4,}" +                // at least 4 characters
                    "$");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        //hide status bar
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();

        regName=findViewById(R.id.regUsername);
        regEmail=findViewById(R.id.regEmail);
        regPass=findViewById(R.id.regPass);
        regConfirmPass=findViewById(R.id.regConfirmPass);


        signUpButton=findViewById(R.id.signUpButton);


        logInButtonRegister=findViewById(R.id.logInButtonRegister);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);

        regisProcess();
    }


    private void regisProcess(){
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String name=regName.getEditableText().toString();
                String email=regEmail.getEditableText().toString();
                String password=regPass.getEditableText().toString();
                String cPassword=regConfirmPass.getEditableText().toString();

                if(TextUtils.isEmpty(name)
                        || TextUtils.isEmpty(email)
                        || TextUtils.isEmpty(password)
                        || TextUtils.isEmpty(cPassword) ){
                    progressDialog.dismiss();

                    Toast.makeText(RegistrationActivity.this,"Enter Valid Data", Toast.LENGTH_SHORT).show();
                }else if(!email.matches(emailPattern)){
                    progressDialog.dismiss();
                    regEmail.setError("Invalid email");
                    Toast.makeText(RegistrationActivity.this, "Invalid email", Toast.LENGTH_SHORT).show();

                }else if (!PASSWORD_PATTERN.matcher(password).matches()){
                    progressDialog.dismiss();
                    regPass.setError("Invalid password");
                    Toast.makeText(RegistrationActivity.this, "Please Enter 6 character Password", Toast.LENGTH_SHORT).show();


                }else if (!password.equals(cPassword)) {
                    progressDialog.dismiss();

                    Toast.makeText(RegistrationActivity.this, "Password does not march", Toast.LENGTH_SHORT).show();
                }else{

                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){


                                DatabaseReference reference=database.getReference().child("users")
                                        .child(auth.getCurrentUser().getUid());
                                //basically creating storage for uploaded pic for that specific id
                                //for every unique user if new folder will be created and that will contain
                                //updated picture of user
                                StorageReference storageReference = storage.getReference().child("upload")
                                        .child(auth.getCurrentUser().getUid());

                                //link of default picture
                                imgUri="https://firebasestorage.googleapis.com/v0/b/colothingecommerceapp.appspot.com/o/profilepic.png?alt=media&token=098d7588-d74b-470b-9f8d-960baf121741";


                                //create a new model class for users

                                UsersModel users =new UsersModel(auth.getCurrentUser().getUid(),name,email,imgUri);


                                reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful()){
                                            progressDialog.dismiss();

                                            startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));
                                        }else{
                                            progressDialog.dismiss();
                                            Toast.makeText(RegistrationActivity.this,"Error in creating a new user", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });

                            }else{

                                //if task is not successful
                                progressDialog.dismiss();
                                Toast.makeText(RegistrationActivity.this,"Something went wrong", Toast.LENGTH_SHORT).show();


                            }
                        }
                    });


                }

            }
        });

        logInButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });


    }
}