package com.asamz.psctroll;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import pl.droidsonroids.gif.GifTextView;

public class SplashScreen extends AppCompatActivity {
   private ActionBar actionBar;
    private pl.droidsonroids.gif.GifTextView gifView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        actionBar = getSupportActionBar();
        //Hiding the action bar, since it is a splash screen activity
        actionBar.hide();


    }
}
