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
import pl.droidsonroids.gif.GifTextView;
public class SplashScreen extends AppCompatActivity {
    private ActionBar actionBar;
    private pl.droidsonroids.gif.GifTextView gifView;
    private ImageView ivPscIcon,ivAsamz;
    GifTextView gifLoadBar;
    private Animation SplashZoomin,SplashFadein;

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
        ivPscIcon.startAnimation(SplashFadein);
        ivAsamz.startAnimation(SplashFadein);
        gifLoadBar.startAnimation(SplashFadein);



    }
}
