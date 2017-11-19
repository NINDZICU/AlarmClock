package com.kfpu.alarmclock.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kfpu.alarmclock.tables.AlarmContract;

/**
 * Created by hlopu on 07.11.2017.
 */

public class AlarmOpenHelper extends SQLiteOpenHelper {
    public static final String ALARM_DB_NAME ="alarm.db";
    public static final int CURRENT_VERSION = 1;

    public AlarmOpenHelper(Context context) {
        super(context, ALARM_DB_NAME, null, CURRENT_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        new AlarmContract().createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
