package com.asamz.psctroll;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

import pl.droidsonroids.gif.GifTextView;
public class SplashScreen extends AppCompatActivity {
    private ActionBar actionBar;
    private ImageView ivPscIcon;
    ProgressBar pbLoadBar;
    private FirebaseAuth sAuth;
    private FirebaseAuth.AuthStateListener sAuthListener;
    SharedPreferences LoginCredentials;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        actionBar = getSupportActionBar();
        sAuth = FirebaseAuth.getInstance();
        //Hiding the action bar, since it is a splash screen activity
        actionBar.hide();
        ivPscIcon = (ImageView) findViewById(R.id.ivPscIcon);
        pbLoadBar=(ProgressBar)findViewById(R.id.pbSplashLoad);
        LoginCredentials=getSharedPreferences("loginStatus", Activity.MODE_PRIVATE);
        final boolean status=LoginCredentials.getBoolean("loggedIn",false);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent i = new Intent(SplashScreen.this, SplashScreen.class);
                startActivity(i);
                if(status){
                    Intent mainAct=new Intent(SplashScreen.this,HomeScreen.class);
                    mainAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainAct);
                    finish();
                }
                else{
                    Intent signIn=new Intent(SplashScreen.this,LoginActivity.class);
                    signIn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(signIn);
                    finish();
                }


            }
        }, 3000);


      /*  sAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            if(firebaseAuth.getCurrentUser()==null){
                Intent signIn=new Intent(SplashScreen.this,LoginActivity.class);
                signIn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(signIn);
            }
                else{
                Intent mainAct=new Intent(SplashScreen.this,HomeScreen.class);
                mainAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainAct);
            }
            }
        };





    @Override
    protected void onStart() {
        super.onStart();
        sAuth.addAuthStateListener(sAuthListener);
    } */
    }


}