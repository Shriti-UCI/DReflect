package edu.umich.si.inteco.minuku_2.action;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.logger.Log;
import edu.umich.si.inteco.minuku_2.QuestionnaireActivity;
import edu.umich.si.inteco.minuku_2.R;
import edu.umich.si.inteco.minuku_2.event.MissedFoodEvent;
import edu.umich.si.inteco.minuku_2.preferences.ApplicationConstants;
import edu.umich.si.inteco.minuku_2.question.QuestionConfig;
import edu.umich.si.inteco.minukucore.event.ShowNotificationEvent;
import edu.umich.si.inteco.minukucore.event.ShowNotificationEventBuilder;

/**
 * Created by shriti on 8/12/16.
 */
public class MissedFoodAction {

    private String TAG = "MissedFoodAction";

    public MissedFoodAction() {
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void handleMissedFoodAction(MissedFoodEvent missedFoodEvent) {
        Log.d(TAG, "Handling no data event for  food image");
        Map<String, String> dataSentToQuestinnaireActivity = new HashMap<>();
        dataSentToQuestinnaireActivity.put(Constants.BUNDLE_KEY_FOR_QUESTIONNAIRE_ID,
                String.valueOf(QuestionConfig.missedReportQuestionnaire_2.getID()));
        dataSentToQuestinnaireActivity.put(Constants.BUNDLE_KEY_FOR_NOTIFICATION_SOURCE,
                Constants.FOOD_NOTIFICATION_SOURCE);

        EventBus.getDefault().post(
                new ShowNotificationEventBuilder()
                .setExpirationAction(ShowNotificationEvent.ExpirationAction.DISMISS)
                .setExpirationTimeSeconds(Constants.MISSED_REPORT_NOTIFICATION_EXPIRATION_TIME)
                .setViewToShow(QuestionnaireActivity.class)
                .setIconID(R.drawable.bell_yellow)
                .setTitle(Constants.MISSED_ACTIVITY_DATA_PROMPT_TITLE)
                .setCategory(ApplicationConstants.NOTIFICATION_CATEGORY_MISSED_ACTIVITY)
                .setMessage(Constants.MISSED_ACTIVITY_DATA_PROMPT_MESSAGE)
                .setParams(dataSentToQuestinnaireActivity)
                .createShowNotificationEvent());
    }
}
