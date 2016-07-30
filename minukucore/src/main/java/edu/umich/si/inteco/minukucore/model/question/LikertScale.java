package edu.umich.si.inteco.minukucore.model.question;

import java.lang.reflect.AnnotatedElement;

/**
 * A Likert scale question gives user a question and expects an answer on a gradient.
 * @see <a href="https://en.wikipedia.org/wiki/Likert_scale">Wiki on Likert Scale</a> for more info.
 *
 * Created by neerajkumar on 7/13/16.
 */
public abstract class LikertScale extends Question {

    private static int numSteps;
    private static String[] stepValues;

    private int answerValue;

    public LikertScale() {

    }

    /**
     *
     * @param aQuestion The question to show.
     * @param aNumSteps The number of steps on the likert scale.
     * @param aStepValues Label to be shown for each tick on the scale.
     */
    public LikertScale(String aQuestion, int aNumSteps, String[] aStepValues) {
        super(aQuestion);
        stepValues = aStepValues;
        numSteps = aNumSteps;
    }

    /**
     *
     * @return Number of steps on the likert scale.
     */
    public int getNumSteps() {
        return numSteps;
    }

    public void setNumSteps(int steps) {
        numSteps = steps;
    }

    /**
     *
     * @return The array of labels for the scale.
     */
    public String[] getStepValues() {
        return stepValues;
    }

    public void setStepValues(String[] values) {
        stepValues = values;
    }

    /**
     *
     * @return The answer selected by the user.
     */
    public int getAnswerValue() {
        return answerValue;
    }

    /**
     * Sets the answer value.
     * @param aAnswerValue The answer selected by the user.
     */
    public void setAnswerValue(int aAnswerValue) {
        this.answerValue = aAnswerValue;
    }
}
