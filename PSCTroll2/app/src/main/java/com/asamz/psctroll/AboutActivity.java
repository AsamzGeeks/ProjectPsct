package com.asamz.psctroll;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class AboutActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    private ActionBar actionBar;
    private SharedPreferences LoginCredentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout_about);
        mToggle = new ActionBarDrawerToggle(this, mDrawer, R.string.drawer_open, R.string.drawer_close);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initNavigationDrawer();
        LoginCredentials=getSharedPreferences("loginStatus", Activity.MODE_PRIVATE);
    }
    public  void initNavigationDrawer() {
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view_about);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.home:
                        Intent aboutIntent=new Intent(AboutActivity.this,HomeScreen.class);
                        startActivity(aboutIntent);
                        mDrawer.closeDrawers();
                        break;
                    case R.id.profile:
                        Intent profileIntent = new Intent(AboutActivity.this, ProfileActivity.class);
                        startActivity(profileIntent);
                        mDrawer.closeDrawers();
                        break;
                    case R.id.trollbox:
                        Intent boxIntent=new Intent(AboutActivity.this,TrollBoxActivity.class);
                        startActivity(boxIntent);
                        mDrawer.closeDrawers();
                        break;
                    case R.id.test:
                        Intent testIntent=new Intent(AboutActivity.this,TestActivity.class);
                        startActivity(testIntent);
                        mDrawer.closeDrawers();
                        break;
                    case R.id.settings:
                        Intent settingsIntent=new Intent(AboutActivity.this,SettingsActivity.class);
                        startActivity(settingsIntent);
                        mDrawer.closeDrawers();
                        break;
                    case R.id.donate:
                        Intent donateIntent=new Intent(AboutActivity.this,SettingsActivity.class);
                        startActivity(donateIntent);
                        mDrawer.closeDrawers();
                        break;
                    case R.id.about:

                        mDrawer.closeDrawers();
                        break;
                    case R.id.logout:
                        mDrawer.closeDrawers();
                        Intent logoutIntent=new Intent(AboutActivity.this,LoginActivity.class);
                        SharedPreferences.Editor editor=LoginCredentials.edit();
                        editor.putBoolean("loggedIn",false);
                        editor.apply();
                        startActivity(logoutIntent);
                        finish();
                        break;
                }
                return true;
            }

        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}