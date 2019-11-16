package com.example.projekt1.utils;

import androidx.room.TypeConverter;

import java.math.BigDecimal;

public class Converters {
    @TypeConverter
    public static BigDecimal fromLong(Long value) {
        return value == null ? null : new BigDecimal(value).divide(new BigDecimal(100));
    }

    @TypeConverter
    public static Long toLong(BigDecimal bigDecimal) {
        return bigDecimal == null
            ? null
            : bigDecimal.multiply(new BigDecimal(100)).longValue();
    }
}
