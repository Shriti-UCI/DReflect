package edu.umich.si.inteco.minukucore.event;

import java.util.Map;

/**
 * Created by neerajkumar on 7/30/16.
 */
public class ShowNotificationEvent {

    private String title;
    private String message;
    private int iconID;
    private int expirationTimeSeconds;
    private Class<Object> viewToShow;
    private ExpirationAction expirationAction;
    private Map<String, String> params;
    private long creationTimeMs = 0;
    private long clickedTimeMs = 0;
    private String category = null;

    public enum ExpirationAction {
        ALERT_AGAIN,
        DISMISS,
        KEEP_SHOWING_WITHOUT_ALERT
    }

    public ShowNotificationEvent(String title,
                                 String message,
                                 int iconID,
                                 int expirationTimeSeconds,
                                 Class viewToShow,
                                 ExpirationAction expirationAction,
                                 Map<String, String> params,
                                 String category) {
        this.category = category;
        new ShowNotificationEvent(
                title,
                message,
                iconID,
                expirationTimeSeconds,
                viewToShow,
                expirationAction,
                params);
    }

    public ShowNotificationEvent(String title,
                                 String message,
                                 int iconID,
                                 int expirationTimeSeconds,
                                 Class viewToShow,
                                 ExpirationAction expirationAction,
                                 Map<String, String> params) {
        this.title = title;
        this.message = message;
        this.iconID = iconID;
        this.expirationTimeSeconds = expirationTimeSeconds;
        this.viewToShow = viewToShow;
        this.expirationAction = expirationAction;
        this.params = params;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public int getIconID() {
        return iconID;
    }

    public int getExpirationTimeSeconds() {
        return expirationTimeSeconds;
    }

    public Class getViewToShow() {
        return viewToShow;
    }

    public ExpirationAction getExpirationAction() {
        return expirationAction;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public String getCategory() {
        return this.category;
    }


    public long getCreationTimeMs() {
        return this.creationTimeMs;
    }

    public void setCreationTimeMs(long creationTimeMs) {
        this.creationTimeMs = creationTimeMs;
    }

    public long getClickedTimeMs() {
        return this.clickedTimeMs;
    }

    public void setClickedTimeMs(long clickedTimeMs) {
        this.clickedTimeMs = clickedTimeMs;
    }
}
