package com.example.projekt1.domain;

import android.content.ContentValues;

import java.math.BigDecimal;

public class ProductUtil {
    public static Product fromContentValues(ContentValues values) {
        final Product product = new Product();
        if (values.containsKey("name")) product.name = values.getAsString("name");
        if (values.containsKey("price")) product.price = new BigDecimal(values.getAsDouble("price"));
        if (values.containsKey("quanity")) product.quanity = values.getAsInteger("quanity");
        if (values.containsKey("isBought")) product.isBought = values.getAsBoolean("isBought");
        return product;
    }
}
