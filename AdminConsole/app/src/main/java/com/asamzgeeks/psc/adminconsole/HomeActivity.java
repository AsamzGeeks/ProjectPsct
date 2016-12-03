package com.asamzgeeks.psc.adminconsole;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
    public void goImage(View view){
        Intent mainActivity=new Intent(HomeActivity.this,ImageUploader.class);
        startActivity(mainActivity);

    }
    public void goQuiz(View view){
        Intent mainActivity=new Intent(HomeActivity.this,QuizUploader.class);
        startActivity(mainActivity);

    }

}
