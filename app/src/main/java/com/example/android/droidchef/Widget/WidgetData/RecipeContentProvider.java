package com.example.android.droidchef.Widget.WidgetData;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Astraeus on 1/22/2018.
 */

public class RecipeContentProvider extends ContentProvider {

    private WidgetDbHelper mDbHelper;


    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDbHelper = new WidgetDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                RecipeWidgetContract.RecipeEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long id = db.insert(RecipeWidgetContract.RecipeEntry.TABLE_NAME, null, contentValues);
        Uri returnUri = ContentUris.withAppendedId(RecipeWidgetContract.RecipeEntry.CONTENT_URI, id);

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
