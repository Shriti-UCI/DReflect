package edu.umich.si.inteco.minuku.model;

import java.util.Date;

import edu.umich.si.inteco.minukucore.model.DataRecord;

/**
 * Created by shriti on 8/20/16.
 */
public class NoteDataRecord implements DataRecord {

    public String note;
    public long creationTime;

    public NoteDataRecord() {

    }

    public NoteDataRecord(String note) {
        this.note = note;
        this.creationTime = new Date().getTime();
    }

    public String getNote() {
        return this.note;
    }

    public long getCreationTime() {
        return this.creationTime;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }
}
