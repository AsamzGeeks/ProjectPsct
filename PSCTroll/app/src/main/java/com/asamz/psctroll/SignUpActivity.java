package com.asamz.psctroll;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

import static android.provider.ContactsContract.Intents.Insert.EMAIL;

public class SignUpActivity extends AppCompatActivity {
   private ImageView ivSignup;
   private  EditText etFirstName,etSecondName,etEmail,etPassword;
    private String firstName,secondName,emailId,passWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        etFirstName=(EditText)findViewById(R.id.etName);
        etSecondName=(EditText)findViewById(R.id.etUserName);
        etEmail=(EditText)findViewById(R.id.etEmail);
        etPassword=(EditText)findViewById(R.id.etPassword);
        ivSignup=(ImageView)findViewById(R.id.ivSignUp);
        //setting signup image clicking effect
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
    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(email);
        if ((m.matches()&&email.endsWith(".com"))||(m.matches()&&email.endsWith(".au"))||(m.matches()&&email.endsWith(".edu"))||(m.matches()&&email.endsWith(".in"))){
            return true;
        }
        else
            return false;
    }
    public boolean passwordMin(String password){
        if(password.length()>6){
            return true;
        }
        else
            return false;
    }
    public void signUpProcedure(View view){
        if(etFirstName.getText().toString()==null||etSecondName.getText().toString()==null||etEmail.getText().toString()==null||etPassword.getText().toString()==null){
            Toast.makeText(getApplicationContext(), "Please Fill Appropirate Fields", Toast.LENGTH_SHORT).show();

        }
        firstName=etFirstName.getText().toString();
        secondName=etSecondName.getText().toString();
        emailId=etEmail.getText().toString();
        passWord=etPassword.getText().toString();
        if(isValidEmailAddress(emailId)==false){
            etEmail.setText("Please Enter a valid email ID");
        }
        if(passwordMin(passWord)==false){
            Toast.makeText(getApplicationContext(), "Password should have atleast 6 characters", Toast.LENGTH_SHORT).show();
        }
        if(isValidEmailAddress(emailId)==true&&passwordMin(passWord)==true){
            sendMail(emailId, "Hi", "Dear " + firstName+" "+secondName+ ", You have successfully completed the registration.\n Your Password is "+passWord+" \n Please Login to app using the same EmailID and Password");
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
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(SignUpActivity.this, "Please wait", "Sending mail", true, false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }

        protected Void doInBackground(javax.mail.Message... messages) {
            try {
                Transport.send(messages[0]);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        }
}
}
