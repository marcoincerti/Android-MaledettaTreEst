package com.example.maledettatreest.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Photo {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "uidPversion")
    public String uidPversion;

    @ColumnInfo(name = "photoString")
    public String photoString;

    public Photo(@NonNull String uidPversion, @NonNull String photoString) {
        this.uidPversion = uidPversion;
        this.photoString = photoString;
    }

}

