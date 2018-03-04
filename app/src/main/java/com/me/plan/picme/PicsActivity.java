package com.me.plan.picme;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.me.plan.picme.Model.Model;
import com.me.plan.picme.Model.ModelFirebase;
import com.me.plan.picme.Model.Picture;
import com.me.plan.picme.Model.User;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class PicsActivity extends AppCompatActivity {
    private Model model;
    PicturesListAdapter adapter;
    List<Picture> data = new LinkedList<Picture>();

    public PicsActivity() {
        model = Model.instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pics);

        User currentUser = model.getCurrentUser();
        if (currentUser == null) {
            // Finish activity if user is not logged in
            finish();
        }

        User user = new User();
        user.name = "Alon";
        user.email = "test@google.com";
        Picture p = new Picture();
        p.title = "title";
        p.publishDate = new Date();
        p.user = user;
        Picture p2 = new Picture();
        p2.title = "title2";
        p2.publishDate = new Date();
        p2.user = user;
        Picture p3 = new Picture();
        p3.title = "title3";
        p3.publishDate = new Date();
        p3.user = user;
        Picture p4 = new Picture();
        p4.title = "title4";
        p4.publishDate = new Date();
        p4.user = user;
        Picture p5 = new Picture();
        p5.title = "title5";
        p5.publishDate = new Date();
        p5.user = user;
        Picture p6 = new Picture();
        p6.title = "title6";
        p6.publishDate = new Date();
        p6.user = user;
        data.add(p);
        data.add(p2);
        data.add(p3);
        data.add(p4);
        data.add(p5);
        data.add(p6);

        ListView listView = findViewById(R.id.list_view);
        adapter = new PicturesListAdapter();
        listView.setAdapter(adapter);
    }

    class PicturesListAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public PicturesListAdapter() {
            inflater = (LayoutInflater) PicsActivity.this.getLayoutInflater();
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = inflater.inflate(R.layout.picture_row, null);
            }
            final Picture picture = data.get(i);

            TextView text = (TextView) view.findViewById(R.id.picture_title);
            TextView user = (TextView) view.findViewById(R.id.picture_user);
            TextView date = (TextView) view.findViewById(R.id.picture_date);
            text.setText(picture.title);
            user.setText(picture.user.name);
            date.setText(picture.publishDate.toString());

            final ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            model.LoadImage("s9.jpg", new ModelFirebase.LoadImageInterface() {
                @Override
                public void afterSuccessfulImageLoad(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    imageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, imageView.getWidth(),
                            imageView.getHeight(), false));
                }
            });

            return view;
        }
    }
}
