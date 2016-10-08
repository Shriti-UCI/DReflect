package edu.umich.si.inteco.minuku_2.model;


import edu.umich.si.inteco.minukucore.model.DataRecord;

import java.util.Date;

/**
 * Created by shriti on 10/8/16.
 */

public class DiabetesLogDataRecord implements DataRecord {

    public String glucoseImageBase64Data;
    public String foodImageBase64Data;

    public float carbsConsumed;
    public float basalInsulin;
    public float bolusInsulin;

    public String note;

    public long creationTime;

    public DiabetesLogDataRecord() {

    }

    public DiabetesLogDataRecord(String glucoseImage, String foodImage,
                                 float carbsConsumed, float basalInsulin, float bolusInsulin,
                                 String note) {
        this.glucoseImageBase64Data = glucoseImage;
        this.foodImageBase64Data = foodImage;
        this.carbsConsumed = carbsConsumed;
        this.basalInsulin = basalInsulin;
        this.bolusInsulin = bolusInsulin;
        this.note = note;
        this.creationTime = new Date().getTime();
    }

    public String getGlucoseImageBase64Data() {
        return glucoseImageBase64Data;
    }

    public String getFoodImageBase64Data() {
        return foodImageBase64Data;
    }

    public float getCarbsConsumed() {
        return carbsConsumed;
    }

    public float getBasalInsulin() {
        return basalInsulin;
    }

    public float getBolusInsulin() {
        return bolusInsulin;
    }

    public String getNote() {
        return note;
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }
}
