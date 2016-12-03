package com.asamzgeeks.psc.adminconsole;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ImageUploader extends AppCompatActivity {
    EditText etImageURL,etMetadata;
    Button btnChoose,btnSubmit;
    String URL;
    Uri uri=null;
    StorageReference mStorage;
    ProgressDialog mProgress;
    RelativeLayout relative;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_uploader);
        etImageURL=(EditText)findViewById(R.id.etImagerUrl);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Blog");
        etMetadata=(EditText)findViewById(R.id.etDetails);
        btnChoose=(Button)findViewById(R.id.btnSelect);
        btnSubmit=(Button)findViewById(R.id.btnSubmit);
        mStorage= FirebaseStorage.getInstance().getReference();
        mProgress=new ProgressDialog(this);
        relative=(RelativeLayout)findViewById(R.id.activity_image_uploader);
    }
    public  void ImageSelector(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            etImageURL.setText(uri.toString().trim());
        }
    }
    public void uploadImage(View view){
        if(isNetworkAvailable()) {
            mProgress.setMessage("Uploading,Please Wait");
            mProgress.show();
            mProgress.setCancelable(false);
            startPosting();
        }
        else{
            Snackbar snackbar = Snackbar.make(relative, "You are offline,Please Connect To Internet", Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    }

    private void startPosting() {
        final String Details=etMetadata.getText().toString().trim();
        if(etImageURL.getText().toString().trim()!=null&&etMetadata!=null){

          final   StorageReference filePath=mStorage.child("Images").child(uri.getLastPathSegment());
            final StorageMetadata Imagemetadata=new StorageMetadata.Builder().setContentType("image/jpg").setCustomMetadata("imageData",etMetadata.getText().toString().trim()).build();

            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri download_Uri=taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost=mDatabase.push();
                    newPost.child("details").setValue(Details);
                    newPost.child("image").setValue(download_Uri.toString());
                    filePath.updateMetadata(Imagemetadata).addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                        @Override
                        public void onSuccess(StorageMetadata storageMetadata) {
                            Snackbar snackbar = Snackbar.make(relative, "Metadata Updated", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    });

                    mProgress.dismiss();
                }
            });


        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
