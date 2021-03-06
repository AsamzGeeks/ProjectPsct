package com.asamz.psctroll;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ResetActivity extends AppCompatActivity {
    TextView tvSignIn;
    EditText etResetEmail;
    ImageView ivReset;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    String emailID;
    ProgressBar pbResetLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        tvSignIn = (TextView) findViewById(R.id.tvBackLogin);
        etResetEmail = (EditText) findViewById(R.id.etEmailReset);
        ivReset = (ImageView) findViewById(R.id.ivResetPassword);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        pbResetLoad=(ProgressBar)findViewById(R.id.pbResetLoad);
        pbResetLoad.setVisibility(View.INVISIBLE);
    }

    public void passwordReset(View view) {
        if (isNetworkAvailable() == false) {
            Toast.makeText(getApplicationContext(), "Please connect to the internet", Toast.LENGTH_SHORT).show();
        } else {
            if (etResetEmail.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please Fill All The Fields", Toast.LENGTH_SHORT).show();
            }
            else {
                pbResetLoad.setVisibility(View.VISIBLE);
                tvSignIn.setVisibility(View.INVISIBLE);
                ivReset.setVisibility(View.INVISIBLE);
                new startResetActivity().execute();
                emailID = etResetEmail.getText().toString().trim();

            }
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private  class startResetActivity extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            mAuth.sendPasswordResetEmail(emailID).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        pbResetLoad.setVisibility(View.INVISIBLE);
                        tvSignIn.setVisibility(View.VISIBLE);
                        ivReset.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "Check your E-Mail For Resetting Password", Toast.LENGTH_SHORT).show();
                    } else {
                        pbResetLoad.setVisibility(View.INVISIBLE);
                        tvSignIn.setVisibility(View.VISIBLE);
                        ivReset.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "Error! Try Again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return null;
        }
    }
    public void gotoLoginAgain(View view){
        Intent HomeActivity = new Intent(ResetActivity.this, LoginActivity.class);
        startActivity(HomeActivity);
    }
}