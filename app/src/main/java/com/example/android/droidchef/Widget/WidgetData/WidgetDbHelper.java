package com.example.android.droidchef.Widget.WidgetData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.droidchef.Widget.WidgetData.RecipeWidgetContract.RecipeEntry;

/**
 * Created by Astraeus on 1/22/2018.
 */

public class WidgetDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recipes.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + RecipeEntry.TABLE_NAME + " ("
            + RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + RecipeEntry.COLUMN_RECIPE_NAME + " TEXT NOT NULL, "
            + RecipeEntry.COLUMN_INGREDIENTS + " TEXT NOT NULL);";

    private static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + RecipeEntry.TABLE_NAME;

    public WidgetDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }
}
