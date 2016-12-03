package com.asamzgeeks.psc.adminconsole;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    EditText etPassword;
    RelativeLayout relativeLayout;
    private FirebaseAuth mAuth;
    ProgressDialog progressBar;
     DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etPassword=(EditText)findViewById(R.id.etPassword);
        relativeLayout=(RelativeLayout)findViewById(R.id.activity_main);
        mAuth   = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        progressBar=new ProgressDialog(this);
    }
    public void checkProceed(View view){
        if(etPassword.getText().toString().trim().equals("psctrollrocks")){
            progressBar.setMessage("Signig In, Please Wait");
            progressBar.setCancelable(false);
            progressBar.show();
            new checkUser().execute();

        }
        else{
          //  Toast.makeText(getApplicationContext(),"Enter Valid Password", Toast.LENGTH_SHORT).show();
          Snackbar snackbar = Snackbar.make(relativeLayout, "Password is wrong", Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    }
    private class checkUser extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected Boolean doInBackground(Void... params) {
           // DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");

            mAuth.signInWithEmailAndPassword("psctroll@gmail.com", "psctrollrocks").addOnCompleteListener(new OnCompleteListener<AuthResult>() {


                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent mainActivity=new Intent(MainActivity.this,HomeActivity.class);
                        startActivity(mainActivity);
                        finish();
                    }
                    else{
                        Snackbar snackbarbg = Snackbar.make(relativeLayout, "Sign In Failed, Try Again", Snackbar.LENGTH_LONG);
                        snackbarbg.show();

                    }
                }
            });
            return true;
        }
    }
}
