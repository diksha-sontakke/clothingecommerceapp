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
import com.example.clothecommerceapp.view.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {


    FirebaseAuth auth;
    EditText loginEmail,loginPass;
    AppCompatButton signInButton,signUpButtonLogin;

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
        setContentView(R.layout.activity_login);

        //for status bar
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );


        auth=FirebaseAuth.getInstance();

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);

        loginEmail=findViewById(R.id.loginEmail);
        loginPass=findViewById(R.id.loginPass);



        signInButton=findViewById(R.id.signInButton);

        signUpButtonLogin=findViewById(R.id.signUpButtonLogin);




        loginProcess();
    }


    private void loginProcess(){

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();

                String email=loginEmail.getEditableText().toString();
                String password=loginPass.getEditableText().toString();


                if (TextUtils.isEmpty(email)
                        || TextUtils.isEmpty(password) )
                {
                    progressDialog.dismiss();

                    Toast.makeText(LoginActivity.this,"Enter Valid Data", Toast.LENGTH_SHORT).show();

                }else if(!email.matches(emailPattern)){
                    progressDialog.dismiss();
                    loginEmail.setError("Invalid email");
                    Toast.makeText(LoginActivity.this, "Invalid email", Toast.LENGTH_SHORT).show();

                }else if (!PASSWORD_PATTERN.matcher(password).matches()){
                    progressDialog.dismiss();
                    loginPass.setError("Invalid password");
                    Toast.makeText(LoginActivity.this,
                            "Please Enter 6 character Password\nat least 1 special character\nno white spaces\n4 character",
                            Toast.LENGTH_SHORT).show();


                }else{



                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();

                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            }else{

                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this,"Error in login", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }


            }
        });
        signUpButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(intent);
            }
        });



    }
}