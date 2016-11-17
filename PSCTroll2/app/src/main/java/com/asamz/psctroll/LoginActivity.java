package com.asamz.psctroll;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private EditText emailField,passwordField;
    private TextView signUp,passwordReset;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailField=(EditText)findViewById(R.id.etEmailLogin);
        passwordField=(EditText)findViewById(R.id.EtPasswordLogin);
        signUp=(TextView)findViewById(R.id.tvSignUp);
        passwordReset=(TextView)findViewById(R.id.tvForgot);
        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("users");

    }
    public void LoginCheck(View view){
        String emailLogin=emailField.getText().toString().trim();
        String passwordLogin=passwordField.getText().toString().trim();
        if(TextUtils.isEmpty(emailLogin)||TextUtils.isEmpty(passwordLogin)){
            Toast.makeText(getApplicationContext(), "Please Fill All The Fields", Toast.LENGTH_SHORT).show();
        }
        else{

            signUp.setVisibility(View.INVISIBLE);
            passwordReset.setVisibility(View.INVISIBLE);

            mAuth.signInWithEmailAndPassword(emailLogin,passwordLogin).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        checkUser();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Sign In Failed,Try Again", Toast.LENGTH_SHORT).show();

                        signUp.setVisibility(View.VISIBLE);
                        passwordReset.setVisibility(View.VISIBLE);

                    }
                }
            });
        }
    }
    public void checkUser(){
       final String user_id=mAuth.getCurrentUser().getUid();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user_id)){
                    Intent HomeActivity=new Intent(LoginActivity.this,HomeScreen.class);
                    HomeActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(HomeActivity);
                }
                else{
                    Toast.makeText(getApplicationContext(), "User Does not Exist, Please Sign Up", Toast.LENGTH_SHORT).show();

                    signUp.setVisibility(View.VISIBLE);
                    passwordReset.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void goSignUp(View view){
        Intent signUpIntent=new Intent(LoginActivity.this,SignUp.class);
        startActivity(signUpIntent);
    }
    public void goReset(View view){
        Intent resetIntent=new Intent(LoginActivity.this,ResetActivity.class);
        startActivity(resetIntent);
    }

}
