package edu.umich.si.inteco.minukucore.model.question;

import java.util.Date;

import edu.umich.si.inteco.minukucore.model.DataRecord;

/**
 * A question is a specific type of {@link DataRecord}. Each time a new question is supposed to
 * be asked of the user, a new subclass of Question must be created. e.g:
 * QuestionOne extends Question --> question = "How was your day?"
 * QuestionTwo extends Question --> question = "How was your mood after school?".
 * Objects of QuestionOne and QuestionTwo will have their own stream(s).
 *
 * Created by neerajkumar on 7/13/16.
 */
public abstract class Question implements DataRecord {
    // protected UUID questionnaireID;

    private String question;
    private int ID;
    private long creationTime;

    public Question() {

    }

    /**
     *
     * @param aQuestion The question string. This will not be persisted in DB, but must be a part
     *                  of the code.
     */
    public Question(String aQuestion) {
        this.question = aQuestion;
        this.creationTime = new Date().getTime();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public long getCreationTime() {
        return  this.creationTime;
    }

    public String getQuestion() { return this.question; }

    public void setQuestion(String aQuestion) { this.question = aQuestion; }

    @Override
    public int hashCode() {
        return this.ID;
    }
}
