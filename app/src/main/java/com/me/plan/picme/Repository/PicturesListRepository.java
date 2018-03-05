package com.me.plan.picme.Repository;

import android.arch.lifecycle.MutableLiveData;
import android.content.SharedPreferences;
import android.util.Log;

import com.me.plan.picme.AppLocalStore;
import com.me.plan.picme.Model.Picture;
import com.me.plan.picme.Model.PictureFirebase;
import com.me.plan.picme.MyApplication;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Alon on 05/03/2018.
 */

public class PicturesListRepository {
    public static final PicturesListRepository instance = new PicturesListRepository();

    MutableLiveData<List<Picture>> picturesListliveData;

    PicturesListRepository() {
    }

    public MutableLiveData<List<Picture>> getPicturesList() {
        synchronized (this) {
            if (picturesListliveData == null) {
                picturesListliveData = new MutableLiveData<List<Picture>>();
                PictureFirebase.getAllPicturesAndObserve(new PictureFirebase.Callback<List<Picture>>() {

                    @Override
                    public void onComplete(List<Picture> data) {
                        if (data != null) picturesListliveData.setValue(data);
                        Log.d("TAG", "Got pictures data");
                    }
                });
            }
        }
        return picturesListliveData;
    }

    public MutableLiveData<List<Picture>> getAllPictures() {
        synchronized (this) {
            if (picturesListliveData == null) {
                picturesListliveData = new MutableLiveData<List<Picture>>();

                // Get the last update date
                final float lastUpdateDate = MyApplication.getMyContext()
                        .getSharedPreferences("TAG", MODE_PRIVATE).getFloat("lastUpdateDate", 0);

                // Get all pictures records that where updated since last update date
                PictureFirebase.getAllPicturesAndObserve(new PictureFirebase.Callback<List<Picture>>() {
                    @Override
                    public void onComplete(List<Picture> data) {
                        if (data != null && data.size() > 0) {
                            // Update the local DB
                            float reacentUpdate = lastUpdateDate;
                            for (Picture picture : data) {
                                AppLocalStore.db.pictureDao().insertAll(picture);
                                if (picture.lastUpdated > reacentUpdate) {
                                    reacentUpdate = picture.lastUpdated;
                                }
                                Log.d("TAG", "Updating: " + picture.toString());
                            }
                            SharedPreferences.Editor editor = MyApplication.getMyContext().getSharedPreferences("TAG", MODE_PRIVATE).edit();
                            editor.putFloat("lastUpdateDate", reacentUpdate);
                        }
                        //return the complete student list to the caller
                        List<Picture> empList = AppLocalStore.db.pictureDao().getAll();
                        picturesListliveData.setValue(empList);

                    }
                });
            }
        }
        return picturesListliveData;
    }
}
