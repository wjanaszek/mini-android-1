package com.example.projekt1.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.projekt1.utils.Converters;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

@Entity
public class Product {
    @PrimaryKey(autoGenerate = true)
    @NotNull
    public Long id;

    @ColumnInfo
    public String name;

    @ColumnInfo(name = "bought")
    public Boolean isBought;

    @TypeConverters(Converters.class)
    @ColumnInfo
    public BigDecimal price;

    @ColumnInfo
    public Integer quanity;
}
