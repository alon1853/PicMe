package com.me.plan.picme.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.me.plan.picme.Model.Picture;
import com.me.plan.picme.Repository.PicturesListRepository;

import java.util.List;

/**
 * Created by Alon on 05/03/2018.
 */

public class PicturesListViewModel extends ViewModel {
    private MutableLiveData<List<Picture>> pictures;
    private PicturesListRepository picturesListRepository;

    public PicturesListViewModel() {
        picturesListRepository = PicturesListRepository.instance;
        this.pictures = picturesListRepository.getPicturesList();
    }

    public MutableLiveData<List<Picture>> getPicturesList() {
        return pictures;
    }
}
