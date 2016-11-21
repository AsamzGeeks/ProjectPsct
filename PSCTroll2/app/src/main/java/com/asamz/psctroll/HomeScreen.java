package com.asamz.psctroll;

import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class HomeScreen extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private  ActionBarDrawerToggle mToggle;
    private ActionBar actionBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*actionBar = getSupportActionBar();
        //Hiding the action bar, since it is a splash screen activity
        actionBar.hide(); */
        setContentView(R.layout.activity_home_screen);
        mDrawer=(DrawerLayout)findViewById(R.id.drawer_layout);
        mToggle=new ActionBarDrawerToggle(this,mDrawer,R.string.drawer_open,R.string.drawer_close);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
