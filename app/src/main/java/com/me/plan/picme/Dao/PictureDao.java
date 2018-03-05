package com.me.plan.picme.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.me.plan.picme.Model.Picture;

import java.util.List;

/**
 * Created by Alon on 05/03/2018.
 */

@Dao
public interface PictureDao {
    @Query("SELECT * FROM pictures")
    List<Picture> getAll();

    @Query("SELECT * FROM pictures WHERE id IN (:pictureIds)")
    List<Picture> loadAllByIds(int[] pictureIds);

    @Query("SELECT * FROM pictures WHERE id = :id")
    Picture findById(String id);

    @Insert
    void insertAll(Picture... pictures);

    @Delete
    void delete(Picture pictures);

}