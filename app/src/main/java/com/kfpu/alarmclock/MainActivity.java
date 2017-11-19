package com.kfpu.alarmclock;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kfpu.alarmclock.data.AlarmOpenHelper;
import com.kfpu.alarmclock.fragments.HomeFragment;

import com.kfpu.alarmclock.tables.AlarmContract;

public class MainActivity extends AppCompatActivity {
    public static final String FRAGMENT_TAG = "my_super_fragment_tag";
    public static final String ADD_TAG = "add_fragment_tag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AlarmOpenHelper helper = new AlarmOpenHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        System.out.println(db);

        HomeFragment homeFragment = new HomeFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment, FRAGMENT_TAG).commit();
    }


}
