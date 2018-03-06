package com.me.plan.picme.Repository;

import android.arch.lifecycle.MutableLiveData;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

    public MutableLiveData<List<Picture>> getAllPictures() {
        synchronized (this) {
            if (picturesListliveData == null) {
                picturesListliveData = new MutableLiveData<List<Picture>>();

                // 1. Get the last update date
                long lastUpdateDate = 0;
                try {
                    lastUpdateDate = MyApplication.getMyContext()
                            .getSharedPreferences("TAG", MODE_PRIVATE).getLong("lastUpdateDate", 0);
                }catch (Exception e){
                    Log.d("TAG", e.getMessage());
                }

                // 2. Get all pictures records that where updated since last update date
                PictureFirebase.getAllPicturesAndObserve(lastUpdateDate, new PictureFirebase.Callback<List<Picture>>() {
                    @Override
                    public void onComplete(List<Picture> data) {
                        updatePicturesDataInLocalStorage(data);
                    }
                });
            }
        }
        return picturesListliveData;
    }

    private void updatePicturesDataInLocalStorage(List<Picture> data) {
        Log.d("TAG", "Got items from firebase: " + data.size());
        MyTask task = new MyTask();
        task.execute(data);
    }

    class MyTask extends AsyncTask<List<Picture>, String, List<Picture>>{
        @Override
        protected List<Picture> doInBackground(List<Picture>[] lists) {
            Log.d("TAG","Starting updatePicturesDataInLocalStorage in thread");
            if (lists.length > 0) {
                List<Picture> data = lists[0];
                long lastUpdateDate = 0;
                try {
                    lastUpdateDate = MyApplication.getMyContext()
                            .getSharedPreferences("TAG", MODE_PRIVATE).getLong("lastUpdateDate", 0);
                } catch (Exception e){
                    Log.d("TAG", e.getMessage());
                }
                if (data != null && data.size() > 0) {
                    //3. update the local DB
                    long recentUpdate = lastUpdateDate;
                    for (Picture picture : data) {
                        Log.d("TAG", "Alonnn size = " + data.size());
                        Log.d("TAG", "Alonnn " + picture.id);
                        AppLocalStore.db.pictureDao().insertAll(picture);
                        if (picture.lastUpdated > recentUpdate) {
                            recentUpdate = picture.lastUpdated;
                        }
                        Log.d("TAG", "Updating: " + picture.toString());
                    }
                    SharedPreferences.Editor editor = MyApplication.getMyContext().getSharedPreferences("TAG", MODE_PRIVATE).edit();
                    editor.putLong("lastUpdateDate", recentUpdate);
                    editor.commit();
                }
                //return the complete student list to the caller
                List<Picture> empList = AppLocalStore.db.pictureDao().getAll();
                Log.d("TAG","Finish updatePicturesDataInLocalStorage in thread");

                return empList;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Picture> pictures) {
            super.onPostExecute(pictures);
            picturesListliveData.setValue(pictures);
            Log.d("TAG","Update updatePicturesDataInLocalStorage in main thread");
            Log.d("TAG", "Got items from local db: " + pictures.size());

        }
    }
}
