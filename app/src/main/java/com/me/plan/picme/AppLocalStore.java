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
abstract class AppLocalStoreDb extends RoomDatabase {
    public abstract PictureDao pictureDao();
}

public class AppLocalStore {
    public static AppLocalStoreDb db = Room.databaseBuilder(MyApplication.getMyContext(),
            AppLocalStoreDb.class,
            "database-name").build();
}