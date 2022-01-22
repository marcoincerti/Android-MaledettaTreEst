package com.example.maledettatreest.models;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class PhotoModel {
    private static PhotoModel instance;
    private PhotoRepository mRepository;


    public PhotoModel(@NonNull Application application) {
        mRepository = new PhotoRepository(application);
    }
    public static synchronized PhotoModel getInstance(Application application) {
        if (instance == null) {
            instance = new PhotoModel(application);
        }
        return instance;
    }

    public void insertPhotoinDB(String uid, String foto) {
        Photo photo = new Photo(uid,foto);
        mRepository.insertPhotoDB(photo);
    }

    public String getPhototoDB(String uid) {
        return mRepository.getPhotoDB(uid);
    }

    public Boolean checkPhotoinDB(String uid) {
        return mRepository.checkPhotoDB(uid);
    }
}