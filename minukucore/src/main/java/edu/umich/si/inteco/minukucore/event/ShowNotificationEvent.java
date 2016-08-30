package edu.umich.si.inteco.minukucore.event;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import edu.umich.si.inteco.minukucore.model.DataRecord;

/**
 * Created by neerajkumar on 7/30/16.
 */
public class ShowNotificationEvent implements DataRecord {

    public String title;
    public String message;
    public int iconID;
    public int expirationTimeSeconds;
    public Class<Object> viewToShow;
    public ExpirationAction expirationAction;
    public Map<String, String> params;
    public long creationTimeMs = 0;
    public long clickedTimeMs = 0;
    public String category = null;
    public int expirationCount = 0;
    public Integer id;
    public Integer counter;

    @Override
    public long getCreationTime() {
        return this.creationTimeMs;
    }

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
        this(title,
                message,
                iconID,
                expirationTimeSeconds,
                viewToShow,
                expirationAction,
                params);

        this.category = category;
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
        this.counter = new Integer(0);
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

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setIconID(int iconID) {
        this.iconID = iconID;
    }

    public void setExpirationTimeSeconds(int expirationTimeSeconds) {
        this.expirationTimeSeconds = expirationTimeSeconds;
    }

    public void setViewToShow(Class<Object> viewToShow) {
        this.viewToShow = viewToShow;
    }

    public void setExpirationAction(ExpirationAction expirationAction) {
        this.expirationAction = expirationAction;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public int getExpirationCount() {
        return expirationCount;
    }

    public void setExpirationCount(int expirationCount) {
        this.expirationCount = expirationCount;
    }

    public void incrementExpirationCount() {
        this.expirationCount++;
    }

    @Override
    public String toString() {
        return this.title + ":\n" + this.expirationAction + ":\n" + this.expirationCount + ":\n" +
                this.viewToShow + ":\n" + this.message;
    }
}
