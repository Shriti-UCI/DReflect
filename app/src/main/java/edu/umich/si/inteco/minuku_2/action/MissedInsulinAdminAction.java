package edu.umich.si.inteco.minuku_2.action;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku_2.MoodDataRecordActivity;
import edu.umich.si.inteco.minuku_2.QuestionnaireActivity;
import edu.umich.si.inteco.minuku_2.R;
import edu.umich.si.inteco.minuku_2.event.MissedGlucoseReadingEvent;
import edu.umich.si.inteco.minuku_2.event.MissedInsulinAdminEvent;
import edu.umich.si.inteco.minuku_2.preferences.ApplicationConstants;
import edu.umich.si.inteco.minuku_2.question.QuestionConfig;
import edu.umich.si.inteco.minukucore.event.ShowNotificationEvent;
import edu.umich.si.inteco.minukucore.event.ShowNotificationEventBuilder;

/**
 * Created by shriti on 8/2/16.
 */
public class MissedInsulinAdminAction {
    private static final String TAG = "MisdInsulinAdminAction";

    public MissedInsulinAdminAction() {
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void handleMissedInsulinAdmin(MissedInsulinAdminEvent missedDataActionEvent) {
        Log.d(TAG, "Handling no data event for Insulin Admin Image");
        Map<String, String> dataSentToQuestinnaireActivity = new HashMap<>();
        dataSentToQuestinnaireActivity.put(Constants.BUNDLE_KEY_FOR_QUESTIONNAIRE_ID,
                String.valueOf(QuestionConfig.missedReportQuestionnaire_1.getID()));

        EventBus.getDefault().post(
                new ShowNotificationEventBuilder()
                        .setExpirationAction(ShowNotificationEvent.ExpirationAction.DISMISS)
                        .setExpirationTimeSeconds(Constants.MISSED_REPORT_NOTIFICATION_EXPIRATION_TIME)
                        .setViewToShow(QuestionnaireActivity.class)
                        .setIconID(R.drawable.bell_yellow)
                        .setTitle(Constants.MISSED_ACTIVITY_DATA_PROMPT_TITLE)
                        .setMessage(Constants.MISSED_ACTIVITY_DATA_PROMPT_MESSAGE)
                        .setCategory(ApplicationConstants.NOTIFICATION_CATEGORY_MISSED_ACTIVITY)
                        .setParams(dataSentToQuestinnaireActivity)
                        .createShowNotificationEvent());
    }
}
