package com.example.maledettatreest.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface PhotoDao {

    @Query("SELECT * FROM photo WHERE uidPversion LIKE :uidPversion LIMIT 1")
    Photo getUidPversion(String uidPversion);

    @Query("SELECT EXISTS (SELECT 1 FROM photo WHERE uidPversion = :uidPversion)")
    Boolean exists(String uidPversion);

    @Insert
    void insert(Photo photo);

    @Delete
    void delete(Photo photo);
}
