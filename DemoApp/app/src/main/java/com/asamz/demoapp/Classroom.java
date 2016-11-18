package com.asamz.demoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Classroom extends AppCompatActivity {
    private Button btnFeedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom);
        btnFeedback=(Button)findViewById(R.id.btnFeedback);
    }
    public  void goFeedback(View view){
        Intent goFeedback=new Intent(Classroom.this,FeedBack.class);
        startActivity(goFeedback);
    }
}
