package com.example.clothecommerceapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.clothecommerceapp.R;
import com.example.clothecommerceapp.accountcreation.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        auth=FirebaseAuth.getInstance();

        runNextScreen();
        askForFullScreen();
    }

    private void runNextScreen(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //logic if user already login
                if(auth.getCurrentUser()==null) {

                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));

                }else{
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));


                }
                finish();

            }
        }, 2500);


    }

    private void askForFullScreen(){
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE



        );


    }
}