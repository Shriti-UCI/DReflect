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
import edu.umich.si.inteco.minukucore.event.ShowNotificationEvent;

public class BackgroundService extends Service {

    private static final String TAG = "BackgroundService";
    MinukuStreamManager streamManager;

    public BackgroundService() {
        streamManager = MinukuStreamManager.getInstance();
        EventBus.getDefault().register(this);
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
        Log.d(TAG, "Destroying service.");
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Subscribe
    public void handleNotificationEvent(ShowNotificationEvent aShowNotificationEvent) {
        Log.d(TAG, "Handling notification event.");
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(
                        Service.NOTIFICATION_SERVICE);
        //PackageManager pm = getPackageManager();
        Intent launchIntent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this,
                0, launchIntent, 0);

        Notification n  = new Notification.Builder(this)
                .setContentTitle(aShowNotificationEvent.title)
                .setContentIntent(pIntent)
                .setContentText("Yoohoo")
                .setSmallIcon(edu.umich.si.inteco.minuku.R.drawable.common_google_signin_btn_icon_dark)
                .setAutoCancel(true)
                .build();

        n.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, n);
    }
}
