package edu.umich.si.inteco.minuku.model;

import java.util.Date;

import edu.umich.si.inteco.minukucore.model.DataRecord;

/**
 * Created by neerajkumar on 7/21/16.
 */
public class SemanticLocationDataRecord implements DataRecord {

    public String mSemanticLocation;
    public long mCreationTime;

    public SemanticLocationDataRecord() {

    }

    public SemanticLocationDataRecord(String aSemanticLocation) {
        this.mCreationTime = new Date().getTime();
        this.mSemanticLocation = aSemanticLocation;
    }

    public void setSemanticLocation(String mSemanticLocation) {
        this.mSemanticLocation = mSemanticLocation;
    }

    public void setCreationTime(long mCreationTime) {
        this.mCreationTime = mCreationTime;
    }

    public String getSemanticLocation() {
        return mSemanticLocation;
    }

    @Override
    public long getCreationTime() {
        return mCreationTime;
    }

}
