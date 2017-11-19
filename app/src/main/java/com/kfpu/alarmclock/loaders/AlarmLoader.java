package com.kfpu.alarmclock.loaders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.kfpu.alarmclock.AlarmActivity;
import com.kfpu.alarmclock.models.Alarm;

import java.util.Calendar;

/**
 * Created by hlopu on 12.11.2017.
 */

public class AlarmLoader extends AsyncTaskLoader {
    private static String ACTION_ALARM = "ACTION_ALARM ";
    public static final String ADD_ALARM = "ADD_ALARM";
    public static final String EDIT_ALARM = "EDIT_ALARM";
    public static final String DELETE_ALARM = "DELETE_ALARM";
    public static final String DOWNLOAD_ALARM = "DOWNLOAD_ALARM";
    public static final long MILLISIOONEDAY = 86400000;
    private AlarmManager am;
    private Alarm alarm;
    private String code;

    public AlarmLoader(Context context, Bundle args) {
        super(context);
        alarm = (Alarm) args.getSerializable("alarm");
        this.code = args.getString("code");
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Integer loadInBackground() {
        am = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getContext(), AlarmActivity.class);
        intent.setAction(ACTION_ALARM + alarm.getId());
        intent.putExtra("id", alarm.getId());
        Calendar calendar = Calendar.getInstance();
        Log.d("DAY  ", String.valueOf(calendar.get(Calendar.DAY_OF_WEEK)));
        long addTimeMillis = calculatedMillis(alarm);

        calendar.set(Calendar.HOUR_OF_DAY, alarm.getHours());
        calendar.set(Calendar.MINUTE, alarm.getMinutes());

        PendingIntent pIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);

        System.out.println("TIME " + System.currentTimeMillis());
        System.out.println(addTimeMillis);
        switch (code) {
            case ADD_ALARM:
                am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+addTimeMillis, pIntent);
                break;
            case DELETE_ALARM:
                am.cancel(pIntent);
                break;
            case EDIT_ALARM:
                am.cancel(pIntent);
                am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+addTimeMillis, pIntent);
                break;
        }
        return 1;
    }

    public long calculatedMillis(Alarm alarm) {
        Calendar calendar = Calendar.getInstance();

        int min = 7;
        String aDays[] = alarm.getDate().split(",");
        for (int i = 0; i < aDays.length; i++) {
            int difference = Math.abs(Integer.valueOf(aDays[i]) - calendar.get(Calendar.DAY_OF_WEEK));
            if (difference < min) min = difference;
        }

        if(min == 0) {
            if ((alarm.getHours() < calendar.get(Calendar.HOUR_OF_DAY) ||
                    (alarm.getHours() == calendar.get(Calendar.HOUR_OF_DAY) && alarm.getMinutes() <= calendar.get(Calendar.MINUTE)))) {
                min = 1;
            }
        }
        return min * MILLISIOONEDAY;
    }
}
