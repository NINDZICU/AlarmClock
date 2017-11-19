package com.kfpu.alarmclock.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kfpu.alarmclock.data.AlarmOpenHelper;

import com.kfpu.alarmclock.tables.AlarmContract;

/**
 * Created by hlopu on 07.11.2017.
 */

public class AlarmProvider extends ContentProvider {
    private AlarmOpenHelper alarmOpenHelper;

    public static final int ALARMS_URI_KEY = 101;

    public static final String BASE_CONTENT_AUTHORITY = "com.kpfu.alarmclock";

    private final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static Uri baseUri;

    @Override
    public boolean onCreate() {
        if (getContext() != null) {
            alarmOpenHelper = new AlarmOpenHelper(getContext());
            baseUri = Uri.parse("content://" + BASE_CONTENT_AUTHORITY);
            uriMatcher.addURI(BASE_CONTENT_AUTHORITY, AlarmContract.TABLE_NAME, ALARMS_URI_KEY);
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String tableName = getType(uri);
        Cursor mCursor = alarmOpenHelper.getReadableDatabase().query(tableName, projection, selection, selectionArgs, null, null, null);
        return mCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int matchCode = uriMatcher.match(uri);
        switch (matchCode) {
            case ALARMS_URI_KEY:
                return AlarmContract.TABLE_NAME;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        String tableName = getType(uri);
        long id = alarmOpenHelper.getWritableDatabase().insert(tableName, null, values);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        String tableName = getType(uri);
        int count = alarmOpenHelper.getWritableDatabase().delete(tableName, selection, selectionArgs);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        String tableName = getType(uri);
        return alarmOpenHelper.getWritableDatabase().update(tableName, values, selection, selectionArgs);
    }
}
