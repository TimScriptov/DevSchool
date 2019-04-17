package com.devschool.data;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.devschool.App;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class Database extends SQLiteOpenHelper {

    private static SQLiteDatabase database = new Database().getWritableDatabase();

    private Database() {
        super(App.getContext(), "devschool.db", null, 1);
    }

    @Contract(pure = true)
    public static SQLiteDatabase getDatabase() {
        return database;
    }

    @Override
    public void onCreate(@NotNull SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ReadItems (Url TEXT PRIMARY KEY, Title TEXT)");
        db.execSQL("CREATE TABLE Bookmarks (Url TEXT PRIMARY KEY, Title TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
