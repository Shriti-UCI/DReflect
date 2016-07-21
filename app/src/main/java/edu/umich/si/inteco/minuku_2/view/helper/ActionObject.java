package edu.umich.si.inteco.minuku_2.view.helper;

/**
 * Created by shriti on 7/20/16.
 */
public class ActionObject {
    String label;
    String secondaryLabel;
    int imageResourceId;

    public ActionObject(String label, String secondaryLabel, int imageResourceId) {
        this.label = label;
        this.secondaryLabel = secondaryLabel;
        this.imageResourceId = imageResourceId;
    }
}
