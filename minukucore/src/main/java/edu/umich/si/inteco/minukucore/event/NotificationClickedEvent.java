package edu.umich.si.inteco.minukucore.event;

/**
 * Created by neerajkumar on 8/12/16.
 */
public class NotificationClickedEvent {

    /**
     * The notificationId of the notification tapped by the user.
     */
    private String mNotificationId;

    /**
     *
     * @param aNotificationId NotificationID of the notification tapped by the user.
     */
    public NotificationClickedEvent(String aNotificationId) {
       mNotificationId = aNotificationId;
    }

    public String getNotificationId() {
        return mNotificationId;
    }
}
