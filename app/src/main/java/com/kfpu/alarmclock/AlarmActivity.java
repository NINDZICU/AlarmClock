package com.kfpu.alarmclock;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.kfpu.alarmclock.data.AlarmOpenHelper;
import com.kfpu.alarmclock.fragments.AddAlarmFragment;
import com.kfpu.alarmclock.loaders.AlarmLoader;
import com.kfpu.alarmclock.models.Alarm;
import com.kfpu.alarmclock.tables.AlarmContract;

import java.util.ArrayList;
import java.util.List;

public class AlarmActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Integer> {
    private MediaPlayer mediaPlayer;
    private AudioManager am;
    private Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        btnCancel = findViewById(R.id.btn_cancel);
        mediaPlayer = MediaPlayer.create(this, R.raw.animal);
        mediaPlayer.start();


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();

                System.out.println("IDDDDD2 " + getIntent().getAction());
                String[] action = getIntent().getAction().split(" ");
                Cursor mCursor = getContentResolver().query(AlarmContract.getBaseUri(),
                        null, AlarmContract.AlarmEntry._ID + "=?", new String[]{action[1]}, null);
                Alarm changedAlarm = null;
                if (mCursor != null) {
                    while (mCursor.moveToNext()) {
                        changedAlarm = AlarmContract.fromCursor(mCursor);
                    }
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("alarm", changedAlarm);
                bundle.putString("code", AlarmLoader.ADD_ALARM);
                getLoaderManager().initLoader(1, bundle, AlarmActivity.this);
                finish();
            }
        });
    }

    @Override
    public Loader<Integer> onCreateLoader(int id, Bundle args) {
        return new AlarmLoader(this, args);
    }

    @Override
    public void onLoadFinished(Loader<Integer> loader, Integer data) {
        System.out.println("DATA RESULT" + data);
    }

    @Override
    public void onLoaderReset(Loader<Integer> loader) {

    }
}
