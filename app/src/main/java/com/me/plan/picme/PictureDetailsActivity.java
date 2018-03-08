package com.me.plan.picme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.me.plan.picme.Model.Model;
import com.me.plan.picme.Model.ModelFirebase;
import com.me.plan.picme.Model.Picture;

public class PictureDetailsActivity extends AppCompatActivity {
    private Picture picture;
    Model model = Model.instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_details);

        Intent intent = getIntent();
        String picId = intent.getStringExtra("PICID");

        final TextView pictureTitleText = (TextView) findViewById(R.id.pictureTitleText);
        final TextView pictureDateText = (TextView) findViewById(R.id.pictureDateText);
        final TextView pictureUserText = (TextView) findViewById(R.id.pictureUserText);
        final ImageView imageView = (ImageView) findViewById(R.id.pictureImageView);

        model.getPicture(picId, new ModelFirebase.GetPictureInterface() {
            @Override
            public void AfterSuccessfulGetPicture(Picture picture) {
                pictureTitleText.setText(picture.getTitle());
                pictureDateText.setText(picture.getDate());
                pictureUserText.setText(picture.getUser());
                model.LoadImage(picture.url, new ModelFirebase.LoadImageInterface() {
                    @Override
                    public void afterSuccessfulImageLoad(final byte[] bytes) {
                        ProgressBar progressBar = (ProgressBar) findViewById(R.id.pictureProgressBar);
                        progressBar.setVisibility(View.INVISIBLE);
                        imageView.post(new Runnable() {
                            @Override
                            public void run() {
                                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                imageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, imageView.getWidth(),
                                        imageView.getHeight(), false));
                            }
                        });
                    }
                });
            }

            @Override
            public void AfterFailGetPicture() {

            }
        });
    }

}
