package edu.umich.si.inteco.minukucore.manager;

import edu.umich.si.inteco.minukucore.event.ShowNotificationEvent;
import edu.umich.si.inteco.minukucore.event.Subscribe;

/**
 * Created by neerajkumar on 8/3/16.
 */
public interface NotificationManager {

    @Subscribe
    public void handleShowNotificationEvent(ShowNotificationEvent aShowNotificationEvent);
}
