package com.kfpu.alarmclock.fragments;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kfpu.alarmclock.MainActivity;
import com.kfpu.alarmclock.R;
import com.kfpu.alarmclock.adapters.AlarmsRecyclerAdapter;
import com.kfpu.alarmclock.loaders.AlarmLoader;
import com.kfpu.alarmclock.models.Alarm;

import java.util.ArrayList;
import java.util.List;

import com.kfpu.alarmclock.tables.AlarmContract;

import static com.kfpu.alarmclock.MainActivity.ADD_TAG;
import static com.kfpu.alarmclock.adapters.AlarmsRecyclerAdapter.SWITCH_OFF;
import static com.kfpu.alarmclock.adapters.AlarmsRecyclerAdapter.SWITCH_ON;

/**
 * Created by hlopu on 06.11.2017.
 */

public class HomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Integer> {

    private RecyclerView rvAlarms;
    private Button addAlarm;
    private AlarmsRecyclerAdapter adapter;
    private Context context;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home, container, false);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.my_toolbar);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        addAlarm = view.findViewById(R.id.add_alarm);
        rvAlarms = view.findViewById(R.id.rv_alarms);
        rvAlarms.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AlarmsRecyclerAdapter(getContext());
        adapter.setAlarmListener(alarm -> {
            EditAlarmFragment editAlarmFragment = new EditAlarmFragment().newInstance(alarm);
            editAlarmFragment.setParentFragment(HomeFragment.this);
            getFragmentManager().beginTransaction().add(R.id.fragment_container, editAlarmFragment,
                    EditAlarmFragment.class.toString()).addToBackStack(ADD_TAG).commit();
        });
        adapter.setHomeFragment(HomeFragment.this);

        updateData();
        rvAlarms.setAdapter(adapter);

        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAlarmFragment addAlarmFragment = new AddAlarmFragment();
                addAlarmFragment.setParentFragment(HomeFragment.this);
                getFragmentManager().beginTransaction().add(R.id.fragment_container, addAlarmFragment, AddAlarmFragment.class.toString()).addToBackStack(ADD_TAG).commit();
            }
        });
    }

    public void updateData() {
        Cursor mCursor = getActivity().getContentResolver().query(AlarmContract.getBaseUri(),
                null, null, null, null);
        if (mCursor != null) {
            List<Alarm> mAlarms = new ArrayList<>();
            while (mCursor.moveToNext()) {
                Alarm changedAlarm = AlarmContract.fromCursor(mCursor);
                mAlarms.add(changedAlarm);
            }
            mCursor.close();
            adapter.setAlarms(mAlarms);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar_menu, menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public Loader<Integer> onCreateLoader(int id, Bundle args) {
        return new AlarmLoader(getContext(), args);
    }

    @Override
    public void onLoadFinished(Loader<Integer> loader, Integer data) {
        System.out.println("DATA RESULT" + data);

    }

    @Override
    public void onLoaderReset(Loader<Integer> loader) {

    }
    public void setContext(Context context){
        this.context = context;
    }
}
