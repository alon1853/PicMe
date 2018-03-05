package com.me.plan.picme.Repository;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.me.plan.picme.Model.Picture;
import com.me.plan.picme.Model.PictureFirebase;

import java.util.List;

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
}
