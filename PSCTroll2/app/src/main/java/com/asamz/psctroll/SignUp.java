package com.asamz.psctroll;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    private TextView tvSignIn;
    private EditText etFirstName, etSecondName, etEmail, etPassword, etRepeat;
    private ImageView ivSignUp;
    int token = 0;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    String FirstName, SecondName, Email, Password;
    String ErrorMessage = "Errors Found";
    private ImageView ivSignUpButton;
    private boolean regStatus=false;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        tvSignIn = (TextView) findViewById(R.id.tvSignIn);
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etSecondName = (EditText) findViewById(R.id.etSecondName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etRepeat = (EditText) findViewById(R.id.etRepeat);
        ivSignUpButton = (ImageView) findViewById(R.id.ivSignUp);
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("users");
        mProgress=new ProgressDialog(this);

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void SignUpProcedure(View view) {
        FirstName = etFirstName.getText().toString().trim();
        SecondName = etSecondName.getText().toString().trim();
        Email = etEmail.getText().toString().trim();
        Password = etPassword.getText().toString().trim();
        if (isNetworkAvailable() == false) {
            AlertDialog alertDialog = new AlertDialog.Builder(SignUp.this).create();
            alertDialog.setTitle("Data Connection");
            alertDialog.setMessage("No Internet,Conncect To Internet");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        } else {

            if (MyMethods.isValidEmailAddress(Email) == false || Email.isEmpty()) {

                ErrorMessage = ErrorMessage.concat("\n-Email ID is not valid");
                token = 1;
            }
            if (MyMethods.passwordMin(Password) == false || Password.isEmpty()) {
                ErrorMessage = ErrorMessage.concat("\n-Password Should Have Minimum 6 Letters");
                token = 1;
            }
            if (FirstName.isEmpty()) {
                ErrorMessage = ErrorMessage.concat("\n-Please Enter Your First Name");
                token = 1;
            }
            if (!Password.equals(etRepeat.getText().toString().trim())) {
                ErrorMessage = ErrorMessage.concat("\n-Passwords are not Matching");

                token = 1;
            }
            if (token == 1) {
                if (ErrorMessage.contains("-")) {
                    AlertDialog alertDialogError = new AlertDialog.Builder(SignUp.this).create();
                    alertDialogError.setTitle("Errors Found");
                    alertDialogError.setMessage(ErrorMessage.toString());
                    alertDialogError.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    ErrorMessage = "These are the Errors Found";
                                }
                            });
                    alertDialogError.show();

                } else {
                    token = 0;
                }

            }
            if (token == 0) {
                mProgress.setMessage("Please Wait");
                mProgress.setCancelable(false);
                mProgress.show();
                new startFirebaseRegistration().execute();



            }

        }
    }

    private class startFirebaseRegistration extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPostExecute(Void aVoid) {

        }

        @Override
        protected Void doInBackground(Void... params) {

            mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user_db = mDatabase.child(user_id);
                        current_user_db.child("name").setValue(FirstName);
                        current_user_db.child("secondname").setValue(SecondName);
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        user.sendEmailVerification();
                                            mProgress.dismiss();
                                            Intent loginNew = new Intent(SignUp.this, LoginActivity.class);
                                            loginNew.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(loginNew);
                                            Toast.makeText(getApplicationContext(), "Activation Mail Sent", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            regStatus=false;
                        Toast.makeText(getApplicationContext(), "Error,Please Try Again", Toast.LENGTH_SHORT).show();
                                        }



                }
            });
            return null;
        }
    }


  public void gotoLogin(View view) {
      Intent login = new Intent(SignUp.this, LoginActivity.class);
      startActivity(login);
  }




}
