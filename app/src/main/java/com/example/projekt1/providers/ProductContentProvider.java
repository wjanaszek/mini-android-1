package com.example.projekt1.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Room;

import com.example.projekt1.R;
import com.example.projekt1.domain.AppDatabase;
import com.example.projekt1.domain.Product;
import com.example.projekt1.domain.ProductUtil;

public class ProductContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.example.projekt1.providers";
    public static final String TABLE_NAME = Product.class.getSimpleName();
    public static final Uri URI_ITEM = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(
        @NonNull Uri uri,
        @Nullable String[] projection,
        @Nullable String selection,
        @Nullable String[] selectionArgs,
        @Nullable String sortOrder
    ) {
        if (getContext() != null) {
            AppDatabase db = Room.databaseBuilder(
                getContext(),
                AppDatabase.class,
                getContext().getString(R.string.database_name)
            )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()   // @TODO should be removed
                .build();
            final Cursor cursor = db.productDao().getAllWithCursor();
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }

        throw new IllegalArgumentException("Failed to query row for uri " + uri);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return "vnd.android.cursor.product/" + AUTHORITY + "." + TABLE_NAME;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if (getContext() != null) {
            AppDatabase db = Room.databaseBuilder(
                getContext(),
                AppDatabase.class,
                getContext().getString(R.string.database_name)
            )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()   // @TODO should be removed
                .build();
            db.productDao().insertAll(ProductUtil.fromContentValues(values));
            getContext().getContentResolver().notifyChange(uri, null);
            return ContentUris.withAppendedId(uri, values.getAsLong("id"));
        }

        throw new IllegalArgumentException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(
        @NonNull Uri uri,
        @Nullable String selection,
        @Nullable String[] selectionArgs
    ) {
        if (getContext() != null) {
            AppDatabase db = Room.databaseBuilder(
                getContext(),
                AppDatabase.class,
                getContext().getString(R.string.database_name)
            )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()   // @TODO should be removed
                .build();
            final Product product = db.productDao().findById(ContentUris.parseId(uri));
            db.productDao().delete(product);
            return 1;
        }

        throw new IllegalArgumentException("Failed to delete row with " + uri);
    }

    @Override
    public int update(
        @NonNull Uri uri,
        @Nullable ContentValues values,
        @Nullable String selection,
        @Nullable String[] selectionArgs
    ) {
        if (getContext() != null) {
            AppDatabase db = Room.databaseBuilder(
                getContext(),
                AppDatabase.class,
                getContext().getString(R.string.database_name)
            )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()   // @TODO should be removed
                .build();
            db.productDao().updateOne(ProductUtil.fromContentValues(values));
            return 1;
        }

        throw new IllegalArgumentException("Failed to update row with " + uri);
    }
}
