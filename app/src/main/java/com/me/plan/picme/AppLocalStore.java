package com.me.plan.picme;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.me.plan.picme.Dao.PictureDao;
import com.me.plan.picme.Model.Picture;

/**
 * Created by Alon on 05/03/2018.
 */

@Database(entities = {Picture.class}, version = 1)
public abstract class AppLocalStore extends RoomDatabase {
    public abstract PictureDao pictureDao();

    public static AppLocalStore db = Room.databaseBuilder(MyApplication.getMyContext(),
            AppLocalStore.class,
            "database-name").build();
}