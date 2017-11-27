package com.kfpu.alarmclock.fragments;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;

import com.kfpu.alarmclock.AlarmActivity;
import com.kfpu.alarmclock.R;
import com.kfpu.alarmclock.models.Alarm;

import java.util.Calendar;

import com.kfpu.alarmclock.loaders.AlarmLoader;
import com.kfpu.alarmclock.tables.AlarmContract;

/**
 * Created by hlopu on 06.11.2017.
 */

public class AddAlarmFragment extends Fragment implements LoaderManager.LoaderCallbacks<Integer> {
    public final static String MONDAY = "2";
    public final static String TUESDAY = "3";
    public final static String THURSDAY = "4";
    public final static String WEDNESDAY = "5";
    public final static String FRIDAY = "6";
    public final static String SATURDAY = "7";
    public final static String SUNDAY = "1";


    private HomeFragment parentFragment;
    private TimePicker timePicker;
    private CheckBox day1;
    private CheckBox day2;
    private CheckBox day3;
    private CheckBox day4;
    private CheckBox day5;
    private CheckBox day6;
    private CheckBox day7;
    private CheckBox allDay;
    private Button btnAddAlarm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_alarm, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        timePicker = view.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        day1 = view.findViewById(R.id.day_1);
        day2 = view.findViewById(R.id.day_2);
        day3 = view.findViewById(R.id.day_3);
        day4 = view.findViewById(R.id.day_4);
        day5 = view.findViewById(R.id.day_5);
        day6 = view.findViewById(R.id.day_6);
        day7 = view.findViewById(R.id.day_7);
        allDay = view.findViewById(R.id.all_day);
        btnAddAlarm = view.findViewById(R.id.btn_save_alarm);
        Calendar now = Calendar.getInstance();

        timePicker.setCurrentHour(now.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(now.get(Calendar.MINUTE));
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                System.out.println("hour " + hourOfDay + " minute   " + minute);
            }
        });

        allDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allDay.isChecked()) setCheckedCheckboxes(true);
                else setCheckedCheckboxes(false);
            }
        });

        btnAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String days = "";
                if (day1.isChecked()) days += MONDAY + ",";
                if (day2.isChecked()) days += TUESDAY + ",";
                if (day3.isChecked()) days += THURSDAY + ",";
                if (day4.isChecked()) days += WEDNESDAY + ",";
                if (day5.isChecked()) days += FRIDAY + ",";
                if (day6.isChecked()) days += SATURDAY + ",";
                if (day7.isChecked()) days += SUNDAY;
                if (days.equals(""))
                    Toast.makeText(getActivity(), "Выберите дни недели", Toast.LENGTH_LONG).show();
                else {
                    Alarm alarm = new Alarm(timePicker.getCurrentHour(), timePicker.getCurrentMinute(), days, "ON");
                    Uri uri =getActivity().getContentResolver().insert(AlarmContract.getBaseUri(), AlarmContract.toContentValues(alarm));
                    getActivity().getContentResolver().notifyChange(AlarmContract.getBaseUri(), null);
                    parentFragment.updateData();
                    Bundle bundle = new Bundle();
                    alarm.setId((int)ContentUris.parseId(uri));
                    bundle.putSerializable("alarm", alarm);
                    bundle.putString("code", AlarmLoader.ADD_ALARM);
                    getLoaderManager().initLoader(1, bundle, AddAlarmFragment.this);
                    closeFragment();
                }
            }
        });
    }

    public void setCheckedCheckboxes(boolean check) {
        day1.setChecked(check);
        day2.setChecked(check);
        day3.setChecked(check);
        day4.setChecked(check);
        day5.setChecked(check);
        day6.setChecked(check);
        day7.setChecked(check);
    }

    public void closeFragment() {
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
    }

    public void setParentFragment(Fragment fragment) {
        this.parentFragment = (HomeFragment) fragment;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public Loader<Integer> onCreateLoader(int id, Bundle args) {
        return new AlarmLoader(getActivity(), args);
    }

    @Override
    public void onLoadFinished(Loader<Integer> loader, Integer data) {
        System.out.println("DATA RESULT" + data);

    }

    @Override
    public void onLoaderReset(Loader<Integer> loader) {

    }
}
