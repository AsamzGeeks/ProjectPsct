package com.asamz.psctroll;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import pl.droidsonroids.gif.GifTextView;
public class SplashScreen extends AppCompatActivity {
    private ActionBar actionBar;
    private pl.droidsonroids.gif.GifTextView gifView;
    private ImageView ivPscIcon,ivAsamz;
    GifTextView gifLoadBar;
    private Animation SplashZoomin,SplashFadein;
    private FirebaseAuth sAuth;
    private  FirebaseAuth.AuthStateListener sAuthListener;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        actionBar = getSupportActionBar();
        sAuth=FirebaseAuth.getInstance();

        SplashZoomin= AnimationUtils.loadAnimation(this, R.anim.zoomin);
        SplashFadein= AnimationUtils.loadAnimation(this, R.anim.fadein);
        //Hiding the action bar, since it is a splash screen activity
        actionBar.hide();
        ivPscIcon=(ImageView)findViewById(R.id.ivPscIcon);
        ivAsamz=(ImageView)findViewById(R.id.ivAsamz);
        gifLoadBar=(GifTextView)findViewById(R.id.gfLoadBar);
        ivPscIcon.startAnimation(SplashFadein);
        ivAsamz.startAnimation(SplashFadein);
        gifLoadBar.startAnimation(SplashFadein);
        sAuthListener= new FirebaseAuth.AuthStateListener() {
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



    }

    @Override
    protected void onStart() {
        super.onStart();
        sAuth.addAuthStateListener(sAuthListener);
    }
}
