package edu.umich.si.inteco.minukucore.model.question;

/**
 * A multiple choice question gives users a specific question with a list of possible answers the
 * user needs to choose from.
 * @see <a href="https://en.wikipedia.org/wiki/Multiple_choice"> Wiki on Multiple Choice</a>.
 *
 * Created by neerajkumar on 7/13/16.
 */
public abstract class MultipleChoiceQuestion extends Question {

    private static int numChoices;
    private static String[] choiceValues;

    private int answerValue;

    /**
     *
     * @param aQuestion The question to show.
     * @param aNumChoices The number of choices this MCQ will have.
     * @param aChoiceValues The label(s) for each choice.
     */
    public MultipleChoiceQuestion(String aQuestion, int aNumChoices, String[] aChoiceValues) {
        super(aQuestion);
        numChoices = aNumChoices;
        choiceValues = aChoiceValues;
    }

    /**
     *
     * @return The number of choices.
     */
    public static int getNumChoices() {
        return numChoices;
    }

    /**
     *
     * @return The labels for choices.
     */
    public static String[] getChoiceValues() {
        return choiceValues;
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
