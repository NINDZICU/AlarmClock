package com.kfpu.alarmclock.fragments;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;

import com.kfpu.alarmclock.R;
import com.kfpu.alarmclock.loaders.AlarmLoader;
import com.kfpu.alarmclock.models.Alarm;

import com.kfpu.alarmclock.tables.AlarmContract;

import static com.kfpu.alarmclock.fragments.AddAlarmFragment.FRIDAY;
import static com.kfpu.alarmclock.fragments.AddAlarmFragment.MONDAY;
import static com.kfpu.alarmclock.fragments.AddAlarmFragment.SATURDAY;
import static com.kfpu.alarmclock.fragments.AddAlarmFragment.SUNDAY;
import static com.kfpu.alarmclock.fragments.AddAlarmFragment.THURSDAY;
import static com.kfpu.alarmclock.fragments.AddAlarmFragment.TUESDAY;
import static com.kfpu.alarmclock.fragments.AddAlarmFragment.WEDNESDAY;

/**
 * Created by hlopu on 08.11.2017.
 */

public class EditAlarmFragment extends Fragment implements LoaderManager.LoaderCallbacks<Integer> {

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
    private Button btnEditAlarm;
    private Button btnDeleteAlarm;

    public static EditAlarmFragment newInstance(Alarm alarm) {

        Bundle args = new Bundle();
        args.putSerializable("alarm", alarm);
        EditAlarmFragment fragment = new EditAlarmFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_alarm, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Alarm alarm = (Alarm) getArguments().getSerializable("alarm");

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
        btnEditAlarm = view.findViewById(R.id.btn_save_alarm);
        btnDeleteAlarm = view.findViewById(R.id.btn_delete_alarm);
        btnDeleteAlarm.setVisibility(View.VISIBLE);

        timePicker.setCurrentHour(alarm.getHours());
        timePicker.setCurrentMinute(alarm.getMinutes());

        String aDays[] = alarm.getDate().split(",");
        for (int i = 0; i < aDays.length; i++) {
            switch (aDays[i]) {
                case MONDAY:
                    day1.setChecked(true);
                    break;
                case TUESDAY:
                    day2.setChecked(true);
                    break;
                case THURSDAY:
                    day3.setChecked(true);
                    break;
                case WEDNESDAY:
                    day4.setChecked(true);
                    break;
                case FRIDAY:
                    day5.setChecked(true);
                    break;
                case SATURDAY:
                    day6.setChecked(true);
                    break;
                case SUNDAY:
                    day7.setChecked(true);
                    break;
            }
        }
        allDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allDay.isChecked()) setCheckedCheckboxes(true);
                else setCheckedCheckboxes(false);
            }
        });

        btnEditAlarm.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
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
                    Alarm alarm1 = new Alarm(alarm.getId(), timePicker.getCurrentHour(), timePicker.getCurrentMinute(), days, alarm.getState());
                    getActivity().getContentResolver().update(AlarmContract.getBaseUri(), AlarmContract.toContentValues(alarm1),
                            AlarmContract.AlarmEntry._ID + "=?", new String[]{String.valueOf(alarm.getId())});
                    updateLists();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("alarm", alarm1);
                    bundle.putString("code", AlarmLoader.EDIT_ALARM);
                    getLoaderManager().initLoader(1, bundle, EditAlarmFragment.this);
                    getActivity().getFragmentManager().beginTransaction().remove(EditAlarmFragment.this).commit();
                }
            }
        });

        btnDeleteAlarm.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                getActivity().getContentResolver().delete(AlarmContract.getBaseUri(), AlarmContract.AlarmEntry._ID + "=?",
                        new String[]{String.valueOf(alarm.getId())});
                getActivity().getContentResolver().notifyChange(AlarmContract.getBaseUri(), null);
                updateLists();

                Bundle bundle = new Bundle();
                bundle.putSerializable("alarm", alarm);
                bundle.putString("code", AlarmLoader.DELETE_ALARM);
                getLoaderManager().initLoader(1, bundle, EditAlarmFragment.this);

                getActivity().getFragmentManager().beginTransaction().remove(EditAlarmFragment.this).commit();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void updateLists() {
        parentFragment.updateData();
    }

    public void setParentFragment(Fragment fragment) {
        this.parentFragment = (HomeFragment) fragment;
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
