package com.example.maledettatreest.models;

import android.app.Application;

public class PhotoRepository {

    private PhotoDao photoDao;


    public PhotoRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        photoDao = db.photoDao();
    }

    public String getPhotoDB(String uidPversion){
        Photo foto = photoDao.getUidPversion(uidPversion);
        return foto.photoString;
    }

    void insertPhotoDB(Photo foto) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            photoDao.insert(foto);
        });
    }

    public Boolean checkPhotoDB(String uidPversion) {
        return photoDao.exists(uidPversion);
    }
}