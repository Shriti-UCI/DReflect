package edu.umich.si.inteco.minuku.model;

import java.util.Date;

import edu.umich.si.inteco.minukucore.model.DataRecord;

/**
 * Created by shriti on 7/19/16.
 */
public class ImageDataRecord implements DataRecord {

    public String base64Data;
    public long creationTime;

    public ImageDataRecord(String base64Data) {
        this.base64Data = base64Data;
        this.creationTime = new Date().getTime();;
    }

    public String getBase64Data() {
        return base64Data;
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }
}
