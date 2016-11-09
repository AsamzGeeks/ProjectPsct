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
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        actionBar = getSupportActionBar();
        SplashZoomin= AnimationUtils.loadAnimation(this, R.anim.zoomin);
        SplashFadein= AnimationUtils.loadAnimation(this, R.anim.fadein);
        //Hiding the action bar, since it is a splash screen activity
        actionBar.hide();
        ivPscIcon=(ImageView)findViewById(R.id.ivPscIcon);
        ivAsamz=(ImageView)findViewById(R.id.ivAsamz);
        gifLoadBar=(GifTextView)findViewById(R.id.gfLoadBar);
        ivPscIcon.startAnimation(SplashZoomin);
        ivAsamz.startAnimation(SplashZoomin);
        gifLoadBar.startAnimation(SplashFadein);
        mAuth=FirebaseAuth.getInstance();
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    Intent signIn=new Intent(SplashScreen.this,SignUpActivity.class);
                    //command to prevent user getting va
                    signIn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(signIn);
                }
                else{

                }
            }
        };


    }
}
