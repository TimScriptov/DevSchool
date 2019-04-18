package com.devschool.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;

import com.devschool.data.Database;

public class ItemUtils {
    public static boolean isRead(String url) {
        @SuppressLint("Recycle") Cursor cursor = Database.getDatabase().rawQuery("SELECT Url FROM ReadItems WHERE Url = '" + url + "'", null);
        return cursor.getCount() != 0;
    }

    public static boolean markRead(String url, String title) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Url", url);
        contentValues.put("Title", title);
        return Database.getDatabase().insert("ReadItems", null, contentValues) != -1;
    }
}
