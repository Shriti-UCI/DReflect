package edu.umich.si.inteco.minuku_2.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.manager.MinukuStreamManager;
import edu.umich.si.inteco.minuku_2.MainActivity;
import edu.umich.si.inteco.minuku_2.MoodDataRecordActivity;
import edu.umich.si.inteco.minuku_2.QuestionnaireActivity;
import edu.umich.si.inteco.minukucore.event.ShowNotificationEvent;

public class BackgroundService extends Service {

    private static final String TAG = "BackgroundService";
    MinukuStreamManager streamManager;

    public BackgroundService() {
        streamManager = MinukuStreamManager.getInstance();

        Notification note  = new Notification.Builder(this)
                .setContentTitle(Constants.APP_NAME)
                .setContentText(Constants.RUNNING_APP_DECLARATION)
                .setSmallIcon(edu.umich.si.inteco.minuku.R.drawable.cast_ic_notification_small_icon)
                .setAutoCancel(false)
                .build();
        note.flags |= Notification.FLAG_NO_CLEAR;
        startForeground( 42, note );
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        streamManager.updateStreamGenerators();
        AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarm.set(
                alarm.RTC_WAKEUP,
                System.currentTimeMillis() + Constants.PROMPT_SERVICE_REPEAT_MILLISECONDS,
                PendingIntent.getService(this, 0, new Intent(this, BackgroundService.class), 0)
        );

        return START_STICKY_COMPATIBILITY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Destroying service. Your state might be lost!");
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
