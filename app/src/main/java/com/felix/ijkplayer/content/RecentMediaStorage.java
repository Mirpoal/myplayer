package com.felix.ijkplayer.content;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

public class RecentMediaStorage {
    private Context mAppContext;

    public RecentMediaStorage(Context context) {
        mAppContext = context.getApplicationContext();
    }

    public void saveUrlAsync(String url) {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... params) {
                saveUrl(params[0]);
                return null;
            }
        }.execute(url);
    }

    public void saveUrl(String url) {
        ContentValues cv = new ContentValues();
        cv.putNull(Entry.COLUMN_NAME_ID);
        cv.put(Entry.COLUMN_NAME_URL, url);
        cv.put(Entry.COLUMN_NAME_LAST_ACCESS, System.currentTimeMillis());
        cv.put(Entry.COLUMN_NAME_NAME, getNameOfUrl(url));
        save(cv);
    }

    public static final String ALL_COLUMNS[] = new String[] {
            Entry.COLUMN_NAME_ID + " as _id",
            Entry.COLUMN_NAME_ID,
            Entry.COLUMN_NAME_URL,
            Entry.COLUMN_NAME_NAME,
            Entry.COLUMN_NAME_LAST_ACCESS
    };

    public static class Entry {
        public static final String TABLE_NAME = "RecentMedia";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_LAST_ACCESS = "last_access";
    }

    public static String getNameOfUrl(String url, String defaultName) {
        String name = null;
        int pos = url.lastIndexOf('/');
        if (pos >= 0) {
            name = url.substring(pos + 1);
        }
        if (TextUtils.isEmpty(name)) {
            name = defaultName;
        }
        return name;
    }

    public static String getNameOfUrl(String url) {
        return getNameOfUrl(url, "");
    }

    public static class OpenHelper extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "RecentMedia.db";
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE IF NOT EXISTS " + Entry.TABLE_NAME +
                " (" +
                        Entry.COLUMN_NAME_ID + "INTERGE PRIMARY KEY AUTOINCREMENT," +
                        Entry.COLUMN_NAME_URL + "VARCHAR UNIQUE," +
                        Entry.COLUMN_NAME_NAME + "VARCHAR," +
                        Entry.COLUMN_NAME_LAST_ACCESS + "INTERGE" +
                ")";

        public OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }

    public void save(ContentValues contentValues) {
        OpenHelper openHelper = new OpenHelper(mAppContext);
        SQLiteDatabase db = openHelper.getWritableDatabase();
        db.replace(Entry.TABLE_NAME, null, contentValues);
    }

    public static class CursorLoader extends AsyncTaskLoader {

        public CursorLoader(@NonNull Context context) {
            super(context);
        }

        @Nullable
        @Override
        public Object loadInBackground() {
            Context context = getContext();
            OpenHelper openHelper = new OpenHelper(context);
            SQLiteDatabase db = openHelper.getReadableDatabase();

            return db.query(Entry.TABLE_NAME, ALL_COLUMNS, null, null, null, null,
                    Entry.COLUMN_NAME_LAST_ACCESS + " DESC", "100");
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }
    }
}
