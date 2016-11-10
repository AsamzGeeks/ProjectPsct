package com.asamz.psctroll;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pkmmte.view.CircularImageView;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import pl.droidsonroids.gif.GifTextView;

public class RegisterActivity extends AppCompatActivity {

    ImageView ivProfileImage,ivSignup;
    TextView tvStatus;
    TextView roundedMethod;
    CircularImageView circularImageView;
    EditText etFirstName,etSecondName,etEmail,etPassword,etPasswordRepeat;
    SharedPreferences appPrefs;
    Uri uri;
    Uri destinationURI;
    Animation fadein;
    TextView tvImageStatus;
    LinearLayout llSignUp;
    GifTextView gifLoad;
    String FirstName,SecondName,Email,Password;
    String ErrorMessage="These are the Errors Found";
    int token=0;
    String namePreferance="name";
    String imagePreferance="image";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private  FirebaseDatabase database;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fadein= AnimationUtils.loadAnimation(this, R.anim.fadein);
        ivProfileImage = (ImageView) findViewById(R.id.ivLoginDefualt);
       // ivProfileImage.startAnimation(fadein);
        ivProfileImage.setVisibility(View.VISIBLE);

        circularImageView = (CircularImageView)findViewById(R.id.cvProfileLogin);
        tvImageStatus=(TextView)findViewById(R.id.tvImageStatus) ;
       // tvImageStatus.startAnimation(fadein);
        llSignUp=(LinearLayout)findViewById(R.id.rlSignup);
      //  llSignUp.setAnimation(fadein);
        etFirstName=(EditText)findViewById(R.id.ettFirstName) ;
        etSecondName=(EditText)findViewById(R.id.ettSecondName) ;
        etEmail=(EditText)findViewById(R.id.etEmail);
        etPassword=(EditText)findViewById(R.id.ettPassword) ;
        etPasswordRepeat=(EditText)findViewById(R.id.ettPasswordRepeat);
        gifLoad=(GifTextView)findViewById(R.id.gfSignUpBar);
        ivSignup=(ImageView)findViewById(R.id.ivSignupButtton);
        gifLoad.setVisibility(View.INVISIBLE);
        appPrefs  = PreferenceManager.getDefaultSharedPreferences(this);
        mAuth=FirebaseAuth.getInstance();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("USER DATA");





    }

    public void addImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1234);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1234 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            destinationURI=Uri.fromFile(new File(getCacheDir(), "cropped"));
            Crop.of(uri, destinationURI).asSquare().start(this);
            tvImageStatus.setVisibility(View.INVISIBLE);
            circularImageView.setVisibility(View.INVISIBLE);
        }
        if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {

            try {
                Crop.getOutput(data);
                Bitmap bitmap = MyMethods.loadBitmap(destinationURI.toString());
                SharedPreferences.Editor editor=appPrefs.edit();
                editor.putString("namePreferance", "profileImage");
                editor.putString("imagePreferance", MyMethods.encodeTobase64(bitmap));
                editor.apply();
                ivProfileImage.setVisibility(View.GONE);
                tvImageStatus.setVisibility(View.INVISIBLE);
                circularImageView.setImageBitmap(bitmap);
                circularImageView.setBorderColor(getResources().getColor(R.color.colorActionBar));
                circularImageView.setBorderWidth(10);
                circularImageView.setSelectorColor(getResources().getColor(R.color.colorWhite));
                circularImageView.setSelectorStrokeColor(getResources().getColor(R.color.colorPrimary));
                circularImageView.setSelectorStrokeWidth(10);
                circularImageView.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Error in picture conversion",Toast.LENGTH_SHORT).show();
            }
            tvImageStatus.setAnimation(fadein);
            tvImageStatus.setText("Your Image Looks Great!");


        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public  void SignUpProcedure(View view){
        FirstName = etFirstName.getText().toString().trim();
        SecondName = etSecondName.getText().toString().trim();
        Email = etEmail.getText().toString().trim();
        Password = etPassword.getText().toString().trim();
        if(isNetworkAvailable()==false){
            AlertDialog alertDialog= new AlertDialog.Builder(RegisterActivity.this).create();
            alertDialog.setTitle("Data Connection");
            alertDialog.setMessage("No Internet,Conncect To Internet");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        else {

            if (MyMethods.isValidEmailAddress(Email) == false||Email.isEmpty()) {

                ErrorMessage=ErrorMessage.concat("\n-Email ID is not valid");
                token=1;
            }
            if (MyMethods.passwordMin(Password) == false||Password.isEmpty()) {
                ErrorMessage=ErrorMessage.concat("\n-Password Should Have Minimum 6 Letters");
                token=1;
            }
            if(FirstName.isEmpty()){
                ErrorMessage=ErrorMessage.concat("\n-Please Enter Your First Name");
                token=1;
            }
            if(!Password.equals(etPasswordRepeat.getText().toString().trim())){
                ErrorMessage=ErrorMessage.concat("\n-Passwords are not Matching");

                token=1;
            }
            if(token==1){
                if(ErrorMessage.contains("-")){
                    AlertDialog alertDialogError = new AlertDialog.Builder(RegisterActivity.this).create();
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
                    gifLoad.setVisibility(View.INVISIBLE);

                }
                else {
                    token=0;
                }

            }
            if(token==0){
                gifLoad.setVisibility(View.VISIBLE);
                gifLoad.startAnimation(fadein);
                sendMail(Email, "Hi", "Dear " + FirstName + " " + SecondName + ", You have successfully completed the registration.\n Your Password is " + Password + " \n Please Login to app using the same EmailID and Password");
                Toast.makeText(getApplicationContext(), "Please Wait", Toast.LENGTH_SHORT).show();

            }

        }
    }
    private void sendMail(String email, String subject, String messageBody) {
        Session session = createSessionObject();

        try {
            Message message = createMessage(email, subject, messageBody, session);
            new RegisterActivity.SendMailTask().execute(message);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private  Message createMessage(String email, String subject, String messageBody, Session session) throws
            MessagingException, UnsupportedEncodingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("psctroll@gmail.com", "Registration Successfull"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email, email));
        message.setSubject(subject);
        message.setText(messageBody);
        return message;
    }

    public class SendMailTask extends AsyncTask<Message, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent login=new Intent(RegisterActivity.this,LoginActivity.class);
            startActivity(login);

        }

        protected Void doInBackground( javax.mail.Message... messages) {

            try {
                mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            String user_id=mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db=mDatabase.child(user_id);
                            current_user_db.child("name").setValue(FirstName);
                            current_user_db.child("secondname").setValue(SecondName);


                        }
                    }
                });
                try {
                    Transport.send(messages[0]);
                } catch (MessagingException e) {
                    Toast.makeText(getApplicationContext(), "Mail Sending Error", Toast.LENGTH_SHORT).show();

                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }


            return null;

        }
    }

    private Session createSessionObject() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        return Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("psctroll@gmail.com", "asamzgeekspsc");
            }
        });
    }
}




