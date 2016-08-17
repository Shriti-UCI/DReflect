package edu.umich.si.inteco.minuku_2.view.helper;

import edu.umich.si.inteco.minukucore.action.Action;

/**
 * Created by shriti on 7/20/16.
 */
public class ActionObject {
    protected String label;
    protected String secondaryLabel;
    protected int imageResourceId;

    public ActionObject() {

    }

    public ActionObject(String label, int imageResourceId) {
        this.imageResourceId = imageResourceId;
        this.label = label;
        this.secondaryLabel = null;
    }

    public ActionObject(String label, String secondaryLabel, int imageResourceId) {
        this.label = label;
        this.secondaryLabel = secondaryLabel;
        this.imageResourceId = imageResourceId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSecondaryLabel() {
        return secondaryLabel;
    }

    public void setSecondaryLabel(String secondaryLabel) {
        this.secondaryLabel = secondaryLabel;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }
}
