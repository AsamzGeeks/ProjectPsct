package com.asamz.psctroll;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import pl.droidsonroids.gif.GifTextView;

import static android.provider.ContactsContract.Intents.Insert.EMAIL;

public class SignUpActivity extends AppCompatActivity {
  /* private ImageView ivSignup;
   private  EditText etFirstName,etSecondName,etEmail,etPassword;
    private String firstName,secondName,emailId,passWord;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    GifTextView gfStatus;
    Animation animationFadeIn;
    Animation animationFadeOut;
    FirebaseUser firebaseUser = null;
    private FirebaseAuth mAuth;
    int myphase=0;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        etFirstName=(EditText)findViewById(R.id.etName);
        etSecondName=(EditText)findViewById(R.id.etUserName);
        etEmail=(EditText)findViewById(R.id.etEmail);
        etPassword=(EditText)findViewById(R.id.etPassword);
        ivSignup=(ImageView)findViewById(R.id.ivSignUp);
        firebaseAuth = FirebaseAuth.getInstance();
        gfStatus=(GifTextView)findViewById(R.id.gfSignUp);
        gfStatus.setVisibility(View.INVISIBLE);
        //setting signup image clicking effect
         animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
         animationFadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        ivSignup.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageView view = (ImageView) v;
                        //overlay is black with transparency of 0x77 (119)
                        view.getDrawable().setColorFilter(0x88112233, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        ImageView view = (ImageView) v;
                        //clear the overlay
                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }

                return false;
            }
        });

    }

    public void signUpProcedure(View view){
        if(isNetworkAvailable()==false){
            Toast.makeText(getApplicationContext(), "Connect to an active internet connection", Toast.LENGTH_SHORT).show();
        }
        else {

            firstName = etFirstName.getText().toString();
            secondName = etSecondName.getText().toString();
            emailId = etEmail.getText().toString();
            passWord = etPassword.getText().toString();
            if (isValidEmailAddress(emailId) == false) {
                etEmail.setHint(R.string.emailError);
            }
            if (passwordMin(passWord) == false) {
                etPassword.setHint(R.string.passwordError);
            }

            if (isValidEmailAddress(emailId) == true && passwordMin(passWord) == true) {
                ivSignup.startAnimation(animationFadeOut);
                ivSignup.setVisibility(View.INVISIBLE);
                if(isEmailAlreadyUsed(emailId)==true){
                    ivSignup.startAnimation(animationFadeIn);
                    ivSignup.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Email ID is already in use", Toast.LENGTH_SHORT).show();

                }
                else {

                    sendMail(emailId, "Hi", "Dear " + firstName + " " + secondName + ", You have successfully completed the registration.\n Your Password is " + passWord + " \n Please Login to app using the same EmailID and Password");
                }
            }

        }


        }

    private void sendMail(String email, String subject, String messageBody) {
        Session session = createSessionObject();

        try {
            Message message = createMessage(email, subject, messageBody, session);
            new SendMailTask().execute(message);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
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

    private Message createMessage(String email, String subject, String messageBody, Session session) throws
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
            gfStatus.setVisibility(View.VISIBLE);
            gfStatus.startAnimation(animationFadeIn);


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

                if(i==2){
                    gfStatus.startAnimation(animationFadeOut);
                    gfStatus.setVisibility(View.INVISIBLE);
                    ivSignup.startAnimation(animationFadeIn);
                    ivSignup.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Registration is Unsuccessfull,Do it Again", Toast.LENGTH_SHORT).show();

                }
                gfStatus.startAnimation(animationFadeOut);
                gfStatus.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Registration Successfull", Toast.LENGTH_SHORT).show();



        }

        protected Void doInBackground(javax.mail.Message... messages) {
            try {
                Transport.send(messages[0]);
                registerUserOnFireBase(emailId,passWord);


            } catch (MessagingException e) {

               e.printStackTrace();

            }

            return null;
        }
}
    private void registerUserOnFireBase(String email,String password){



        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            i=0;

                        }else{
                            //display some message here
                            i=2;


                        }

                    }
                });

    }

    private boolean isEmailAlreadyUsed(String EmailID) {
        mAuth = FirebaseAuth.getInstance();



        mAuth.createUserWithEmailAndPassword(EmailID, "dummypass")
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            /////user do not exit so good to initialize firebase user.
                            firebaseUser = task.getResult().getUser();

                        } else {
                            if(task.getException().getMessage().equals("The email address is already in use by another account.")) {
                               myphase=1;
                            }
                        }
                    }
                });
        if(myphase==0){
            return false;
        }
        else
            return true;
    }*/
}
