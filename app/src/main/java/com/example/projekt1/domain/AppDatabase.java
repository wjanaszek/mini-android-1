package com.example.projekt1.domain;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Product.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProductDao productDao();
}
