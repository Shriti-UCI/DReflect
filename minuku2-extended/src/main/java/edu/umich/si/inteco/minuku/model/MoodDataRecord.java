package edu.umich.si.inteco.minuku.model;

import java.util.Date;

import edu.umich.si.inteco.minukucore.model.DataRecord;

/**
 * Created by shriti on 7/21/16.
 * Code reused from intel's mood map
 *
 * see: https://github.com/ohmage/mood-map-android
 */
public class MoodDataRecord implements DataRecord {

    /**
     * X axis to draw
     */
    public float x = -50f;
    /**
     * Y axis to draw
     */
    public float y = -50f;
    /**
     * Checking flag while drag the mood
     */
    public boolean isSelected = false;
    /**
     * Checking flag while Inserting and Posting the mood
     */
    public boolean isCreated = false;

    //Actual values, X and Y range from -10 to +10, X-Mood, Y-Energy
    /**
     * X Possition on 10/10 graph
     */
    public float moodLevel = -50;
    /**
     * Y Possition on 10/10 graph
     */
    public float energyLevel = -50;

    public long creationTime;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean getIsSelected() {

        return isSelected;
    }

    public boolean getIsCreated() {

        return isCreated;
    }

    public float getMoodLevel() {

        return moodLevel;
    }

    public float getEnergyLevel() {

        return energyLevel;
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime =creationTime;
    }

    public MoodDataRecord(){

    }

    public MoodDataRecord(float x, float y, boolean isSelected, boolean isCreated, float moodLevel,
                          float energyLevel) {
        this.x = x;
        this.y = y;
        this.isSelected = isSelected;
        this.isCreated = isCreated;
        this.moodLevel = moodLevel;
        this.energyLevel = energyLevel;
        this.creationTime = new Date().getTime();
    }

    /**todo: a) add a simple annotation after selecting mood, OR
     b)compare with last saved mood and ask why did your mood change this way?, OR
     c)if it is a negative mood that is sellected, ask what happened?
     Put this as a situation??**/
}
