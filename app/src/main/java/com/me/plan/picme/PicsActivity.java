package com.me.plan.picme;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.me.plan.picme.Model.Model;
import com.me.plan.picme.Model.ModelFirebase;
import com.me.plan.picme.Model.Picture;
import com.me.plan.picme.Model.User;
import com.me.plan.picme.ViewModel.PicturesListViewModel;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class PicsActivity extends AppCompatActivity {
    private Model model;
    PicturesListAdapter adapter;
    private PicturesListViewModel picturesListViewModel;
    private List<Picture> picturesList;

    public PicsActivity() {
        model = Model.instance;
        picturesList = new LinkedList<>();
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

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        picturesListViewModel = ViewModelProviders.of(this).get(PicturesListViewModel.class);

        final Observer<List<Picture>> picturesListObserver = new Observer<List<Picture>>() {
            @Override
            public void onChanged(@Nullable List<Picture> pictures) {
                if (progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.INVISIBLE);
                }

                picturesList = pictures;
                if (adapter != null) adapter.notifyDataSetChanged();
            }
        };

        picturesListViewModel.getPicturesList().observe(this, picturesListObserver);

        ListView listView = findViewById(R.id.list_view);
        adapter = new PicturesListAdapter();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TAG", "Row item was clicked at position: " + position);
                Intent intent = new Intent(PicsActivity.this, PictureDetailsActivity.class);
                intent.putExtra("PICID", picturesList.get(position).id);
                Log.d("TAG","Picture id selected = " + picturesList.get(position).id);
                startActivity(intent);
            }
        });

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fabUploadPicture);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PicsActivity.this, UploadPictureActivity.class);
                startActivity(intent);
            }
        });
    }

    class PicturesListAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public PicturesListAdapter() {
            inflater = (LayoutInflater) PicsActivity.this.getLayoutInflater();
        }

        @Override
        public int getCount() {
            return picturesList.size();
        }

        @Override
        public Object getItem(int i) {
            return picturesList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final PictureViewHolder mHolder;

            if (view == null) {
                view = inflater.inflate(R.layout.picture_row, null);
                mHolder = new PictureViewHolder();
                mHolder.title = (TextView) view.findViewById(R.id.picture_title);
                mHolder.user = (TextView) view.findViewById(R.id.picture_user);
                mHolder.date = (TextView) view.findViewById(R.id.picture_date);
                mHolder.image = (ImageView) view.findViewById(R.id.imageView);

                view.setTag(mHolder);
            } else {
                mHolder = (PictureViewHolder) view.getTag();
            }

            final Picture picture = picturesList.get(i);

            mHolder.title.setText(picture.title);
            mHolder.user.setText(picture.user);
            mHolder.date.setText(picture.date);

            model.LoadImage(picture.url, new ModelFirebase.LoadImageInterface() {
                @Override
                public void afterSuccessfulImageLoad(final byte[] bytes) {
                    mHolder.image.post(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            mHolder.image.setImageBitmap(Bitmap.createScaledBitmap(bmp, mHolder.image.getWidth(),
                                    mHolder.image.getHeight(), false));
                        }
                    });
                }
            });

            return view;
        }
    }

    protected static class PictureViewHolder {
        TextView title;
        TextView date;
        TextView user;
        ImageView image;
    }
}
