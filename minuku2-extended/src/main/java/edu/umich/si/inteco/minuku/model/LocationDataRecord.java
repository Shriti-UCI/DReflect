package edu.umich.si.inteco.minuku.model;

import java.util.Date;

import edu.umich.si.inteco.minukucore.model.DataRecord;
/**
 * Created by shriti on 7/15/16.
 */
public class LocationDataRecord implements DataRecord {

    public float latitude;
    public float longitude;
    public long creationTime;

    public LocationDataRecord() {

    }

    public LocationDataRecord(float latitude, float longitude) {
        this.creationTime = new Date().getTime();
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public String toString() {
        return "Loc:" + this.latitude + ":" + this.longitude;
    }
}
