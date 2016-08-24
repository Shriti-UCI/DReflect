package edu.umich.si.inteco.minuku.manager;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.StatusBarNotification;
import android.support.annotation.Nullable;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.dao.NotificationDAO;
import edu.umich.si.inteco.minukucore.dao.DAOException;
import edu.umich.si.inteco.minukucore.event.NotificationClickedEvent;
import edu.umich.si.inteco.minukucore.event.ShowNotificationEvent;
import edu.umich.si.inteco.minukucore.manager.DAOManager;
import edu.umich.si.inteco.minukucore.manager.NotificationManager;

/**
 * Created by neerajkumar on 8/3/16.
 */
public class MinukuNotificationManager extends Service implements NotificationManager {

    private static AtomicInteger CURRENT_NOTIFICATION_ID = new AtomicInteger(Integer.MIN_VALUE);
    private Map<Integer, ShowNotificationEvent> registeredNotifications;
    private Map<Integer, Integer> notificationCounterMap;
    private android.app.NotificationManager mNotificationManager;
    private Map<String, ShowNotificationEvent> categorizedNotificationMap;
    private NotificationDAO mDAO;

    public MinukuNotificationManager() {
        Log.d(TAG, "Started minuku notification manager");
        registeredNotifications = new HashMap<>();
        notificationCounterMap = new HashMap<>();
        categorizedNotificationMap = new HashMap<>();
        mDAO = MinukuDAOManager.getInstance().getDaoFor(ShowNotificationEvent.class);
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
                AlarmManager.RTC_WAKEUP,
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
            if(counter == null) {
                /* TODO(neerajkumar): This is happening due to concurrent modification. Fix it */
                continue;
            }
            Log.d(TAG, "Counter : " + counter);
            Log.d(TAG, "Notification " + notification.getExpirationTimeSeconds());
            if(counter == notification.getExpirationTimeSeconds()) {
                Log.d(TAG, "Counter for " + notification.getTitle() + " is matching.");

                switch (notification.getExpirationAction()) {
                    case DISMISS:
                        Log.d(TAG, "Dismissing " + notification.getTitle());
                        mNotificationManager.cancel(entry.getKey());
                        unregisterNotification(entry.getKey());
                        break;
                    case ALERT_AGAIN:
                        /**
                         * TODO(neerajkumar): Find a way to see if a notification was dismissed by
                         * the user and unregister it, if it was.
                         */

                        Log.d(TAG, "Alerting again " + notification.getTitle());
                        mNotificationManager.cancel(entry.getKey());
                        mNotificationManager.notify(notificationID,
                                buildNotificationForNotificationEvent(notification, entry.getKey()));
                        notification.incrementExpirationCount();
                        break;
                    case KEEP_SHOWING_WITHOUT_ALERT:
                        /**
                         * TODO(neerajkumar): Find a way to see if a notification was dismissed by
                         * the user and unregister it, if it was.
                         */
                        Log.d(TAG, "Ignoring " + notification.getTitle());
                        notification.incrementExpirationCount();
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
            ShowNotificationEvent aShowNotificationEvent, Integer id) {

        if(aShowNotificationEvent.getCreationTimeMs() != 0) {
            aShowNotificationEvent.setCreationTimeMs(new Date().getTime());
        }

        Intent launchIntent = new Intent(this, aShowNotificationEvent.getViewToShow());
        for(Map.Entry<String, String> entry: aShowNotificationEvent.getParams().entrySet()) {
            launchIntent.putExtra(entry.getKey(), entry.getValue());
        }
        launchIntent.putExtra(Constants.TAPPED_NOTIFICATION_ID_KEY, id.toString());

        PendingIntent pIntent = PendingIntent.getActivity(this,
                0, launchIntent, PendingIntent.FLAG_CANCEL_CURRENT);


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
        if(aShowNotificationEvent.getCategory() != null) {
            if(!categorizedNotificationMap.containsKey(aShowNotificationEvent.getCategory())) {
                categorizedNotificationMap.put(aShowNotificationEvent.getCategory(),
                        aShowNotificationEvent);
            } else {
                ShowNotificationEvent previousNotification = categorizedNotificationMap.get(
                        aShowNotificationEvent.getCategory());
                if(previousNotification.getExpirationCount() > 0) {
                    // If a previously existing notification has expired, then regardless of the
                    // expiration mechanism of such notification, remove it from the map, push
                    // that information to DAO and add the current notification to the map.
                    Integer id = null;
                    if((id = getIdForNotification(previousNotification)) != null) {
                        unregisterNotification(id);
                    }

                    try {
                        Log.d(TAG, "Adding previous notification information.");
                        mDAO.add(previousNotification);
                    } catch (DAOException e) {
                        e.printStackTrace();
                    }
                } else {
                    return;
                }
            }
        }
        Integer notificationID = CURRENT_NOTIFICATION_ID.incrementAndGet();
        Notification n = buildNotificationForNotificationEvent(aShowNotificationEvent, notificationID);
        registeredNotifications.put(notificationID, aShowNotificationEvent);
        notificationCounterMap.put(notificationID, 1);
        mNotificationManager.notify(notificationID, n);
    }

    @Subscribe
    public void handleNotificationClickEvent(NotificationClickedEvent notificationClickedEvent) {
        Log.d(TAG, "Trigerred notification click handler for notification ID"  +
                notificationClickedEvent.getNotificationId());
        Integer notificationId = getNotificationIdIfValid(
                notificationClickedEvent.getNotificationId());
        Log.d(TAG, "Valid notifiation id retrieved is " + notificationId);
        unregisterNotification(notificationId);
    }


    private Integer getNotificationIdIfValid(String aNotificationId) {
        try {
            Integer notificationId = Integer.parseInt(aNotificationId);
            if(registeredNotifications.containsKey(notificationId)) {
                return notificationId;
            } else {
                return null;
            }
        } catch (NumberFormatException numberFormatException) {
            return null;
        }
    }

    /**
     *
     * @param aNotificaitonId
     * @return if the notification associated with the notificationID passed in was successfully
     *          unregistered.
     */
    private boolean unregisterNotification(Integer aNotificaitonId) {
        if(registeredNotifications.containsKey(aNotificaitonId)) {
            ShowNotificationEvent notifiation = registeredNotifications.get(aNotificaitonId);
            notifiation.setClickedTimeMs(new Date().getTime());
            categorizedNotificationMap.remove(notifiation);
            registeredNotifications.remove(notifiation);
            notificationCounterMap.remove(aNotificaitonId);
            try {
                MinukuDAOManager.getInstance()
                        .getDaoFor(ShowNotificationEvent.class)
                        .add(registeredNotifications.get(aNotificaitonId));
            } catch (DAOException e) {
                e.printStackTrace();
                Log.e(TAG, "Could not notification info to DAO", e);
            }
        }
        // Notification was already unregistered at some earlier time or was never registered.
        // This is not a failure case, hence we return true.
        return true;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * (TODO:neerajkumar): Start using BiMaps
     */
    public Integer getIdForNotification(ShowNotificationEvent notification) {
        for(Map.Entry<Integer, ShowNotificationEvent> entry: registeredNotifications.entrySet()) {
            if(entry.getValue().equals(notification)) {
                return entry.getKey();
            }
        }
        Log.d(TAG, "Returning null object for notification");
        return null;
    }
}
