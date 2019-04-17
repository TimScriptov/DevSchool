package com.devschool.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;

public final class Bookmarks {
    public static Cursor getAllBookmarks() {
        return Database.getDatabase().rawQuery("SELECT * FROM Bookmarks", null);
    }

    public static boolean add(String url, String title) {
        ContentValues cv = new ContentValues();
        cv.put("Url", url);
        cv.put("Title", title);
        long result = Database.getDatabase().insert("Bookmarks", null, cv);
        return result != -1;
    }

    public static void remove(String url) {
        Database.getDatabase().execSQL("DELETE FROM Bookmarks WHERE Url='" + url + "'");
    }

    public static boolean isBookmarked(String url) {
        @SuppressLint("Recycle") Cursor cursor = Database.getDatabase().rawQuery("SELECT Url FROM Bookmarks WHERE Url = '" + url + "'", null);
        return cursor.getCount() != 0;
    }

}
