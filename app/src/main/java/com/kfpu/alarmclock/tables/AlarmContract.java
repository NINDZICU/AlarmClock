package com.kfpu.alarmclock.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kfpu.alarmclock.models.Alarm;
import com.kfpu.alarmclock.providers.AlarmProvider;

import static com.kfpu.alarmclock.tables.AlarmContract.AlarmEntry.COLUMN_ALARM_DATE;
import static com.kfpu.alarmclock.tables.AlarmContract.AlarmEntry.COLUMN_ALARM_HOURS;
import static com.kfpu.alarmclock.tables.AlarmContract.AlarmEntry.COLUMN_ALARM_MINUTES;
import static com.kfpu.alarmclock.tables.AlarmContract.AlarmEntry.COLUMN_ALARM_STATE;
import static com.kfpu.alarmclock.tables.AlarmContract.AlarmEntry._ID;

/**
 * Created by hlopu on 07.11.2017.
 */

public class AlarmContract {
    public static final String TABLE_NAME = "alarms";

    public void createTable(@NonNull SQLiteDatabase database) {
        TableBuilder.create(TABLE_NAME)
                .intColumn(_ID).autoincrement()
                .intColumn(COLUMN_ALARM_HOURS)
                .intColumn(COLUMN_ALARM_MINUTES)
                .textColumn(COLUMN_ALARM_DATE)
                .textColumn(COLUMN_ALARM_STATE)
                .execute(database);
    }

    public static ContentValues toContentValues(@NonNull Alarm alarm) {
        ContentValues values = new ContentValues();
//        values.put(_ID, alarm.getId());
        values.put(AlarmEntry.COLUMN_ALARM_HOURS, alarm.getHours());
        values.put(AlarmEntry.COLUMN_ALARM_MINUTES, alarm.getMinutes());
        values.put(AlarmEntry.COLUMN_ALARM_DATE, alarm.getDate());
        values.put(AlarmEntry.COLUMN_ALARM_STATE, alarm.getState());
        return values;
    }

    public static Alarm fromCursor(@Nullable Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        int alarmId = cursor.getColumnIndex(_ID);
        int alarmHours = cursor.getColumnIndex(AlarmEntry.COLUMN_ALARM_HOURS);
        int alarmMinutes = cursor.getColumnIndex(AlarmEntry.COLUMN_ALARM_MINUTES);
        int alarmDate = cursor.getColumnIndex(AlarmEntry.COLUMN_ALARM_DATE);
        int alarmState = cursor.getColumnIndex(AlarmEntry.COLUMN_ALARM_STATE);
        int id = cursor.getInt(alarmId);
        int hours = cursor.getInt(alarmHours);
        int minutes = cursor.getInt(alarmMinutes);
        String date = cursor.getString(alarmDate);
        String state = cursor.getString(alarmState);
        Alarm mAlarm = new Alarm(id, hours, minutes, date, state);
        return mAlarm;
    }

    public static Uri getBaseUri() {
        return AlarmProvider.baseUri.buildUpon().appendPath(TABLE_NAME).build();
    }


    public class AlarmEntry implements BaseColumns {
        public static final String COLUMN_ALARM_HOURS = "hours";
        public static final String COLUMN_ALARM_MINUTES = "minutes";
        public static final String COLUMN_ALARM_DATE = "date";
        public static final String COLUMN_ALARM_STATE = "state";
        public static final String _ID = BaseColumns._ID;
    }

}
