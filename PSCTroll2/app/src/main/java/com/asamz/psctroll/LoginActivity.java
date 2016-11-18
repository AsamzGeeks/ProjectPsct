package com.asamz.psctroll;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pkmmte.view.CircularImageView;

public class LoginActivity extends AppCompatActivity {
    private EditText emailField, passwordField;
    private TextView signUp, passwordReset;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ProgressBar pbLogin;
    private ImageView tvLogin;
    String emailLogin;
    String passwordLogin;
    SharedPreferences  LoginCredentials ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailField = (EditText) findViewById(R.id.etEmailLogin);
        passwordField = (EditText) findViewById(R.id.EtPasswordLogin);
        signUp = (TextView) findViewById(R.id.tvSignUp);
        passwordReset = (TextView) findViewById(R.id.tvForgot);
        mAuth = FirebaseAuth.getInstance();
        pbLogin = (ProgressBar) findViewById(R.id.pbLogin);
        pbLogin.setVisibility(View.INVISIBLE);
        tvLogin = (ImageView) findViewById(R.id.tvLogin);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        LoginCredentials=getSharedPreferences("loginStatus", Activity.MODE_PRIVATE);


    }

    public void LoginCheck(View view) {
        if(isNetworkAvailable()){

        if (emailField.getText().toString().isEmpty()||passwordField.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Fill All The Fields", Toast.LENGTH_SHORT).show();
        } else {
            tvLogin.setVisibility(View.INVISIBLE);
            emailLogin = emailField.getText().toString().trim();
            passwordLogin = passwordField.getText().toString().trim();
            signUp.setVisibility(View.INVISIBLE);
            passwordReset.setVisibility(View.INVISIBLE);
            pbLogin.setVisibility(View.VISIBLE);
            new checkUser().execute();
        }

        }
        else{
            Toast.makeText(getApplicationContext(), "Internet is not connected", Toast.LENGTH_SHORT).show();
        }
    }

    private class checkUser extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            mAuth.signInWithEmailAndPassword(emailLogin, passwordLogin).addOnCompleteListener(new OnCompleteListener<AuthResult>() {


                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                      final String user_id=  mAuth.getCurrentUser().getUid();

                        mDatabase.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(user_id)) {
                                    SharedPreferences.Editor editor=LoginCredentials.edit();
                                    editor.putBoolean("loggedIn",true);
                                    editor.putString("Email",emailLogin);
                                    editor.putString("Password",passwordLogin);
                                    editor.apply();
                                    Intent HomeActivity = new Intent(LoginActivity.this, HomeScreen.class);
                                    HomeActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(HomeActivity);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Sign In Failed", Toast.LENGTH_SHORT).show();
                                    signUp.setVisibility(View.VISIBLE);
                                    passwordReset.setVisibility(View.VISIBLE);
                                    pbLogin.setVisibility(View.INVISIBLE);
                                    tvLogin.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(), "Sign In Failed", Toast.LENGTH_SHORT).show();
                                signUp.setVisibility(View.VISIBLE);
                                passwordReset.setVisibility(View.VISIBLE);
                                pbLogin.setVisibility(View.INVISIBLE);
                                tvLogin.setVisibility(View.VISIBLE);
                            }


                        });


                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Sign In Failed,Try Again", Toast.LENGTH_SHORT).show();
                        signUp.setVisibility(View.VISIBLE);
                        passwordReset.setVisibility(View.VISIBLE);
                        pbLogin.setVisibility(View.INVISIBLE);
                        tvLogin.setVisibility(View.VISIBLE);
                    }

                }

            });
            return null;
        }
    }

        public void goSignUp(View view) {
            Intent signUpIntent = new Intent(LoginActivity.this, SignUp.class);
            startActivity(signUpIntent);
        }

        public void goReset(View view) {
            Intent resetIntent = new Intent(LoginActivity.this, ResetActivity.class);
            startActivity(resetIntent);
        }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    }

