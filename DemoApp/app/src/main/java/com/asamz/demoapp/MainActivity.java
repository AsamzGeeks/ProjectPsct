package com.asamz.demoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
   private Button btnTopic,btnTrainer,btnDates,btnLocation,btnStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStatus=(Button)findViewById(R.id.btnStatus);
    }
    public void goClassroom(View view){
        Intent classroom=new Intent(MainActivity.this,Classroom.class);
        startActivity(classroom);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.firstMenu:
                Intent goPreference=new Intent(MainActivity.this,Preference.class);
                startActivity(goPreference);
                return true;
            default:
                return true;
        }
    }
}
