package edu.umich.si.inteco.minuku.model;

import java.util.Date;

import edu.umich.si.inteco.minukucore.model.DataRecord;

/**
 * Created by neerajkumar on 8/22/16.
 */
public class UserSubmissionStats implements DataRecord {

    public int questionCount;
    public int totalImageCount;
    public int glucoseReadingCount;
    public int insulinCount;
    public int foodCount;
    public int othersCount;
    public long creationTime;
    public int totalSubmissionCount;
    public int moodCount;

    public int getMoodCount() {
        return moodCount;
    }

    public void setMoodCount(int moodCount) {
        this.moodCount = moodCount;
    }

    public void incrementMoodCount() {
        this.moodCount++;
        this.totalSubmissionCount++;
    }

    public UserSubmissionStats() {
        this.creationTime = new Date().getTime();
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    public void incrementQuestionCount() {
        this.questionCount++;
        this.totalSubmissionCount++;
    }

    public void incrementTotalImageCount() {
        this.totalImageCount++;
        this.totalSubmissionCount++;
    }

    public void incrementGlucoseReadingCount() {
        this.glucoseReadingCount++;
        this.totalSubmissionCount++;
    }

    public void incrementInsulinCount() {
        this.insulinCount++;
        this.totalSubmissionCount++;
    }

    public void incrementFoodCount() {
        this.foodCount++;
        this.totalSubmissionCount++;
    }

    public void incrementOtherImagesCount() {
        this.othersCount++;
        this.totalSubmissionCount++;
    }


    /**
     *
     * @param creationTime
     */
    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public void setTotalImageCount(int totalImageCount) {
        this.totalImageCount = totalImageCount;
    }

    public void setGlucoseReadingCount(int glucoseReadingCount) {
        this.glucoseReadingCount = glucoseReadingCount;
    }

    public void setInsulinCount(int insulinCount) {
        this.insulinCount = insulinCount;
    }

    public void setFoodCount(int foodCount) {
        this.foodCount = foodCount;
    }

    public void setOthersCount(int othersCount) {
        this.othersCount = othersCount;
    }

    public void setTotalSubmissionCount(int totalSubmissionCount) {
        this.totalSubmissionCount = totalSubmissionCount;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public int getTotalImageCount() {
        return totalImageCount;
    }

    public int getGlucoseReadingCount() {
        return glucoseReadingCount;
    }

    public int getInsulinCount() {
        return insulinCount;
    }

    public int getFoodCount() {
        return foodCount;
    }

    public int getOthersCount() {
        return othersCount;
    }

    public int getTotalSubmissionCount() {
        return totalSubmissionCount;
    }
}
