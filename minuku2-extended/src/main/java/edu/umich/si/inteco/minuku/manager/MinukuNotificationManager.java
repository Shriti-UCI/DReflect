package edu.umich.si.inteco.minuku.manager;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minukucore.event.ShowNotificationEvent;
import edu.umich.si.inteco.minukucore.manager.NotificationManager;

/**
 * Created by neerajkumar on 8/3/16.
 */
public class MinukuNotificationManager extends Service implements NotificationManager {

    private static AtomicInteger CURRENT_NOTIFICATION_ID = new AtomicInteger(Integer.MIN_VALUE);
    private Map<Integer, ShowNotificationEvent> registeredNotifications;
    private Map<Integer, Integer> notificationCounterMap;
    private android.app.NotificationManager mNotificationManager;

    public MinukuNotificationManager() {
        Log.d(TAG, "Started minuku notification manager");
        registeredNotifications = new HashMap<>();
        notificationCounterMap = new HashMap<>();
        EventBus.getDefault().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "OnStartCommand");

        if(mNotificationManager == null) {
            mNotificationManager = (android.app.NotificationManager) getSystemService(
                    Service.NOTIFICATION_SERVICE);
        }

        AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarm.set(
                alarm.RTC_WAKEUP,
                System.currentTimeMillis() + Constants.PROMPT_SERVICE_REPEAT_MILLISECONDS,
                PendingIntent.getService(this, 0, new Intent(this, MinukuNotificationManager.class), 0)
        );

        checkRegisteredNotifications();

        return START_STICKY_COMPATIBILITY;
    }

    private void checkRegisteredNotifications() {

        Log.d(TAG, "Checking for registered notificaitons.");

        for(Map.Entry<Integer, ShowNotificationEvent> entry: registeredNotifications.entrySet()) {
            ShowNotificationEvent notification = entry.getValue();
            Integer notificationID = entry.getKey();
            Integer counter = notificationCounterMap.get(notificationID);
            if(counter == notification.getExpirationTimeSeconds()) {
                Log.d(TAG, "Counter for " + notification.getTitle() + " is matching.");

                switch (notification.getExpirationAction()) {
                    case DISMISS:
                        Log.d(TAG, "Dismissing " + notification.getTitle());

                        mNotificationManager.cancel(entry.getKey());
                        break;
                    case ALERT_AGAIN:
                        Log.d(TAG, "Alerting again " + notification.getTitle());

                        mNotificationManager.cancel(entry.getKey());
                        mNotificationManager.notify(notificationID,
                                buildNotificationForNotificationEvent(notification));
                        break;
                    case KEEP_SHOWING_WITHOUT_ALERT:
                        Log.d(TAG, "Ignoring " + notification.getTitle());
                        break;
                    default:
                        break;
                }

                counter = 0;
            }
            counter++;
            notificationCounterMap.put(notificationID, counter);
        }

    }

    private Notification buildNotificationForNotificationEvent(
            ShowNotificationEvent aShowNotificationEvent) {
        Intent launchIntent = new Intent(this, aShowNotificationEvent.getViewToShow());
        PendingIntent pIntent = PendingIntent.getActivity(this,
                0, launchIntent, 0);
        Notification n  = new Notification.Builder(this)
                .setContentTitle(aShowNotificationEvent.getTitle())
                .setContentIntent(pIntent)
                .setContentText(aShowNotificationEvent.getMessage())
                .setSmallIcon(aShowNotificationEvent.getIconID())
                .setAutoCancel(true)
                .build();
        n.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
        return n;
    }

    private static final String TAG = "MinNotificationManager";

    @Subscribe
    @Override
    public void handleShowNotificationEvent(ShowNotificationEvent aShowNotificationEvent) {
        Log.d(TAG, "Handling notification event.");
        Notification n = buildNotificationForNotificationEvent(aShowNotificationEvent);
        Integer notificationID = CURRENT_NOTIFICATION_ID.incrementAndGet();
        registeredNotifications.put(notificationID, aShowNotificationEvent);
        notificationCounterMap.put(notificationID, 1);
        mNotificationManager.notify(notificationID, n);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
