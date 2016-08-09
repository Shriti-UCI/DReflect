package edu.umich.si.inteco.minukucore.event;

import android.util.Log;

import java.util.Map;

public class ShowNotificationEventBuilder {
    private String title;
    private String message;
    private int iconID;
    private int expirationTimeSeconds;
    private Class viewToShow;
    private ShowNotificationEvent.ExpirationAction expirationAction;
    private Map<String, String> params;

    private String TAG = "ShowNotificationEventBuilder";

    public ShowNotificationEventBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public ShowNotificationEventBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public ShowNotificationEventBuilder setIconID(int iconID) {
        this.iconID = iconID;
        return this;
    }

    public ShowNotificationEventBuilder setExpirationTimeSeconds(int expirationTimeSeconds) {
        this.expirationTimeSeconds = expirationTimeSeconds;
        return this;
    }

    public ShowNotificationEventBuilder setViewToShow(Class viewToShow) {
        this.viewToShow = viewToShow;
        return this;
    }

    public ShowNotificationEventBuilder setExpirationAction(ShowNotificationEvent.ExpirationAction expirationAction) {
        this.expirationAction = expirationAction;
        return this;
    }

    public ShowNotificationEvent createShowNotificationEvent() {
        Log.d(TAG, "Returing show notification event for " + title);
        return new ShowNotificationEvent(title,
                message,
                iconID,
                expirationTimeSeconds,
                viewToShow,
                expirationAction,
                params);
    }

    public ShowNotificationEventBuilder setParams(Map<String, String> someParams) {
        this.params = someParams;
        return this;
    }
}