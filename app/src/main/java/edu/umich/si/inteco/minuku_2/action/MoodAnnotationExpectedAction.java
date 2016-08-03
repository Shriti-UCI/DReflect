package edu.umich.si.inteco.minuku_2.action;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku_2.MoodDataRecordActivity;
import edu.umich.si.inteco.minuku_2.R;
import edu.umich.si.inteco.minuku_2.event.MissedInsulinAdminEvent;
import edu.umich.si.inteco.minuku_2.event.MoodDataExpectedActionEvent;
import edu.umich.si.inteco.minukucore.event.ShowNotificationEvent;
import edu.umich.si.inteco.minukucore.event.ShowNotificationEventBuilder;

/**
 * Created by shriti on 8/2/16.
 */
public class MoodAnnotationExpectedAction {

    private static final String TAG = "MoodAnnotnExpctdAction";

    public MoodAnnotationExpectedAction() {
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void handleMoodAnnotationExpected(MoodDataExpectedActionEvent missedDataActionEvent) {
        Log.d(TAG, "Handling state change event for mood data record");

        EventBus.getDefault().post(
                new ShowNotificationEventBuilder()
                        .setExpirationAction(ShowNotificationEvent.ExpirationAction.DISMISS)
                        .setExpirationTimeSeconds(Constants.MOOD_NOTIFICATION_EXPIRATION_TIME)
                        .setViewToShow(MoodDataRecordActivity.class)
                        .setIconID(R.drawable.analysis)
                        .setTitle(Constants.MOOD_REMINDER_TITLE)
                        .setMessage("")
                        .setParams(new HashMap<String, String>())
                        .createShowNotificationEvent()
        );
    }
}
