package com.kfpu.alarmclock.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.kfpu.alarmclock.fragments.AddAlarmFragment;
import com.kfpu.alarmclock.loaders.AlarmLoader;
import com.kfpu.alarmclock.models.Alarm;
import com.kfpu.alarmclock.tables.AlarmContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hlopu on 12.11.2017.
 */

public class BroadcastOnBoot extends BroadcastReceiver {
    private static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_BOOT.equals(intent.getAction())) {
            Cursor mCursor = context.getContentResolver().query(AlarmContract.getBaseUri(),
                    null, null, null, null);
            if (mCursor != null) {
                while (mCursor.moveToNext()) {
                    Alarm changedAlarm = AlarmContract.fromCursor(mCursor);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("alarm", changedAlarm);
                    bundle.putString("code", AlarmLoader.ADD_ALARM);
                    AlarmLoader loader = new AlarmLoader(context, bundle);
                    loader.startLoading();
                }
                mCursor.close();
            }
        }
    }
}
