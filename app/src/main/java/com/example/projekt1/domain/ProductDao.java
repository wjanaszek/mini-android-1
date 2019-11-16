package com.example.projekt1.domain;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM product")
    List<Product> getAll();

    @Query("SELECT * FROM product")
    Cursor getAllWithCursor();

    @Query("SELECT * FROM product WHERE id = :id")
    Product findById(Long id);

    @Insert
    void insertAll(Product... products);

    @Update
    void updateOne(Product product);

    @Delete
    void delete(Product product);
}
