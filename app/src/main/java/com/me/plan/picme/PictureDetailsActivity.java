package com.me.plan.picme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

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

        model.getPicture(picId, new ModelFirebase.GetPictureInterface() {
            @Override
            public void AfterSuccessfulGetPicture(Picture picture) {
                model.LoadImage(picture.url, new ModelFirebase.LoadImageInterface() {
                    @Override
                    public void afterSuccessfulImageLoad(byte[] bytes) {

                    }
                });
            }

            @Override
            public void AfterFailGetPicture() {

            }
        });
    }

}
