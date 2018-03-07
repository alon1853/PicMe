package com.me.plan.picme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.me.plan.picme.Model.Model;
import com.me.plan.picme.Model.ModelFirebase;
import com.me.plan.picme.Model.Picture;
import com.me.plan.picme.Model.PictureFirebase;
import com.me.plan.picme.Model.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UploadPictureActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    final static int RESAULT_SUCCESS = 0;
    Bitmap currentImageBitmap;
    Model model = Model.instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_picture);

        User currentUser = model.getCurrentUser();
        if (currentUser == null) {
            // Finish activity if user is not logged in
            finish();
        }

        final EditText inputTitle = (EditText) findViewById(R.id.title_text);
        final Button uploadButton = (Button) findViewById(R.id.upload_button);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckForm(inputTitle);
            }
        });

        Button capturePicture = (Button) findViewById(R.id.caoture_button);
        capturePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
    }

    private void CheckForm(EditText inputTitle) {
        if (inputTitle.getText().toString().equals("")) {
            Toast toast = Toast.makeText(this, R.string.empty_picture_title, Toast.LENGTH_SHORT);
            toast.show();
        } else if (currentImageBitmap == null) {
            Toast toast = Toast.makeText(this, R.string.select_picture, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            UploadCurrentPicture(inputTitle);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            currentImageBitmap = (Bitmap) extras.get("data");
            ImageView mImageView = (ImageView) findViewById(R.id.thumbnail);
            mImageView.setImageBitmap(currentImageBitmap);
        }
    }

    private void UploadCurrentPicture(EditText inputTitle) {
        Picture picture = new Picture();
        picture.setUser(model.getCurrentUser().name);
        picture.setTitle(inputTitle.getText().toString());
        Date date = Calendar.getInstance().getTime();
        picture.setDate(new SimpleDateFormat("dd/MM/yyyy").format(date));
        picture.setUrl(picture.getTitle().trim() + picture.getDate()+".jpg");

        PictureFirebase.addPicture(picture);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        final Button uploadButton = (Button) findViewById(R.id.upload_button);
        uploadButton.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        model.UploadImage(currentImageBitmap, picture.getUrl(), new ModelFirebase.UploadImageInterface() {
            @Override
            public void AfterSuccessfulUploadImage() {
                Toast toast = Toast.makeText(UploadPictureActivity.this, R.string.picture_uploaded_successfully, Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }

            @Override
            public void AfterFailedUploadImage() {
                Toast toast = Toast.makeText(UploadPictureActivity.this, R.string.failed_to_upload_picture, Toast.LENGTH_SHORT);
                toast.show();

                final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
                final Button uploadButton = (Button) findViewById(R.id.upload_button);
                uploadButton.setEnabled(true);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
