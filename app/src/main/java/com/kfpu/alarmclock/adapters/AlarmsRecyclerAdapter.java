package com.kfpu.alarmclock.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.kfpu.alarmclock.R;
import com.kfpu.alarmclock.fragments.AddAlarmFragment;
import com.kfpu.alarmclock.fragments.HomeFragment;
import com.kfpu.alarmclock.loaders.AlarmLoader;
import com.kfpu.alarmclock.models.Alarm;

import java.util.Collections;
import java.util.List;

import com.kfpu.alarmclock.tables.AlarmContract;

/**
 * Created by hlopu on 07.11.2017.
 */

public class AlarmsRecyclerAdapter extends RecyclerView.Adapter<AlarmsRecyclerAdapter.AlarmViewHolder> {
    public static final String SWITCH_ON = "ON";
    public static final String SWITCH_OFF = "OFF";
    private List<Alarm> mAlarms;
    private Context context;
    private AlarmListener mAlarmListener;
    private HomeFragment homeFragment;

    public AlarmsRecyclerAdapter(Context context) {
        this.context = context;
        mAlarms = Collections.emptyList();
    }

    public void setAlarmListener(AlarmListener alarmListener) {
        this.mAlarmListener = alarmListener;
    }

    public void setHomeFragment(HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
    }

    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.alarm_item,
                parent,
                false
        );
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AlarmViewHolder holder, int position) {
        final Alarm alarm = mAlarms.get(position);
        holder.timeAlarm.setText(alarm.getHours() + ":" + alarm.getMinutes());
        holder.dateAlarm.setText(convertDate(alarm.getDate()));
        if (alarm.getState().equals(SWITCH_ON)) {
            holder.aSwitch.setChecked(true);
        } else holder.aSwitch.setChecked(false);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAlarmListener != null) {
                    mAlarmListener.onAlarmClick(alarm);
                }
            }
        });
        holder.aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                Bundle bundle = new Bundle();
                if (holder.aSwitch.isChecked()) {
                    values.put(AlarmContract.AlarmEntry.COLUMN_ALARM_STATE, SWITCH_ON);
                    bundle.putSerializable("alarm", alarm);
                    bundle.putString("code", AlarmLoader.ADD_ALARM);
                    homeFragment.getLoaderManager().initLoader(1, bundle, homeFragment);
                } else values.put(AlarmContract.AlarmEntry.COLUMN_ALARM_STATE, SWITCH_OFF);
                context.getContentResolver().update(AlarmContract.getBaseUri(), values, AlarmContract.AlarmEntry._ID + "=?", new String[]{String.valueOf(alarm.getId())});
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAlarms.size();
    }

    public class AlarmViewHolder extends RecyclerView.ViewHolder {
        TextView timeAlarm;
        TextView dateAlarm;
        Switch aSwitch;

        public AlarmViewHolder(View itemView) {
            super(itemView);
            timeAlarm = itemView.findViewById(R.id.alarm_time);
            dateAlarm = itemView.findViewById(R.id.tv_alarm_date);
            aSwitch = itemView.findViewById(R.id.alarm_switch);
        }
    }

    public void setAlarms(List<Alarm> mAlarms) {
        this.mAlarms = mAlarms;
        notifyDataSetChanged();
    }

    public interface AlarmListener {
        void onAlarmClick(Alarm alarm);
    }

    private String convertDate(String dateOfNumbers) {
        dateOfNumbers = dateOfNumbers.replace("1", "Sun");
        dateOfNumbers = dateOfNumbers.replace("2", "Mon");
        dateOfNumbers = dateOfNumbers.replace("3", "Tue");
        dateOfNumbers = dateOfNumbers.replace("4", "Thu");
        dateOfNumbers = dateOfNumbers.replace("5", "Wed");
        dateOfNumbers = dateOfNumbers.replace("6", "Fri");
        dateOfNumbers = dateOfNumbers.replace("7", "Sat");
        if(dateOfNumbers.endsWith(","))  dateOfNumbers = dateOfNumbers.substring(0, dateOfNumbers.length()-1);
        return dateOfNumbers;
    }


}
